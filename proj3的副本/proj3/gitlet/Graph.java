package gitlet;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;
import java.util.TreeMap;

public class Graph {

    public static ArrayList<Commit> findFutureCommits(
            String localRemoteCommitId, String headCommitId) {

        Stack<String> path = new Stack<>();
        initializeGraph(null, headCommitId, false);
        boolean flag = isReachable(localRemoteCommitId, headCommitId, path);
        if (!flag) {
            throw new GitletException("Please "
                    + "pull down remote changes before pushing.");
        }
        ArrayList<Commit> output = new ArrayList<>();
        while (!path.isEmpty()) {
            String curr = path.pop();
            Commit commit = Utils.readObject(Utils.join(
                    GitLet.getCommits(), curr + ".txt"), Commit.class);
            output.add(commit);
        }
        return output;
    }

    private static boolean isReachable(
            String dest, String start, Stack<String> path) {
        Node curr = _map.remove(start);
        curr._marked = true;
        _map.put(start, curr);
        if (start.equals(dest)) {
            return true;
        }
        path.push(start);

        for (String parentId: _map.get(start)._parents) {
            if (parentId != null) {
                Node parent = _map.get(parentId);
                if (!parent._marked) {
                    if (isReachable(dest, parentId, path)) {
                        return true;
                    }
                }
            }
        }

        path.pop();
        return false;
    }


    public static String findLatestCommonAncestor(
            String currBranch, String mergedBranch) {
        TreeMap<String, Node> result = dijkstraAlgorithm(
                currBranch, mergedBranch);
        Node back = result.get(mergedBranch);
        Commit parent = Utils.readObject(Utils.join(GitLet.
                getCommits(), mergedBranch + ".txt"), Commit.class);
        String firstParent = parent.getCode();
        String secondParent = parent.getCode();

        while (true) {
            if ((back._name.equals(firstParent)
                    || back._name.equals(secondParent))) {
                parent = Utils.readObject(Utils.join(GitLet.
                        getCommits(), back._name + ".txt"), Commit.class);
                if (back._back != null) {
                    back = result.get(back._back);
                }
                firstParent = parent.getFirstParent();
                secondParent = parent.getSecondParent();

            } else {
                return parent.getCode();
            }
        }
    }

    private static TreeMap<String, Node> dijkstraAlgorithm(
            String curr, String merge) {
        initializeGraph(curr, merge, true);
        TreeMap<String, Node> fringe = new TreeMap<>(_map);

        while (!fringe.isEmpty()) {
            ArrayList<Node> values = new ArrayList<>(fringe.values());
            values.sort(Comparator.comparingInt(o -> o._dis));
            Node first = fringe.remove(values.get(0)._name);
            for (String code: first._neighbors) {
                if (code != null && fringe.containsKey(code)) {
                    Node node = fringe.get(code);
                    if (first._dis + 1 < node._dis) {
                        Node rm = fringe.remove(code);
                        rm._dis = first._dis + 1;
                        rm._back = first._name;
                        fringe.put(rm._name, rm);
                    }
                }
            }
        }
        return _map;
    }

    private static void initializeGraph(
            String curr, String merge, boolean flag) {
        _map = new TreeMap<>();
        constructAncestorTree(curr, null, flag);
        constructAncestorTree(merge, null, flag);
        if (curr != null) {
            Node startNode = _map.remove(curr);
            startNode._dis = 0;
            _map.put(curr, startNode);
        }
    }


    private static void constructAncestorTree(
            String start, String child, boolean flag) {
        if (child != null && flag) {
            Node childNode = _map.remove(child);
            childNode._neighbors.add(start);
            _map.put(child, childNode);
        }
        if (start == null) {
            return;
        }
        Node newNode = new Node(start, Integer.MAX_VALUE);
        Commit curr = Utils.readObject(Utils.join
                (GitLet.getCommits(), start + ".txt"), Commit.class);
        String firstParent = curr.getFirstParent();
        String secondParent = curr.getSecondParent();
        if (flag) {
            newNode._neighbors.add(child);
        } else {
            if (firstParent != null) {
                newNode._parents.add(firstParent);
            }
            if (secondParent != null) {
                newNode._parents.add(secondParent);
            }
        }
        _map.put(start, newNode);
        constructAncestorTree(firstParent, start, flag);
        constructAncestorTree(secondParent, start, flag);
    }


    private static class Node {

        Node(String sha1, int dis) {
            _name = sha1;
            _dis = dis;
            _marked = false;
        }

        /**The name of the node.*/
        private String _name;
        /**The back node of that node.*/
        private String _back;
        /**The distance of the node to the starting node.*/
        private int _dis;
        /**The neighbors of the node.*/
        private ArrayList<String> _neighbors = new ArrayList<>();
        /**The parents of the node.*/
        private ArrayList<String> _parents = new ArrayList<>();
        /**The status of that node.*/
        private boolean _marked;
    }

    /**The map for the graph.*/
    private static TreeMap<String, Node> _map;
}
