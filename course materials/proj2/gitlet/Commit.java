package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.HashMap;
import java.util.Arrays;


/** Represents a gitlet commit object.
 *  @author Jiayi Xu
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */


    /** The message of this Commit. */
    private String message;
    /** The parent commit */
    private ArrayList<File> parent;
    /** blobs being commited */
    private HashMap<String, File> blobs;
    private String time;
    private String sha1Code;
    private File fileRef;
    private String branch;
    private ArrayList<String> removed;

    
    public Commit(String message, File[] parent, 
                HashMap stage, String branch, ArrayList<String> removed) {
        this.message = message;
        this.parent = new ArrayList<File>(Arrays.asList(parent));

        Date timer = new Date(System.currentTimeMillis());
        SimpleDateFormat timeStamp = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
        timeStamp.setTimeZone(TimeZone.getTimeZone("PST"));
        this.time = timeStamp.format(timer);
        this.blobs = (HashMap) stage.clone();
        this.branch = branch;

        if (!(this.parent.get(0) == null)) {
            Commit decodeParent = Utils.readObject(this.parent.get(0), Commit.class);
            for (Object fileName: decodeParent.getBlobs().keySet()) {
                if (!blobs.keySet().contains(fileName)) {
                    blobs.put((String) fileName, (File) decodeParent.getBlobs().get(fileName));
                }
            }
        }
        this.sha1Code = Utils.sha1(Utils.serialize(this));
        this.removed = removed;
        this.saveCommit();
    }


    public void saveCommit() {
        String sha1 = this.sha1Code();
        File commit = Utils.join(Repository.COMMIT_DIR, sha1 + ".txt");
        this.fileRef = commit;
        Utils.writeObject(commit, this);
    }

    public HashMap getBlobs() {
        return this.blobs;
    }

    public String sha1Code() {
        return this.sha1Code;
    }

    public File getFileRef() {
        return this.fileRef;
    }

    public ArrayList<File> getParent() {
        return this.parent;
    }

    public String getTime() {
        return this.time;
    }

    public String getMessage() {
        return this.message;
    }

    public String getBranch() {
        return this.branch;
    }

    public ArrayList<String> getRemoved() {
        return this.removed;
    }
}


