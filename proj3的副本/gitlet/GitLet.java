package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;


public static final File CWD = new File(System.getProperty("user.dir"));
public static final File GITLET_DIR = Utils.join(CWD, ".gitlet");
public static final File BRANCH_DIR = Utils.join(GITLET_DIR, "Branches");
public static final File HEAD = Utils.join(GITLET_DIR, "Head.txt");
public static final File _stagingArea = Utils.join(GITLET_DIR, "StagingArea.txt");
public static final File COMMIT_DIR = Utils.join(GITLET_DIR, "Commits");
public static final File BLOB_DIR = Utils.join(GITLET_DIR, "Blobs");
public static final File REMOTE = Utils.join(GITLET_DIR, "RemotePath");



public class GitLet {

    public GitLet() {


    }

    public void init(String... args) {
        if (GITLET_DIR.exists()) {
            throw new GitletException("A Gitlet version-control system "
                    + "already exists in the current directory.");
        }
        GITLET_DIR.mkdir();
        BRANCH_DIR.mkdir();
        COMMIT_DIR.mkdir();
        BLOB_DIR.mkdir();
        REMOTE.mkdir();

        StagingArea s = new StagingArea();
        s.saveContents();

        Commit init = new Commit("initial commit", null, new TreeMap<>());
        init.saveContents();

        Branch master = new Branch("master", init.getCode());
        master.saveContents();

        Head head = new Head("master");
        head.saveContents();

    }

    public void add(String... args) {
        checkExistence();

        String name = args[1];
        File file = Utils.join(CWD, name);

        if (!file.exists()) {
            throw new GitletException("File does not exist.");
        }

        byte[] contents = Utils.readContents(file);
        String code = Utils.sha1(contents);

        Commit headCommit = getHeadCommit();
        StagingArea s = Utils.readObject(_stagingArea, StagingArea.class);
        if (headCommit.containFile(name, code)) {
            if (s.containAddedFiles(name, code)) {
                s.removeAddedFile(name, code);
            }
        } else {
            s.addFile(name, code);
            Blob blob = new Blob(code, file);
            blob.saveContents();
        }
        s.removeRemovedFile(name);
        s.saveContents();
    }

    public void commit(String... args) {
        checkExistence();

        String message = args[1];
        if (message.equals("")) {
            throw new GitletException("Please enter a commit message.");
        }

        generalCommit(message, null, false);

    }


    private void generalCommit(String message,
                               String secondParent, boolean flag) {

        Commit lastCommit = getHeadCommit();
        TreeMap<String, String> updatedPrevFiles =
                new TreeMap<>(lastCommit.getBlobs());

        StagingArea s = Utils.readObject(_stagingArea, StagingArea.class);
        TreeMap<String, String> allAddedFiles = s.getAddedFiles();
        TreeMap<String, String> allRemovedFiles = s.getRemovedFiles();

        if (allAddedFiles.isEmpty() && allRemovedFiles.isEmpty()) {
            throw new GitletException("No changes added to the commit.");
        }
        String prev;
        String curr;
        for (String name: allAddedFiles.keySet()) {
            if (updatedPrevFiles.containsKey(name)) {
                prev = updatedPrevFiles.get(name);
                curr = allAddedFiles.get(name);
                if (!prev.equals(curr)) {
                    updatedPrevFiles.put(name, allAddedFiles.get(name));
                }
            } else {
                updatedPrevFiles.put(name, allAddedFiles.get(name));
            }
        }

        for (String name: allRemovedFiles.keySet()) {
            if (updatedPrevFiles.containsKey(name)) {
                prev = updatedPrevFiles.get(name);
                curr = allRemovedFiles.get(name);
                if (prev.equals(curr)) {
                    updatedPrevFiles.remove(name, curr);
                }
            }
        }

        Commit next;
        if (!flag) {
            next = new Commit(message, lastCommit.getCode(), updatedPrevFiles);
        } else {
            next = new Merge(message,
                    lastCommit.getCode(), secondParent, updatedPrevFiles);
        }
        next.saveContents();

        s = new StagingArea();
        s.saveContents();

        Branch headBranch = getHeadBranch();

        headBranch.update(next.getCode());
        headBranch.saveContents();
    }
    public void remove(String... args) {
        checkExistence();

        String filename = args[1];
        Commit curr = getHeadCommit();

        StagingArea s = Utils.readObject(_stagingArea, StagingArea.class);

        boolean flag = false;
        if (s.containAddedFiles(filename)) {
            s.removeAddedFile(filename);
            flag = !flag;
        }
        if (curr.containFile(filename)) {
            String code = curr.getBlobs().get(filename);
            s.addFile(filename, code);
            Utils.restrictedDelete(filename);
            s.removeFile(filename);
            flag = !flag;
        }
        if (!flag) {
            throw new GitletException("No reason to remove the file.");
        }

        s.saveContents();
    }

