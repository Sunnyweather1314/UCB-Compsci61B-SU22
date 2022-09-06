import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Stack;
import java.util.HashSet;
import java.util.Collections;

public class Graph implements Iterable<Integer> {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;

    /* Initializes a graph with NUMVERTICES vertices and no Edges. */
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }

        vertexCount = numVertices;
    }

    /* Adds a directed Edge (V1, V2) to the graph. That is, adds an edge
       in ONE directions, from v1 to v2. */
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, 0);
    }

    /* Adds an undirected Edge (V1, V2) to the graph. That is, adds an edge
       in BOTH directions, from v1 to v2 and from v2 to v1. */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, 0);
    }

    /* Adds a directed Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        Edge added = new Edge(v1, v2, weight);

        if (neighbors(v1).contains(v2)) {
            int indexV2 = neighbors(v1).indexOf(v2);
            adjLists[v1].set(indexV2, added);
        } else {
            adjLists[v1].add(added);
        }
    }

    /* Adds an undirected Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int weight) {
        // TODO: YOUR CODE HERE
        addEdge(v1, v2, weight);
        addEdge(v2, v1, weight);
    }

    /* Returns true if there exists an Edge from vertex FROM to vertex TO.
       Returns false otherwise. */
    public boolean isAdjacent(int from, int to) {
        LinkedList<Edge> startNode = adjLists[from];
        for(Edge edge: adjLists[from]) {
            if (edge.to() == to) {
                return true;
            }   
        }
        return false;

    }

    /* Returns a list of all the vertices u such that the Edge (V, u)
       exists in the graph. */
    public List<Integer> neighbors(int v) {
        List<Integer> neighbor = new ArrayList<Integer>();
        for(Edge edge: adjLists[v]) {
            neighbor.add(edge.to());
        }
        return neighbor;
    }
    /* Returns the number of incoming Edges for vertex V. */
    public int inDegree(int v) {
        // TODO: YOUR CODE HERE
        int count = 0;
        for (int i = 0; i < vertexCount; i++ ){
            if (i != v && isAdjacent(i,v)) {
                count++;
            }
        }
        return count;
    }

    /* Returns an Iterator that outputs the vertices of the graph in topological
       sorted order. */
    public Iterator<Integer> iterator() {
        return new TopologicalIterator();
    }

    /**
     *  A class that iterates through the vertices of this graph,
     *  starting with a given vertex. Does not necessarily iterate
     *  through all vertices in the graph: if the iteration starts
     *  at a vertex v, and there is no path from v to a vertex w,
     *  then the iteration will not include w.
     */
    private class DFSIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;


        public DFSIterator(Integer start) {
            fringe = new Stack<>();
            visited = new HashSet<>();
            fringe.push(start);
        }

        public boolean hasNext() {
            if (!fringe.isEmpty()) {
                int i = fringe.pop();
                while (visited.contains(i)) {
                    if (fringe.isEmpty()) {
                        return false;
                    }
                    i = fringe.pop();
                }
                fringe.push(i);
                return true;
            }
            return false;
        }

        public Integer next() {
            int curr = fringe.pop();
            ArrayList<Integer> lst = new ArrayList<>();
            for (int i : neighbors(curr)) {
                lst.add(i);
            }
            lst.sort((Integer i1, Integer i2) -> -(i1 - i2));
            for (Integer e : lst) {
                if (!visited.contains(e)){
                    fringe.push(e);
                }


            }
            visited.add(curr);
            return curr;
        }

        //ignore this method
        public void remove() {
            throw new UnsupportedOperationException(
                    "vertex removal not implemented");
        }

    }

    /* Returns the collected result of performing a depth-first search on this
       graph's vertices starting from V. */
    public List<Integer> dfs(int v) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(v);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /* Returns true iff there exists a path from START to STOP. Assumes both
       START and STOP are in this graph. If START == STOP, returns true. */
    public boolean pathExists(int start, int stop) {
        // TODO: YOUR CODE HERE
        if (start == stop) {
            return true;
        } else {
            return (dfs(start).contains(stop));
            
        }
    }


    /* Returns the path from START to STOP. If no path exists, returns an empty
       List. If START == STOP, returns a List with START. */
    public List<Integer> path(int start, int stop) {
        // TODO: YOUR CODE HERE
        ArrayList path = new ArrayList<>();
        if (!dfs(start).contains(stop)) {
            return path;
        } else {
            List<Integer> DFS = dfs(start);
            Collections.reverse(DFS);
            int indexOfStop = DFS.indexOf(stop);
            
            path.add(0,stop);
            ArrayList visited = new ArrayList<>();
            int des = stop;
            System.out.println(DFS);
            for (Integer k: DFS) {
                if (DFS.indexOf(k)>indexOfStop) {
                    if (des != start) {
                        if (!visited.contains(k) && neighbors(k).contains(des)) {
                            des = k;
                            visited.add(k);
                            path.add(0,des);
                        } else {
                            visited.add(k);
    
                        }
                    } else {
                        path.add(0, des);
                    }
                }
            }

            return path;
        // return pathbuilder(start, stop, new ArrayList<>());
        }
    }   



    private List<Integer> pathbuilder(int start, int stop, List<Integer> visited){
        // if (!dfs(start).contains(stop)) {
        //     return new ArrayList<>(new ArrayList<>());
        // } else if (start == stop){
        //     return new ArrayList<>(new ArrayList<>(stop));
        // } else {
        //     List<List<Integer>> result = new ArrayList<List<Integer>>();
        //     for (Integer node: neighbors(start)) {
        //         if (!visited.contains(node)) {
        //             visited.add(node);
        //             List<List<Integer>> add = pathbuilder(node, stop, visited);

        //             for (List<Integer> path: add) {
        //                 if (path.contains(stop)) {
        //                     ArrayList<Integer> in = new ArrayList<>(start);
        //                     in.addAll(path);
        //                     result.add(in);
        //                 }

        //             }

        //         }
        //     }
        //     return result;
        // }
        if (!dfs(start).contains(stop)) {
            ArrayList b = new ArrayList();
            return b;

        } else if (start == stop) {
            ArrayList a = new ArrayList(stop);
            return a;
        } else {
            ArrayList result = null;
            for (Integer node: neighbors(start)) {
                if (!visited.contains(node)) {
                    visited.add(node);
                    List<Integer> add = pathbuilder(node, stop, visited);
                    if (!add.isEmpty()) {
                        result = new ArrayList(start);
                        result.addAll(add);
                        break;
                    }
                }
            }
            return result;
        }

    }

    public List<Integer> topologicalSort() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();


        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;
        private ArrayList<Integer> indegrees;
        // TODO: Instance variables here!

        TopologicalIterator() {
            fringe = new Stack<Integer>();

            visited = new HashSet<>();
            indegrees = new ArrayList<Integer>();


            for (int k = 0; k < vertexCount; k++) {
                indegrees.add(k, inDegree(k));
            }

            fringe.push(indegrees.indexOf(0));
            // TODO: YOUR CODE HERE
        }

        public boolean hasNext() {
            if (!fringe.isEmpty()) {
                int i = fringe.pop();
                while (visited.contains(i)) {
                    if (fringe.isEmpty()) {
                        return false;
                    }
                    i = fringe.pop();
                }
                fringe.push(i);
                return true;
            }
            return false;
        }

        public Integer next() {
            // TODO: YOUR CODE HERE
            int curr = fringe.pop();

            for (int i : neighbors(curr)) {
                indegrees.set(i, indegrees.get(i)-1);
                if (!visited.contains(i) && indegrees.get(i) == 0){
                    fringe.push(i);
                }

            }
            visited.add(curr);
            return curr;
            
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public String toString() {
            return "(" + from + ", " + to + ", weight = " + weight + ")";
        }

        public int weight(){return weight;}
        public int to(){return to;}
        public int from(){return from;}
        
        public boolean equals(Edge other) {
            return other.from() == this.from && this.to == other.to();

        }
    }

    private void generateG1() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG2() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG3() {
        addUndirectedEdge(0, 2);
        addUndirectedEdge(0, 3);
        addUndirectedEdge(1, 4);
        addUndirectedEdge(1, 5);
        addUndirectedEdge(2, 3);
        addUndirectedEdge(2, 6);
        addUndirectedEdge(4, 5);
    }

    private void generateG4() {
        addEdge(0, 1);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 2);
    }

    private void printDFS(int start) {
        System.out.println("DFS traversal starting at " + start);
        List<Integer> result = dfs(start);
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printPath(int start, int end) {
        System.out.println("Path from " + start + " to " + end);
        List<Integer> result = path(start, end);
        if (result.size() == 0) {
            System.out.println("No path from " + start + " to " + end);
            return;
        }
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printTopologicalSort() {
        System.out.println("Topological sort");
        List<Integer> result = topologicalSort();
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }

    public static void main(String[] args) {
        Graph g1 = new Graph(5);
        g1.generateG1();
        g1.printDFS(0);
        g1.printDFS(2);
        g1.printDFS(3);
        g1.printDFS(4);

        g1.printPath(0, 3);
        g1.printPath(0, 4);
        g1.printPath(1, 3);
        g1.printPath(1, 4);
        g1.printPath(4, 0);

        Graph g2 = new Graph(5);
        g2.generateG2();
        g2.printTopologicalSort();
    }
}

