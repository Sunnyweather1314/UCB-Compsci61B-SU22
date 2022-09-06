package gitlet;
// import java.io.File;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.fail;


/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Jiayi Xu
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        Repository repo = new Repository();
        if (Repository.REPO_FILE.exists()) {
            repo = Utils.readObject(Repository.REPO_FILE, Repository.class);
        }
        String firstArg = args[0];
        if (!firstArg.equals("init") && !Repository.GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        switch (firstArg) {
            case "init":
                validateNumArgs(firstArg, args, 1);
                repo.init();
                break;
            case "add":
                validateNumArgs(firstArg, args, 2);
                repo.add(args[1]);
                break;
            case "commit":
                validateNumArgs(firstArg, args, 2);
                String message = args[1];
                if (message.equals("")) {
                    System.out.println("Please enter a commit message.");
                }
                repo.commit(message);
                break;
            case "rm":
                validateNumArgs(firstArg, args, 2);
                String fileName = args[1];
                if (fileName.equals("")) {
                    System.out.println("Please enter a file name.");
                }
                repo.remove(fileName);
                break;

            case "checkout":
                repo.checkOut(args);
                break;
            case "log":
                repo.log();
                break;
            case "global-log":
                repo.globalLog();
                break;
            case "status":
                repo.status();
                break;
            case "find":
                validateNumArgs(firstArg, args, 2);
                if (args[1].equals("")) {
                    System.out.println("Please enter a commit message.");
                }
                repo.find(args[1]);
                break;
            case "reset":
                validateNumArgs(firstArg, args, 2);
                repo.reset(args[1]);
                break;
            case "branch":
                validateNumArgs(firstArg, args, 2);
                repo.branch(args[1]);
                break;
            case "rm-branch":
                validateNumArgs(firstArg, args, 2);
                repo.rmbranch(args[1]);
                break;
            case "merge":
                validateNumArgs(firstArg, args, 2);
                repo.merge(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }


    private static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            System.out.println("Invalid number of arguments.");
        }
    //     switch (args.length) {
    //         case 2:
    //             return;
    //         case 3:
    //             errorMessage(2);
    //         default:
    //             return;
    //     }
    }
}


