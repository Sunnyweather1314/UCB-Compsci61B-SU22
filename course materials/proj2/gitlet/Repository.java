package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  @author Jiayi Xu
 */

public class Repository implements Serializable {
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = Utils.join(CWD, ".gitlet");
    /** The head pointer of the CWD, kept in .gitlet folder*/
    public static final File HEAD = Utils.join(GITLET_DIR, "Head.txt");
    /** The commit directory, commit instances will be written inside this directory. */
    public static final File COMMIT_DIR = Utils.join(GITLET_DIR, "Commits");
    /** The blobs that has been added or commited*/
    public static final File BLOB_DIR = Utils.join(GITLET_DIR, "Blobs");
    public static final File BRANCH_DIR = Utils.join(GITLET_DIR, "Branches");
    public static final File REPO_FILE = Utils.join(GITLET_DIR, "repo.txt");
    private HashMap<String, File> stage;
    private File headCommit;
    private HashMap<String, File> branches;
//    public Branch main;
//    public Branch otherBranch;
    private ArrayList<String> removed;
    private String crrBranch;


    public Repository() {
    }

    public void init() {
        if (GITLET_DIR.exists()) {
            System.out.println(
                "A Gitlet version-control system already exists in the current directory.");
            return;
        }

        if (!REPO_FILE.exists()) {
            GITLET_DIR.mkdir();
            COMMIT_DIR.mkdir();
            BLOB_DIR.mkdir();
            BRANCH_DIR.mkdir();
            stage = new HashMap<String, File>();
            removed = new ArrayList<String>();
            branches = new HashMap<String, File>();
            save(this);
            Repository.branch("main");


            Repository repo = Utils.readObject(REPO_FILE, Repository.class);
            repo.commit("initial commit");

//            System.out.println("this one: " + repo.headCommit);
            repo = Utils.readObject(REPO_FILE, Repository.class);
//            Utils.readObject(repo.branches.get("main"),Commit.
            Branch branch = Utils.readObject(Utils.join(BRANCH_DIR, "main"), Branch.class);
            branch.setCommit(repo.headCommit);
            branch.saveBranch();
            repo.crrBranch = "main";
            repo.save(repo);


        }
    }

    public void add(String file) {
        File fileRef = Utils.join(CWD, file);
//            Scanner sc = new Scanner(Utils.readContentsAsString(fileRef));
//
//            while (sc.hasNextLine()) {
//                String i = sc.nextLine();
//                System.out.println(i);
//            }
//            sc.close();
        if (!checkExistence(fileRef)) {
            return;
        }
        Repository repo = Utils.readObject(REPO_FILE, Repository.class);
        Commit head = Utils.readObject(repo.headCommit, Commit.class);

        if (repo.removed.contains(file)) {
            repo.removed.remove(file);
            repo.save(repo);
//            System.out.println(repo.removed);
            return;
        }

        if (head.getBlobs().keySet().contains(file)) {
            String adding = Utils.readContentsAsString(fileRef);
            Blob addBlob = Utils.readObject((File) head.getBlobs().get(file), Blob.class);
            String added = addBlob.getContents();
//            boolean x= adding.equals(added);
            if (!adding.equals(added)) {
                Blob blob = new Blob(fileRef, file);
                blob.save();
                repo.stage.put(blob.getName(), blob.getFileRef());
                repo.save(repo);
            }

        } else {
            Blob blob = new Blob(fileRef, file);
            blob.save();
            repo.stage.put(blob.getName(), blob.getFileRef());
            repo.save(repo);
        }



    }


    public void commit(String message) {
//        System.out.println(message);
        Repository repo = Utils.readObject(REPO_FILE, Repository.class);

        if (repo.stage.isEmpty() && repo.removed.isEmpty() && !message.equals("initial commit")) {
            System.out.println("No changes added to the commit.");
        }

        File[] parent = {repo.headCommit};

        Commit commited = new Commit(message, parent, repo.stage, repo.crrBranch, removed);
        repo.stage = new HashMap<String, File>();

        repo.headCommit = commited.getFileRef();
        repo.removed = new ArrayList<String>();
//        repo.crrBranch = repo.headCommit;
        repo.branches.put(commited.getBranch(), repo.headCommit);
        repo.save(repo);
    }


