package gitlet;


/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author David Yin
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        try {
            if (args.length == 0) {
                failure(1);
                System.exit(0);
            }
            String cmd = args[0];
            GitLet repository = new GitLet();
            switch (cmd) {
            case "init" -> {
                validateNumArgs(cmd, args, 1);
                repository.init(args);
            }
            case "add" -> {
                validateNumArgs(cmd, args, 2);
                repository.add(args);
            }
            case "commit" -> {
                validateNumArgs(cmd, args, 2);
                repository.commit(args);
            }
            case "rm" -> {
                validateNumArgs(cmd, args, 2);
                repository.remove(args);
            }
            case "checkout" -> {
                validateNumArgs(cmd, args, 4);
                repository.checkOut(args);
            }
            case "log" -> {
                validateNumArgs(cmd, args, 1);
                repository.log(args);
            }
            case "global-log" -> {
                validateNumArgs(cmd, args, 1);
                repository.globalLog(args);
            }
            case "find" -> {
                validateNumArgs(cmd, args, 2);
                repository.find(args);
            }
            case "status" -> {
                validateNumArgs(cmd, args, 1);
                repository.status(args);
            }
            default -> mainHelper(repository, args);
            }
        } catch (GitletException e) {
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }


    private static void mainHelper(GitLet repository, String... args) {
        String cmd = args[0];
        switch (cmd) {
        case "branch" -> {
            validateNumArgs(cmd, args, 2);
            repository.branch(args);
        }
        case "rm-branch" -> {
            validateNumArgs(cmd, args, 2);
            repository.removeBranch(args);
        }
        case "reset" -> {
            validateNumArgs(cmd, args, 2);
            repository.reset(args);
        }
        case "merge" -> {
            validateNumArgs(cmd, args, 2);
            repository.merge(args);
        }
        case "add-remote" -> {
            validateNumArgs(cmd, args, 3);
            repository.addRemote(args);
        }
        case "rm-remote" -> {
            validateNumArgs(cmd, args, 2);
            repository.removeRemote(args);
        }
        case "push" -> {
            validateNumArgs(cmd, args, 3);
            repository.push(args);
        }
        case "fetch" -> {
            validateNumArgs(cmd, args, 3);
            repository.fetch(args);
        }
        case "pull" -> {
            validateNumArgs(cmd, args, 3);
            repository.pull(args);
        }
        default -> failure(2);
        }
    }

    private static void failure(int key) {
        switch (key) {
        case 1 -> throw new GitletException("Please enter a command");
        case 2 -> throw new GitletException(
                "No command with that name exists.");
        default -> throw new GitletException("Incorrect operands.");
        }
    }

    private static void validateNumArgs(String cmd, String[] args, int n) {
        if (cmd.equals("checkout")) {
            switch (args.length) {
            case 2 -> {
                return;
            }
            case 3 -> {
                if (!args[1].equals("--")) {
                    failure(3);
                }
            }
            case 4 -> {
                if (!args[2].equals("--")) {
                    failure(3);
                }throw new gitlet.GitletException("No command with that name exists");

            }
            default -> failure(3);
            }
        } else if (args.length != n) {
            failure(3);
        }
    }
}
