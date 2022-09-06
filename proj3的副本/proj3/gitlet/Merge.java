package gitlet;

import java.util.TreeMap;

public class Merge extends Commit {

    public Merge(String message, String firstParent,
                 String secondParent, TreeMap<String, String> blobs) {
        super(message, firstParent, secondParent, blobs);
    }

    @Override
    public String toString() {
        return "===\n" + "commit " + getCode()
                + "\n" + "Merge: " + getFirstParent().substring(0, 7)
                + " " + getSecondParent().substring(0, 7) + "\n"
                + "Date: " + getDate() + " -8000\n" + getMessage();
    }

}