    public void checkOut(String... args) {
        checkExistence();
        if (args.length == 2) {
            checkOutAll(args[1]);
        } else if (args.length == 3) {
            Branch head = getHeadBranch();
            checkOutOne(head.getCommit(), args[2]);
        } else {
            checkOutOne(args[1], args[3]);
        }
    }

    private void checkOutOne(String sha1, String file) {
        File commitPath = null;
        if (sha1.length() == LIMIT) {
            commitPath = Utils.join(COMMIT_DIR, sha1 + ".txt");
        } else {
            int len = sha1.length();
            for (String name: Utils.plainFilenamesIn(COMMIT_DIR)) {
                if (len > LIMIT) {
                    break;
                }
                String sub = name.substring(0, len);
                if (sub.equals(sha1)) {
                    commitPath = Utils.join(COMMIT_DIR, name);
                    break;
                }
            }
        }

        if (commitPath == null || !commitPath.exists()) {
            throw new GitletException("No commit with that id exists.");
        }
        Commit commit = Utils.readObject(commitPath, Commit.class);
        if (!commit.containFile(file)) {
            throw new GitletException("File does not exist in that commit.");
        }
        File blobPath = Utils.join(BLOB_DIR, commit.getBlob(file) + ".txt");
        byte[] contents =  Utils.readContents(blobPath);
        File filePath = Utils.join(CWD, file);
        Utils.writeContents(filePath, contents);
    }

    private void checkOutAll(String name) {
        File branchPath = Utils.join(BRANCH_DIR, Utils.sha1(name) + ".txt");
        if (!branchPath.exists()) {
            throw new GitletException("No such branch exists.");
        }

        if (name.equals(getHeadBranch().getName())) {
            throw new GitletException("No need to "
                    + "checkout the current branch.");
        }

        Commit curr = getHeadCommit();
        StagingArea s = Utils.readObject(_stagingArea, StagingArea.class);
        Branch prev = Utils.readObject(branchPath, Branch.class);
        File prevCommitFile = Utils.join(COMMIT_DIR, prev.getCommit() + ".txt");
        Commit prevCommit = Utils.readObject(prevCommitFile, Commit.class);


        for (String fileName: Utils.plainFilenamesIn(CWD)) {
            String sha1 = Utils.sha1(Utils.readContents
                    (Utils.join(CWD, fileName)));
            if (curr.containFile(fileName, sha1)
                    && !prevCommit.containFile(fileName, sha1)) {
                Utils.restrictedDelete(fileName);
            }
        }

        for (String changedFileName: prevCommit.getBlobs().keySet()) {
            String sha1 = prevCommit.getBlob(changedFileName);
            if (!curr.containFile(changedFileName)
                    && Utils.join(CWD, changedFileName).exists()
                            && !s.containAddedFiles(changedFileName)) {
                throw new GitletException("There is an untracked "
                        + "file in the way; delete it"
                        + ", or add and commit it first.");
            }
            byte[] contents = Utils.readContents(
                    Utils.join(BLOB_DIR, sha1 + ".txt"));
            Utils.writeContents(Utils.join(CWD, changedFileName), contents);
        }

        Head newHead = new Head(name);
        newHead.saveContents();

        s = new StagingArea();
        s.saveContents();

    }
    public void log(String... args) {
        checkExistence();

        Branch headBranch = getHeadBranch();
        String code = headBranch.getCommit();
        File commitFile;
        Commit commit;
        while (code != null) {
            commitFile = Utils.join(COMMIT_DIR, code + ".txt");
            commit = Utils.readObject(commitFile, Commit.class);
            System.out.println(commit);
            code = commit.getFirstParent();
            System.out.println();
        }
    }


