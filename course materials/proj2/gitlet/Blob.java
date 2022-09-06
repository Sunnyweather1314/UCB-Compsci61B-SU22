package gitlet;

import java.io.File;
import java.io.Serializable;


/**creates a blob which consists of sha1code, file 
 *address, and the contents of the file when it was added*/
public class Blob implements Serializable {

    private String sha1Code;
    private File fileRef;
    private String contents;
    private String name;


    public Blob(File file, String name) {

        //System.out.println("Conetents here: " + Utils.readContentsAsString(file));
        this.contents = Utils.readContentsAsString(file);
        //System.out.println(contents);
        this.sha1Code = Utils.sha1(Utils.serialize(this));
        this.fileRef = Utils.join(Repository.BLOB_DIR, this.sha1Code + ".txt");
        this.name = name;

    }


    public void save() { 
        Utils.writeObject(fileRef, this);
    }

    public String getSha1Code() {
        return this.sha1Code;
    }

    public String getName() {
        return this.name;
    }

    public String getContents() {
        return this.contents;
    }

    public File getFileRef() {
        return this.fileRef;
    }
}


