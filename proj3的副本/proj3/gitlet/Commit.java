package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.TreeMap;

public class Commit implements Serializable, Sha1, Save {

    public Commit(String message, String firstParent,
                  TreeMap<String, String> blobs) {
        this._message = message;
        this._firstParent = firstParent;
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("PST"));
        this._date = sdf.format(date);
        this._blobs = blobs;
    }

    public Commit(String message, String firstParent,
                  String secondParent, TreeMap<String, String> blobs) {
        this._message = message;
        this._firstParent = firstParent;
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("PST"));
        this._date = sdf.format(date);
        this._blobs = blobs;
        _secondParent = secondParent;
    }

    public String getFirstParent() {
        return _firstParent;
    }

    public String getSecondParent() {
        return _secondParent;
    }

    public String getMessage() {
        return _message;
    }

    public TreeMap<String, String> getBlobs() {
        return _blobs;
    }

    public String getDate() {
        return _date;
    }

    public boolean containFile(String name) {
        return _blobs.containsKey(name);
    }

    public boolean containFile(String name, String sHA1) {
        return _blobs.containsKey(name) && _blobs.get(name).equals(sHA1);
    }

    public String getBlob(String name) {
        return _blobs.get(name);
    }

    @Override
    public void saveContents() {
        String sha1 = getCode();
        File commit = Utils.join(GitLet.getCommits(), sha1 + ".txt");
        Utils.writeObject(commit, this);
    }

    @Override
    public String getCode() {
        return Utils.sha1(Utils.serialize(this));
    }

    @Override
    public String toString() {
        return "===\n" + "commit " + getCode() + "\n"
                + "Date: " + _date + " -8000\n" + _message;
    }
    /**The message for this commit.*/
    private String _message;

    /**The first parent of this commit.*/
    private String _firstParent;

    /**The second parent of this commit (only for merge).*/
    private String _secondParent;

    /**Date.*/
    private String _date;

    /**Contents of files.*/
    private TreeMap<String, String> _blobs;

}