    public void globalLog(String... args) {
        checkExistence();

        List<String> allFiles = Utils.plainFilenamesIn(COMMIT_DIR);
        Commit commit;
        for (String file: allFiles) {
            commit = Utils.readObject(Utils.join(
                    COMMIT_DIR, file), Commit.class);
            System.out.println(commit);
            System.out.println();
        }
    }

    public void find(String... args) {
        checkExistence();

        String message = args[1];
        List<String> allFiles = Utils.plainFilenamesIn(COMMIT_DIR);
        Commit commit;
        boolean flag = false;
        for (String file: allFiles) {
            commit = Utils.readObject(Utils.join(
                    COMMIT_DIR, file), Commit.class);
            if (commit.getMessage().equals(message)) {
                flag = true;
                System.out.println(commit.getCode());
            }
        }
        if (!flag) {
            throw new GitletException("Found no commit with that message.");
        }
    }

    public void status(String... args) {
        checkExistence();
        Branch headBranch = getHeadBranch();
        String headBranchName = headBranch.getName();
        List<String> allBranches = new ArrayList<>();
        for (String filename: Utils.plainFilenamesIn(BRANCH_DIR)) {
            File path = Utils.join(BRANCH_DIR, filename);
            Branch curr = Utils.readObject(path, Branch.class);
            String name = curr.getName();
            if (name.equals(headBranchName)) {
                allBranches.add("*" + name);
            } else {
                allBranches.add(name);
            }
        }
        Collections.sort(allBranches);
        StagingArea s = Utils.readObject(_stagingArea, StagingArea.class);
        List<String> allAddedFiles = new ArrayList<>(
                s.getAddedFiles().keySet());
        List<String> allRemovedFiles = new ArrayList<>(
                s.getRemovedFiles().keySet());
        Collections.sort(allAddedFiles); Collections.sort(allRemovedFiles);
        Commit curr = getHeadCommit();
        List<String> modifiedNotStagedFiles = new ArrayList<>();
        List<String> untrackedFiles = new ArrayList<>();
        statusHelper(curr, s, modifiedNotStagedFiles,
                untrackedFiles, allAddedFiles, allBranches, allRemovedFiles);
    }

