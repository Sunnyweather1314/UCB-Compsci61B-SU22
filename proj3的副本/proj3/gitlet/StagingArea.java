package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;

public class StagingArea implements Save, Serializable {
    public StagingArea() {
        _addedFiles = new TreeMap<>();
        _removedFiles = new TreeMap<>();
    }

    public StagingArea(StagingArea other) {
        _addedFiles = new TreeMap<>(other._addedFiles);
        _removedFiles = new TreeMap<>(other._removedFiles);
    }

    public TreeMap<String, String> getAddedFiles() {
        return _addedFiles;
    }

    public void addFile(String file, String sha1) {
        _addedFiles.put(file, sha1);
    }

    public boolean containAddedFiles(String file, String sha1) {
        return containAddedFiles(file) && _addedFiles.get(file).equals(sha1);
    }

    public boolean containAddedFiles(String file) {
        return _addedFiles.containsKey(file);
    }

    public void removeAddedFile(String file, String sha1) {
        _addedFiles.remove(file, sha1);
    }

    public void removeAddedFile(String file) {
        _addedFiles.remove(file);
    }

    public boolean containRemovedFiles(String file, String sha1) {
        return _removedFiles.containsKey(file)
                && _removedFiles.get(file).equals(sha1);
    }

    public boolean containRemovedFiles(String file) {
        return _removedFiles.containsKey(file);
    }

    public TreeMap<String, String> getRemovedFiles() {
        return _removedFiles;
    }

    public void removeFile(String file) {
        String code = _addedFiles.remove(file);
        _removedFiles.put(file, code);
    }

    public void removeRemovedFile(String file) {
        _removedFiles.remove(file);
    }
    @Override
    public void saveContents() {
        File file = GitLet.getStagingArea();
        Utils.writeObject(file, this);
    }

    /**The added files in the Staging Area.*/
    private TreeMap<String, String> _addedFiles;

    /**The removed files in the Staging Area.*/
    private TreeMap<String, String> _removedFiles;
}
