package gitlet;

import java.io.File;
import java.io.Serializable;

public class Branch implements Save, Serializable {

    public Branch(String name, String commit) {
        this._name = name;
        this._commit = commit;
    }

    public String getName() {
        return _name;
    }

    public String getCommit() {
        return _commit;
    }

    @Override
    public void saveContents() {
        File branch = Utils.join(GitLet.getBranches(),
                Utils.sha1(_name) + ".txt");
        Utils.writeObject(branch, this);

    }

    public void update(String sha1) {
        _commit = sha1; }

    /**The name of the branch.*/
    private String _name;

    /**The latest commit this branch heads to.*/
    private String _commit;
}