    private void statusHelper(Commit curr, StagingArea s,
                              List<String> modifiedNotStagedFiles,
                              List<String> untrackedFiles,
                              List<String> allAddedFiles,
                              List<String> allBranches,
                              List<String> allRemovedFiles) {
        for (String name: Utils.plainFilenamesIn(CWD)) {
            String sha1 = Utils.sha1(
                    Utils.readContents(Utils.join(CWD, name)));
            if (curr.containFile(name) && !curr.containFile(name, sha1)
                    && !s.containAddedFiles(name, sha1)) {
                modifiedNotStagedFiles.add(name + " (modified)");
            } else if (s.containAddedFiles(name)
                    && !s.containAddedFiles(name, sha1)) {
                modifiedNotStagedFiles.add(name + " (modified)");
            } else if (!curr.containFile(name, sha1)
                    && !s.containAddedFiles(name)) {
                untrackedFiles.add(name);
            }
        }
        for (String name: allAddedFiles) {
            if (!Utils.join(CWD, name).exists()) {
                modifiedNotStagedFiles.add(name + " (deleted)");
            }
        }
        for (String name: curr.getBlobs().keySet()) {
            if (!s.containRemovedFiles(name) && !Utils.join(CWD, name).exists()
                    && !s.containRemovedFiles(name)) {
                modifiedNotStagedFiles.add(name + " (deleted)");
            }
        }
        Collections.sort(modifiedNotStagedFiles);
        Collections.sort(untrackedFiles);
        System.out.println("=== Branches ===");
        for (String name: allBranches) {
            System.out.println(name);
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        for (String name: allAddedFiles) {
            System.out.println(name);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String name: allRemovedFiles) {
            System.out.println(name);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String name: modifiedNotStagedFiles) {
            System.out.println(name);
        }
        System.out.println();

        System.out.println("=== Untracked Files ===");
        for (String name: untrackedFiles) {
            System.out.println(name);
        }
    }

    public void branch(String... args) {
        checkExistence();
        String branchName = args[1];
        File path = Utils.join(BRANCH_DIR, Utils.sha1(branchName) + ".txt");
        if (path.exists()) {
            throw new GitletException(
                    "A branch with that name already exists.");
        }

        Commit headCommit = getHeadCommit();
        Branch newBr = new Branch(branchName, headCommit.getCode());
        newBr.saveContents();
    }

    public void removeBranch(String... args) {
        checkExistence();
        Branch headBranch = getHeadBranch();
        String deletedBranchName = args[1];
        File deletedBranch = Utils.join(BRANCH_DIR,
                Utils.sha1(deletedBranchName) + ".txt");
        if (!deletedBranch.exists()) {
            throw new GitletException(
                    "A branch with that name does not exist.");
        }

        if (headBranch.getName().equals(deletedBranchName)) {
            throw new GitletException("Cannot remove the current branch.");
        }

        deletedBranch.delete();
    }

    public void reset(String... args) {
        checkExistence();
        String commitId = args[1];
        if (commitId.length() > LIMIT) {
            throw new GitletException("No commit with that id exists.");
        }

        if (commitId.length() != LIMIT) {
            boolean flag = false;
            int len = commitId.length();
            for (String name: Utils.plainFilenamesIn(COMMIT_DIR)) {
                String sub = name.substring(0, len);
                if (sub.equals(commitId)) {
                    commitId = name.substring(0, LIMIT);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                throw new GitletException("No commit with that id exists.");
            }
        }

        File commitPath = Utils.join(COMMIT_DIR, commitId + ".txt");
        if (!commitPath.exists()) {
            throw new GitletException("No commit with that id exists.");
        }

        Branch headBranch = getHeadBranch();

        Branch tmp = new Branch("tmp", commitId);
        tmp.saveContents();
        checkOutAll(tmp.getName());

        headBranch.update(commitId);
        headBranch.saveContents();

        Head head = new Head(headBranch.getName());
        head.saveContents();
        Utils.join(BRANCH_DIR,   Utils.sha1("tmp") + ".txt").delete();

    }

    public void merge(String... args) {
        checkExistence();
        StagingArea s = Utils.readObject(_stagingArea, StagingArea.class);
        if (!s.getAddedFiles().isEmpty() || !s.getRemovedFiles().isEmpty()) {
            throw new GitletException("You have uncommitted changes.");
        }
        _s = new StagingArea(s); String mergedBranchName = args[1];
        File mergedBranchPath = Utils.join(BRANCH_DIR,
                Utils.sha1(mergedBranchName) + ".txt");
        if (!mergedBranchPath.exists()) {
            throw new GitletException(
                    "A branch with that name does not exist.");
        }

        Branch mergedBranch = Utils.readObject(mergedBranchPath, Branch.class);
        if (mergedBranch.getName().equals(getHeadBranch().getName())) {
            throw new GitletException("Cannot merge a branch with itself.");
        }

        String headCommitId = getHeadBranch().getCommit();
        String mergedCommitId = mergedBranch.getCommit();
        String latestCommonAncestor = Graph.findLatestCommonAncestor
                (headCommitId, mergedCommitId);
        if (mergedCommitId.equals(latestCommonAncestor)) {
            throw new GitletException(
                    "Given branch is an ancestor of the current branch.");
        }
        if (headCommitId.equals(latestCommonAncestor)) {
            checkOutAll(mergedBranchName);
            throw new GitletException("Current branch fast-forwarded.");
        }
        TreeMap<String, String> untrackedFiles = new TreeMap<>();
        for (String fileName: Utils.plainFilenamesIn(CWD)) {
            String sha1 = Utils.sha1(
                    Utils.readContents(Utils.join(CWD, fileName)));
            if (!getHeadCommit().containFile(fileName, sha1)) {
                untrackedFiles.put(fileName, sha1);
            }
        }

        for (String fileName: Utils.plainFilenamesIn(CWD)) {
            _storage.put(fileName, Utils.readContentsAsString(
                    Utils.join(CWD, fileName)));
        }

        Commit splitPointCommit = Utils.readObject(Utils.join
                (COMMIT_DIR, latestCommonAncestor + ".txt"), Commit.class);
        Commit headCommit = Utils.readObject(Utils.join
                (COMMIT_DIR, headCommitId + ".txt"), Commit.class);
        Commit mergedCommit = Utils.readObject(Utils.join
                (COMMIT_DIR, mergedCommitId + ".txt"), Commit.class);
        TreeMap<String, String> splitPointFiles =
                new TreeMap<>(splitPointCommit.getBlobs());
        TreeMap<String, String> headFiles =
                new TreeMap<>(headCommit.getBlobs());
        TreeMap<String, String> mergedFiles =
                new TreeMap<>(mergedCommit.getBlobs());
        mergeHelper(splitPointFiles, headFiles, mergedFiles,
                untrackedFiles, mergedBranchName, mergedCommitId);
    }

    private void mergeHelper(TreeMap<String, String> splitPointFiles,
                             TreeMap<String, String> headFiles,
                             TreeMap<String, String> mergedFiles,
                             TreeMap<String, String> untrackedFiles,
                             String mergedBranchName,
                             String mergedCommitId) {

        TreeMap<String, String> tagHeadFiles = new TreeMap<>();
        TreeMap<String, String> tagMergedFiles = new TreeMap<>();

        helper(splitPointFiles, headFiles, tagHeadFiles);
        helper(splitPointFiles, mergedFiles, tagMergedFiles);
        boolean flag = false;

        for (String fileName: tagMergedFiles.keySet()) {
            String mergedSha1 = mergedFiles.get(fileName);
            String headSha1 = headFiles.get(fileName);
            String mergedTag = tagMergedFiles.get(fileName);
            String headTag = tagHeadFiles.get(fileName);
            File writePath = Utils.join(CWD, fileName);
            if (mergedTag.equals("changed")) {
                if (headTag == null || headTag.equals("unchanged")) {
                    untrackedException(fileName, mergedSha1, untrackedFiles);
                    checkOutOne(mergedCommitId, fileName);
                    add(new String[] {"add", fileName});

                } else if ((headTag.equals("changed")
                        && !mergedSha1.equals(headSha1))
                        || headTag.equals("deleted")) {
                    flag = true;
                    String contents = getConflictedContents(
                            mergedSha1, headSha1);
                    untrackedException(fileName, Utils.sha1(
                            contents), untrackedFiles);
                    Utils.writeContents(writePath, contents);
                    add(new String[] {"add", fileName});
                }
            } else if (mergedTag.equals("deleted")) {
                if (headTag.equals("unchanged")) {
                    remove(new String[] {"rm", fileName});
                } else if (headTag.equals("changed")) {
                    flag = true;
                    String contents = getConflictedContents(
                            mergedSha1, headSha1);
                    untrackedException(fileName, Utils.sha1(
                            contents), untrackedFiles);
                    Utils.writeContents(writePath, contents);
                    add(new String[] {"add", fileName});
                }
            }
        }


        generalCommit("Merged" + " " + mergedBranchName + " into "
                + getHeadBranch().getName() + ".", mergedCommitId, true);
        if (flag) {
            throw new GitletException("Encountered a merge conflict.");
        }
    }

    private void untrackedException(String filename, String sha1,
                                    TreeMap<String, String> untrackedFiles) {
        String localSha1 = untrackedFiles.get(filename);
        if (localSha1 != null && !sha1.equals(localSha1)) {
            for (String name: _storage.keySet()) {
                Utils.writeContents(Utils.join(CWD, name), _storage.get(name));
            }
            _s.saveContents();
            throw new GitletException("There is an untracked file in "
                    + "the way; delete it, or add and commit it first.");
        }
    }

    private String getConflictedContents(String mergedSha1, String headSha1) {
        String headContents = processSha1(headSha1);
        String mergedContents = processSha1(mergedSha1);
        String contents = "<<<<<<< HEAD\n" + headContents
                + "=======\n" + mergedContents
                + ">>>>>>>\n";
        return contents;
    }

    private String processSha1(String sha1) {
        String headContents = "";
        if (sha1 != null) {
            File headBlobPath = Utils.join(
                    BLOB_DIR, sha1 + ".txt");
            headContents = new String(Utils.readContents(headBlobPath));
        }
        return headContents;
    }
    private void helper(TreeMap<String, String> st,
                        TreeMap<String, String> allFiles,
                        TreeMap<String, String> tags) {

        for (String fileName: allFiles.keySet()) {
            String sha1 = st.get(fileName);
            if (allFiles.get(fileName).equals(sha1)) {
                tags.put(fileName, "unchanged");
            } else {
                tags.put(fileName, "changed");
            }
        }

        for (String fileName: st.keySet()) {
            if (!allFiles.containsKey(fileName)) {
                tags.put(fileName, "deleted");
            }
        }
    }

    public void addRemote(String... args) {
        checkExistence();
        String remoteName = args[1];
        String remoteDirectoryPath = args[2];
        File remotePath = Utils.join(REMOTE, remoteName + ".txt");
        if (remotePath.exists()) {
            throw new GitletException("A remote "
                    + "with that name already exists.");
        }

        String newRemoteDirectoryPath = "";
        for (int i = 0; i < remoteDirectoryPath.length(); i += 1) {
            char curr = remoteDirectoryPath.charAt(i);
            if (curr == '/') {
                newRemoteDirectoryPath += File.separator;
            } else {
                newRemoteDirectoryPath += curr;
            }
        }


        Utils.writeContents(remotePath, newRemoteDirectoryPath);

    }

    public void removeRemote(String... args) {
        checkExistence();
        String remoteName = args[1];
        File remotePathDir = Utils.join(REMOTE, remoteName + ".txt");
        String name = remotePathDir.getName();
        if (!remotePathDir.exists()) {
            throw new GitletException("A "
                    + "remote with that name does not exist.");
        }
        remotePathDir.delete();
    }

    public void push(String... args) {
        checkExistence();
        String remoteName = args[1];
        File remotePathDir = Utils.join(REMOTE, remoteName + ".txt");
        String remotePath = new String(Utils.readContents(remotePathDir));
        File remoteRepo = new File(remotePath);
        if (!remoteRepo.exists()) {
            throw new GitletException("Remote directory not found.");
        }
        String remoteBranchName = args[2];
        String localRemoteName = remoteName + "/" + remoteBranchName;
        File localRemoteBranchPath = Utils.join(
                BRANCH_DIR, Utils.sha1(localRemoteName) + ".txt");
        if (!localRemoteBranchPath.exists()) {
            throw new GitletException("Please "
                    + "pull down remote changes before pushing.");
        }
        Branch localRemoteBranch = Utils.readObject(
                localRemoteBranchPath, Branch.class);
        String localRemoteCommitId = localRemoteBranch.getCommit();
        String headCommitId = getHeadCommit().getCode();
        List<Commit> allFutureCommits = Graph.
                findFutureCommits(localRemoteCommitId, headCommitId);
        for (Commit c: allFutureCommits) {
            File file = Utils.join(Utils.join(
                    remoteRepo, "Commits"), c.getCode() + ".txt");
            Utils.writeObject(file, c);
            for (String sha1: c.getBlobs().values()) {
                File localBlob = Utils.join(BLOB_DIR, sha1 + ".txt");
                byte[] contents = Utils.readContents(localBlob);
                File remoteBlob = Utils.join(
                        Utils.join(remoteRepo, "Blobs"), sha1 + ".txt");
                Utils.writeContents(remoteBlob, contents);
            }
        }
        CWD = new File(remoteRepo.getName().replace(
                File.separator + ".gitlet", ""));
        GITLET_DIR = remoteRepo;
        BRANCH_DIR = Utils.join(GITLET_DIR, "Branches");
        HEAD = Utils.join(GITLET_DIR, "Head.txt");
        _stagingArea = Utils.join(GITLET_DIR, "StagingArea.txt");
        COMMIT_DIR = Utils.join(GITLET_DIR, "Commits");
        BLOB_DIR = Utils.join(GITLET_DIR, "Blobs");
        REMOTE = Utils.join(GITLET_DIR, "RemotePath");

        reset(new String[]{"reset", headCommitId});


    }

    public void fetch(String... args) {
        checkExistence();
        String remoteName = args[1];
        File remotePathDir = Utils.join(REMOTE, remoteName + ".txt");
        String remotePath = new String(Utils.readContents(remotePathDir));
        File remoteRepo = new File(remotePath);
        if (!remoteRepo.exists()) {
            throw new GitletException("Remote directory not found.");
        }

        String remoteBranchName = args[2];
        String localName = remoteName + "/" + remoteBranchName;
        if (!Utils.join(BRANCH_DIR, Utils.sha1(localName) + ".txt").exists()) {
            String[] newBranchCmd = new String[]{"branch", localName};
            branch(newBranchCmd);
        }

        File remoteBranch = Utils.join(Utils.join(remoteRepo,
                "Branches"),
                Utils.sha1(remoteBranchName) + ".txt");
        if (!remoteBranch.exists()) {
            throw new GitletException("That remote does not have that branch.");
        }

        File remoteAllCommits = Utils.join(remoteRepo, "Commits");
        File remoteAllBlobs = Utils.join(remoteRepo, "Blobs");
        String commitId = Utils.readObject(
                remoteBranch, Branch.class).getCommit();

        getAllRemoteContents(commitId, remoteAllCommits, remoteAllBlobs);

        Branch fetchedBranch = Utils.readObject(Utils.join(
                BRANCH_DIR, Utils.sha1(localName) + ".txt"), Branch.class);
        fetchedBranch.update(commitId);
        fetchedBranch.saveContents();
    }

    public void pull(String... args) {
        checkExistence();
        String remoteName = args[1];
        String remoteBranchName = args[2];
        String localName = remoteName + "/" + remoteBranchName;
        String[] mergedArgs = new String[] {"merge", localName};
        fetch(args);
        merge(mergedArgs);
    }

    private void getAllRemoteContents(
            String id, File remoteAllCommits, File remoteAllBlobs) {
        if (id == null) {
            return;
        }

        File remoteCommitPath = Utils.join(remoteAllCommits, id + ".txt");
        Commit remoteCommit = Utils.readObject(remoteCommitPath, Commit.class);
        File localCommitPath = Utils.join(COMMIT_DIR, id + ".txt");
        Utils.writeObject(localCommitPath, remoteCommit);
        for (String sha1: remoteCommit.getBlobs().values()) {
            File remoteBlob = Utils.join(remoteAllBlobs, sha1 + ".txt");
            byte[] contents = Utils.readContents(remoteBlob);
            File localBlob = Utils.join(BLOB_DIR, sha1 + ".txt");
            Utils.writeContents(localBlob, contents);
        }
        getAllRemoteContents(remoteCommit.getFirstParent(),
                remoteAllCommits, remoteAllBlobs);
        getAllRemoteContents(remoteCommit.getSecondParent(),
                remoteAllCommits, remoteAllBlobs);
    }



    private Commit getHeadCommit() {
        File commitFile = Utils.join(COMMIT_DIR, getHeadBranch().
                getCommit() + ".txt");
        Commit curr = Utils.readObject(commitFile, Commit.class);
        return curr;
    }

    private Branch getHeadBranch() {
        Head head = Utils.readObject(HEAD, Head.class);
        String lastBranchName = head.getName();
        File lastBranchPath = Utils.join(
                BRANCH_DIR, Utils.sha1(lastBranchName) + ".txt");
        Branch last = Utils.readObject(lastBranchPath, Branch.class);
        return last;
    }

    private static void checkExistence() {
        if (!GITLET_DIR.exists()) {
            throw new GitletException(
                    "Not in an initialized Gitlet directory.");
        }
    }

    public static File getRepository() {
        return GITLET_DIR;
    }

    public static File getCwd() {
        return CWD;
    }

    public static File getStagingArea() {
        return _stagingArea;
    }

    public static File getBranches() {
        return BRANCH_DIR;
    }

    public static File getBlobs() {
        return BLOB_DIR;
    }

    public static File getCommits() {
        return COMMIT_DIR;
    }

    public static File getHEAD() {
        return HEAD;
    }


    /**The upper limit for the length of Sha1 code.*/
    private static final int LIMIT = 40;

    /**The working directory.*/
    private static File CWD;

    /**The file for .Gitlet folder.*/
    private static File GITLET_DIR;

    /**The file for the staging area.*/
    private static File _stagingArea;

    /**The file for the branch.*/
    private static File BRANCH_DIR;

    /**The file for commits.*/
    private static File COMMIT_DIR;

    /**The file for blobs.*/
    private static File BLOB_DIR;

    /**The file for the remote directory.*/
    private static File REMOTE;

    /**The current branch.*/
    private static File HEAD;

    /**The previous version of CWD.*/
    private static TreeMap<String, String> _storage = new TreeMap<>();

    /**The previous Staging Area.*/
    private StagingArea _s;
}
