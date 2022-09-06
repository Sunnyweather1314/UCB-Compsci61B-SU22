package gitlet;

import java.io.File;
import java.io.Serializable;

public class Head implements Save, Serializable {

    public Head(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }
    @Override
    public void saveContents() {
        File path = GitLet.getHEAD();
        Utils.writeObject(path, this);
    }

    /**The name of the head.*/
    private String _name;
}