    public void remove(String filename) {
        Repository repo = Utils.readObject(REPO_FILE, Repository.class);
        File fileRef = Utils.join(CWD, filename);
//        checkExistence(fileRef);

        boolean contained = false;
        if (repo.stage.containsKey(filename)) {
            repo.stage.remove(filename);
//            repo.removed.add(filename);
            contained = true;
        }

        Commit head = Utils.readObject(repo.headCommit, Commit.class);
        if (head.getBlobs().containsKey(filename)) {
//            add(filename);
            repo.removed.add(filename);
            Collections.sort(repo.removed);
            Utils.restrictedDelete(fileRef);
            // s.removeFile(filename);
            contained = true;
        }
        if (!contained) {
            System.out.print("No reason to remove the file.");
            return;
        }
        repo.save(repo);

    }


    public void checkOut(String... args) {
        Repository repo = Utils.readObject(REPO_FILE, Repository.class);
        Commit head = Utils.readObject(repo.headCommit, Commit.class);
        if (args.length == 2) {
            if (!Utils.plainFilenamesIn(Repository.BRANCH_DIR).contains(args[1])) {
                System.out.println("No such branch exists.");
                return;
            }
            if (repo.crrBranch.equals(args[1])) {
                System.out.println("No need to checkout the current branch.");
                return;
            } else {
                if (!repo.branches.keySet().contains(args[1])) {
                    System.out.println("No such branch exists.");
                    return;
                }
                String prevBranch = repo.crrBranch;
                File prevRef = repo.branches.get(prevBranch);
                repo.crrBranch = args[1];
                head = Utils.readObject(repo.branches.get(args[1]), Commit.class);
                for (String fileName: Utils.plainFilenamesIn(CWD)) {
                    if (!repo.checkTracked(head.getFileRef(), prevRef, fileName)) {
                        return;
                    }
                }
                for (String fileName: Utils.plainFilenamesIn(CWD)) {
                    if (head.getBlobs().keySet().contains(fileName)) {
                        repo.checkOutOne(head, fileName);
                    } else {
                        Utils.restrictedDelete(fileName);
                    }
                }
                for (Object blobFileName: head.getBlobs().keySet()) {
                    if (!Utils.plainFilenamesIn(CWD).contains((String) blobFileName)) {
                        File blobRef = (File) head.getBlobs().get(blobFileName);
                        Blob blob = Utils.readObject(blobRef, Blob.class);
                        File localRef = Utils.join(CWD, (String) blobFileName);
                        Utils.writeContents(localRef, blob.getContents());
                    }
                }
                repo.headCommit = repo.branches.get(args[1]);
                repo.save(repo);
            }
        } else if (args.length == 3) {
            if (!args[1].equals("--")) {
                System.out.println("Incorrect operands.");
                return;
            }
            checkOutOne(head, args[2]);
        } else {
            if (!args[2].equals("--")) {
                System.out.println("Incorrect operands.");
                return;
            }
            Commit t = head;
            while (t != null) {
                String code = t.sha1Code();
                String arg = args[1];
                if (code.substring(0, arg.length()).equals(arg)) {
                    checkOutOne(t, args[3]);
                    break;
                }
                if (t.getParent().get(0) != null) {
                    t = Utils.readObject(t.getParent().get(0), Commit.class);
                } else {
//                    int q= 3/0;
                    t = null;
                    System.out.println("No commit with that id exists.");
                }
            }
        }
        repo.save(repo);
    }


    private void checkOutOne(Commit commit, String file) {
        Repository repo = Utils.readObject(REPO_FILE, Repository.class);

        if (commit.getBlobs().containsKey(file)) {
            Blob blob = Utils.readObject((File) commit.getBlobs().get(file), Blob.class);
//            File blobPath = blob.getFileRef();
            //int a = 3 / 0;

            //String contents = Utils.readContentsAsString(blobPath);
            File filePath = Utils.join(CWD, file);
            Utils.writeContents(filePath, new String(blob.getContents()));
        } else {

            System.out.println("File does not exist in that commit.");
        }

    }



