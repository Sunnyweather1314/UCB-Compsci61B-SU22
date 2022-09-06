package gitlet;

import java.io.File;
import java.io.Serializable;

public class Branch implements Serializable {

    /**The name of the branch.*/
    private final String name;
    /**The latest commit this branch heads to.*/
    private File commit;


    public Branch(String name, File commit) {
        this.name = name;
        this.commit = commit;
    }

    public String getName() {
        return name;
    }

    public File getCommit() {
        return commit;
    }

    public void setCommit(File commit) {
        this.commit = commit;
        saveBranch();
    }

    public void saveBranch() {
        File branch = Utils.join(Repository.BRANCH_DIR, name + ".txt");
        Utils.writeObject(branch, this);
    }
}

