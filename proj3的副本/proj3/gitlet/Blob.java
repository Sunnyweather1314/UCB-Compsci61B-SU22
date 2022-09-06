package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable, Save {
    public Blob(String sha1, File file) {
        this._sha1 = sha1;
        this._file = file;
    }

    @Override
    public void saveContents() {
        byte[] contents = Utils.readContents(_file);
        File file = Utils.join(GitLet.getBlobs(), _sha1 + ".txt");
        Utils.writeContents(file, contents);
    }

    /**Sha1 code for the blob.*/
    private String _sha1;

    /**File path for the blob.*/
    private File _file;

}