    public void log() {
        Repository repo = Utils.readObject(REPO_FILE, Repository.class);
        Commit head = Utils.readObject(repo.headCommit, Commit.class);

        String time;
        String code;


        while (head != null) {
            if (head.getParent().size() > 1) {
                time = head.getTime();
                code = head.sha1Code();
                System.out.println("===");
                System.out.print("commit ");
                System.out.println(code);
                System.out.print("Merge: ");
                File parent1 = (File) head.getParent().get(0);
                File parent2 = (File) head.getParent().get(1);
                String par1sha = Utils.readObject(parent1, Commit.class).sha1Code();
                String par2sha = Utils.readObject(parent2, Commit.class).sha1Code();
                System.out.print(par1sha.substring(0, 7) + " ");
                System.out.println(par2sha.substring(0, 7));
                System.out.println("Date: " + time + " -0700");
                System.out.println(head.getMessage());
                System.out.println();

            } else {

                time = head.getTime();
                code = head.sha1Code();
                System.out.println("===");
                System.out.print("commit ");
                System.out.println(code);
                System.out.println("Date: " + time + " -0700");
                System.out.println(head.getMessage());
                System.out.println();
            }

            if (head.getParent().get(0) != null) {
                head = Utils.readObject(head.getParent().get(0), Commit.class);
            } else {
                head = null;
            }
        }
    }




    public void globalLog() {
        List<String> commitFileNames = plainFilenamesIn(Repository.COMMIT_DIR);

        String time;
        String code;

        for (String name: commitFileNames) {
            Commit head = Utils.readObject(Utils.join(Repository.COMMIT_DIR, name), Commit.class);
            time = head.getTime();
            code = head.sha1Code();
            System.out.println("===");
            System.out.print("commit ");
            System.out.println(code);
            System.out.println("Date: " + time + " -0700");
            System.out.println(head.getMessage());
            System.out.println();
        }

    }


    public void status() {
        Repository repo = Utils.readObject(REPO_FILE, Repository.class);
        Commit head = Utils.readObject(repo.headCommit, Commit.class);

        System.out.println("=== Branches ===");
        List<String> sortedFileName = new ArrayList();
        for (String branch: repo.branches.keySet()) {
            if (branch != null) {
                sortedFileName.add(branch);
            }
        }
        Collections.sort(sortedFileName);
        for (String branchName: sortedFileName) {
            if (branchName != null) {
                if (repo.crrBranch.equals(branchName)) {
                    System.out.print("*");
                }
                System.out.println(branchName);
            }
        }

//        System.out.println("other-branch");
        System.out.println();

        System.out.println("=== Staged Files ===");
        sortedFileName = new ArrayList(repo.stage.keySet());
        Collections.sort(sortedFileName);
        for (String fileName: sortedFileName) {
            System.out.println(fileName);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String removedFile: repo.removed) {
            System.out.println(removedFile);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        List<String> fileNames = plainFilenamesIn(CWD);
        for (String name: fileNames) {
            if (!repo.stage.containsKey(name) && !head.getBlobs().containsKey(name)) {
                System.out.println(name);
            }
        }
        System.out.println();
    }




    public void find(String message) {
        List<String> commitFileNames = plainFilenamesIn(Repository.COMMIT_DIR);
        boolean found = false;
        String code;
        ArrayList<String> codes = new ArrayList<String>();


        for (String name: commitFileNames) {
            Commit head = Utils.readObject(Utils.join(Repository.COMMIT_DIR, name), Commit.class);

            if (head.getMessage().equals(message)) {
                code = head.sha1Code();
                codes.add(code);
                found = true;
            }
        }
        if (found) {
//            System.out.print("(");
            for (int i = 0; i < codes.size(); i++) {
                if (i < codes.size() - 1) {
                    System.out.println(codes.get(i));
                } else {
                    System.out.print(codes.get(i));
                }
            }
//            System.out.print(")");
        }
        if (!found) {
            System.out.println("Found no commit with that message.");
        }

    }


    public static void reset(String commitId) {
        if (commitId.equals("")) {
            System.out.println("Please enter a commit id.");
        }
        Repository repo = Utils.readObject(REPO_FILE, Repository.class);
        List<String> commitFileNames = plainFilenamesIn(Repository.COMMIT_DIR);
        Commit head = null;
        String code = null;
        for (String name: commitFileNames) {
            head = Utils.readObject(Utils.join(Repository.COMMIT_DIR, name), Commit.class);
            code = head.sha1Code();
            if (code.equals(commitId)) {
                break;
            }
        }
        if (!code.equals(commitId)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit prevHead = Utils.readObject(repo.headCommit, Commit.class);
        repo.headCommit = head.getFileRef();
        repo.branches.replace(repo.crrBranch, head.getFileRef());
        repo.stage = new HashMap<>();
        repo.save(repo);
        head = Utils.readObject(repo.headCommit, Commit.class);

        for (String fileName: Utils.plainFilenamesIn(CWD)) {
            if (!repo.checkTracked(head.getFileRef(), prevHead.getFileRef(), fileName)) {
                return;
            }
        }
        for (Object blobFileName: head.getBlobs().keySet()) {
            repo.checkOutOne(head, (String) blobFileName);
        }
        File fileRef;
        String blobName;
        for (Object blobFileName: prevHead.getBlobs().keySet()) {
            blobName = (String) blobFileName;
            fileRef = Utils.join(CWD, blobName);
            if (!head.getBlobs().containsKey(blobName)) {
                if (fileRef.exists()) {
                    Utils.restrictedDelete(blobName);
                }
            }
        }
        repo.save(repo);
    }



    public static void branch(String branchName) {
        Repository repo = Utils.readObject(REPO_FILE, Repository.class);
        if (repo.getBranches().keySet().contains(branchName)) {
            System.out.println("A branch with that name already exists.");
        }
        Branch branch;
        if (repo.headCommit == null) {
            branch = new Branch(branchName, null);
        } else {
            File headRef = Utils.readObject(repo.headCommit, Commit.class).getFileRef();
            branch = new Branch(branchName, headRef);
        }

        File branchRef = Utils.join(Repository.BRANCH_DIR, branch.getName());
//        repo.crrBranch = branchName;

        repo.branches.put(branchName, branch.getCommit());
        Utils.writeObject(branchRef, branch);
        repo.save(repo);
    }



    public static void rmbranch(String name) {
        Repository repo = Utils.readObject(REPO_FILE, Repository.class);
        if (!repo.getBranches().keySet().contains(name)) {
            System.out.println("A branch with that name does not exist.");
        }
        if (repo.crrBranch.equals(name)) {
            System.out.println("Cannot remove the current branch.");
        } else {
            repo.branches.remove(name);
            repo.save(repo);
        }
    }



    public static void merge(String branchName) {
        Repository repo = Utils.readObject(REPO_FILE, Repository.class);
        if (!repo.getBranches().keySet().contains(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        Commit givenBranch = Utils.readObject(repo.branches.get(branchName), Commit.class);
        Commit currBranch = Utils.readObject(repo.branches.get(repo.crrBranch), Commit.class);
        Commit iterG = givenBranch;

        if (repo.crrBranch.equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }

        while (iterG.getParent().get(0) != null) {
            if (currBranch.sha1Code().equals(iterG.sha1Code())) {
                repo.checkOut("checkout", branchName);
                for (String fileName: givenBranch.getRemoved()) {
                    if (Utils.join(CWD, fileName).exists()) {
                        Utils.restrictedDelete(fileName);
                    }
                }
                System.out.println("Current branch fast-forwarded.");
                return;
            }
            iterG = Utils.readObject(iterG.getParent().get(0), Commit.class);
        }


        Commit splitPoint = repo.mergeGetSplitPoint(repo, givenBranch, currBranch);
        repo = Utils.readObject(REPO_FILE, Repository.class);
        repo.mergeIterSplitPoint(splitPoint, givenBranch, currBranch);


        //File in givenBranch && not in split
        repo.mergeIterGivenB(splitPoint, givenBranch, currBranch);
        repo = Utils.readObject(REPO_FILE, Repository.class);
        repo.mergeCommit(branchName, repo.crrBranch);
    }


    private static boolean checkExistence(File fileRef) {
        if (!GITLET_DIR.exists()) {
            throw new GitletException(
                    "Not in an initialized Gitlet directory.");

        }

        if (!fileRef.exists()) {
            System.out.println("File does not exist.");
            return false;
        }
        return true;
    }

    public void save(Repository repo) {
//        Utils.writeContents(REPO_FILE, repo);
        Utils.writeObject(REPO_FILE, repo);
    }


    public boolean checkTracked(File commitRef, File prevRef, String fileName) {
        Repository repo = Utils.readObject(REPO_FILE, Repository.class);
        File fileRef = Utils.join(CWD, fileName);
        Commit head = Utils.readObject(commitRef, Commit.class);
        Commit prevHead = Utils.readObject(prevRef, Commit.class);
        if (!prevHead.getBlobs().containsKey(fileName) 
            && !prevHead.getRemoved().contains(fileName)) {
            if (head.getBlobs().containsKey(fileName)) {
                System.out.println(
                    "There is an untracked file in the way;" 
                    + " delete it, or add and commit it first.");
                return false;
            }
        }
        return true;
    }


    public HashMap<String, File> getBranches() {
        return branches;
    }


    private void conflictedContent(Blob currB, Blob givenB) {
        String crrContent = currB.getContents();
        String givenContent = givenB.getContents();
        String contents = "<<<<<<< HEAD\n" + crrContent
                + "=======\n" + givenContent
                + ">>>>>>>\n";
        File localRef = Utils.join(CWD, currB.getName());
        Utils.writeContents(localRef, contents);
    }

    private void conflictedRMContent(Blob currB, Blob givenB) {
        String crrContent = currB.getContents();
        String givenContent = "";
        String contents = "<<<<<<< HEAD\n" + crrContent
                + "=======\n" + givenContent
                + ">>>>>>>\n";
        File localRef = Utils.join(CWD, currB.getName());
        Utils.writeContents(localRef, contents);
    }


    private void mergeCommit(String givenName, String currName) {

        Repository repo = Utils.readObject(REPO_FILE, Repository.class);
        Commit givenBranch = Utils.readObject(repo.branches.get(givenName), Commit.class);
        File[] parent = {repo.headCommit, givenBranch.getFileRef()};
        String message = "Merged " + givenName + " into " + repo.crrBranch + ".";
        Commit commited = new Commit(message, parent, repo.stage, repo.crrBranch, removed);
        repo.stage = new HashMap<String, File>();

        repo.headCommit = commited.getFileRef();
        repo.removed = new ArrayList<String>();
//        repo.crrBranch = repo.headCommit;
        repo.branches.put(commited.getBranch(), repo.headCommit);
        repo.save(repo);
    }


    private void mergeIterGivenB(Commit splitPoint, Commit givenBranch, Commit crrCommit) {
        Repository repo = Utils.readObject(REPO_FILE, Repository.class);
        File blobGRef;
        File fileRef;
        Blob blobG;
        Blob blobC;
        File blobCRef;
        List<String> localFiles = Utils.plainFilenamesIn(CWD);
        //in givenBranch not in splitPoint
        for (Object fileInG: givenBranch.getBlobs().keySet()) {
            blobGRef = (File) givenBranch.getBlobs().get((String) fileInG);
            blobG = Utils.readObject(blobGRef, Blob.class);
            fileRef = Utils.join(CWD, (String) fileInG);
            if (!givenBranch.getRemoved().contains((String) fileInG)) {

                if (crrCommit.getBlobs().containsKey((String) fileInG)) {
                    blobCRef = (File) crrCommit.getBlobs().get((String) fileInG);
                    blobC = Utils.readObject(blobCRef, Blob.class);
                    if (!splitPoint.getBlobs().containsKey((String) fileInG)) {
                        if (!blobC.getContents().equals(blobG.getContents())) {
                            repo.conflictedContent(blobC, blobG);
                            System.out.println("Encountered a merge conflict.");
                        }
                    }
                    repo.stage.put((String) fileInG, blobC.getFileRef());

                } else {
                    repo.stage.put((String) fileInG, fileRef);
                    Utils.writeContents(fileRef, blobG.getContents());
                }

            } else {
                if (crrCommit.getBlobs().containsKey((String) fileInG)) {
                    blobCRef = (File) crrCommit.getBlobs().get((String) fileInG);
                    blobC = Utils.readObject(blobCRef, Blob.class);
                    if (!blobC.getContents().equals(blobG.getContents())) {
                        repo.conflictedRMContent(blobC, blobG);
                        System.out.println("Encountered a merge conflict.");
                    }
                    repo.stage.put((String) fileInG, blobC.getFileRef());
                }

            }
            if (localFiles.contains((String) fileInG) 
                && !crrCommit.getBlobs().containsKey((String) fileInG)) {
//                if (!Utils.readContentsAsString(fileRef).equals(blobG.getContents())) {
                System.out.println("There is an untracked file in the way;" 
                    + " delete it, or add and commit it first.");
                // }
            }
        }
        repo.save(repo);
    }

    private Commit mergeGetSplitPoint(Repository repo, Commit givenBranch, Commit currBranch) {

        Commit parentG = givenBranch;
        Commit parentC = currBranch;
        Commit splitPoint = null;


        while (parentG.getParent().get(0) != null) {
            while (parentC.getParent().get(0) != null) {
                if (parentG.sha1Code().equals(parentC.sha1Code())) {
                    if (parentG.sha1Code().equals(givenBranch.sha1Code())) {
                        System.out.println("Given branch is an ancestor of the current branch.");
                        System.exit(0);
                    }
                    splitPoint = parentC;
                    break;
                }
                parentC = Utils.readObject(parentC.getParent().get(0), Commit.class);
            }
            if (parentG.sha1Code().equals(parentC.sha1Code())) {
                splitPoint = parentC;
                break;
            }

            parentC = currBranch;
            parentG = Utils.readObject(parentG.getParent().get(0), Commit.class);
        }


        Commit crrCommit = Utils.readObject(repo.branches.get(repo.crrBranch), Commit.class);

        if (splitPoint != null && splitPoint.sha1Code().equals(crrCommit.sha1Code())) {
            // if (repo.crrBranch.equals(splitPoint.getBranch())) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        return splitPoint;
    }














    private void mergeIterSplitPoint(Commit splitPoint, Commit givenBranch, Commit currBranch) {
        Blob blobS;
        Blob blobG;
        Blob blobC;
        File blobSRef;
        File blobCRef;
        File blobGRef;
        File fileRef;
        Repository repo = Utils.readObject(REPO_FILE, Repository.class);
        Commit crrCommit = Utils.readObject(repo.branches.get(repo.crrBranch), Commit.class);
        //exist in splitpoint
        for (Object fileInSplit: splitPoint.getBlobs().keySet()) {
            blobSRef = (File) splitPoint.getBlobs().get((String) fileInSplit);
            blobS = Utils.readObject(blobSRef, Blob.class);
            fileRef = Utils.join(CWD, (String) fileInSplit);
            if (crrCommit.getRemoved().contains((String) fileInSplit)) {
                if (Utils.join(CWD, (String) fileInSplit).exists()) {
                    Utils.restrictedDelete((String) fileInSplit);
                    repo.removed.add((String) fileInSplit);
                }
            }
            if (givenBranch.getRemoved().contains((String) fileInSplit)) {
                if (Utils.join(CWD, (String) fileInSplit).exists()) {
                    Utils.restrictedDelete((String) fileInSplit);
                    repo.removed.add((String) fileInSplit);
                }
            }
            //exist in givenBranch
            if (givenBranch.getBlobs().containsKey((String) fileInSplit)) {
                blobGRef = (File) givenBranch.getBlobs().get((String) fileInSplit);
                blobG = Utils.readObject(blobGRef, Blob.class);
                //exist in currBranch
                if (crrCommit.getBlobs().containsKey((String) fileInSplit)) {
                    blobCRef = (File) crrCommit.getBlobs().get((String) fileInSplit);
                    blobC = Utils.readObject(blobCRef, Blob.class);
                    //S == C
                    if (blobS.getContents().equals(blobC.getContents())) {
                        //S != G
                        if (!blobS.getContents().equals(blobG.getContents())) {
                            repo.stage.put(blobG.getName(), blobG.getFileRef());
                            repo.checkOutOne(givenBranch, (String) fileInSplit);
                            Utils.writeContents(fileRef, blobG.getContents());
                        //S == G
                        } else {
                            repo.stage.put((String) fileInSplit, blobG.getFileRef());
                        }
                    //S != C
                    } else {
                        //S != G
                        if (!blobS.getContents().equals(blobG.getContents())) {
                            repo.conflictedContent(blobC, blobG);
                            System.out.println("Encountered a merge conflict.");
                        }
                        repo.stage.put((String) fileInSplit, blobC.getFileRef());
                    }
                //not exist in currBranch
                } else {
                    repo.remove((String) fileInSplit);
                    Utils.restrictedDelete((String) fileInSplit);
                }
            //not exist in givenBranch
            } else {
                //exist in currBranch
                if (crrCommit.getBlobs().containsKey((String) fileInSplit)) {
                    blobCRef = (File) crrCommit.getBlobs().get((String) fileInSplit);
                    blobC = Utils.readObject(blobCRef, Blob.class);
                    //S == C
                    if (blobS.getContents().equals(blobC.getContents())) {
                        repo.remove((String) fileInSplit);
                        Utils.restrictedDelete((String) fileInSplit);
                    }
                } else {
                    repo.remove((String) fileInSplit);
                    Utils.restrictedDelete((String) fileInSplit);
                }
            }
        }
        repo.save(repo);
    }
}


