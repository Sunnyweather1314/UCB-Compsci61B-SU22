import java.util.Arrays;


public class UnionFind {

    /* TODO: Add instance variables here. */

    int[] sets;
    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */

    public static void main(String[] args) {

    }
    public UnionFind(int N) {
        // TODO: YOUR CODE HERE
        this.sets = new int[N];
        for (int i = 0; i < N; i++) {
            sets[i] = -1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        // TODO: YOUR CODE HERE
        return Math.abs(sets[find(v)]);
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        // TODO: YOUR CODE HERE

        return sets[v];



    }

    private int findIndex(int i) {
        for (int k = 0; k < sets.length; k++) {
            if (sets[k] == i) {
                return k;
            }
        }
        return -1;
    }


    /* Returns true if nodes V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        // TODO: YOUR CODE HERE
        // if (parent(find(v1)< 0 && parent(find())))
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        // TODO: YOUR CODE HERE
        if(v > sets.length) {
            throw new IllegalArgumentException();
        }

        if (sets[v] < 0) {
            return v;
        }

        int parent = parent(v);
        while (parent >= 0) {
            if(sets[parent] < 0) {
                break;
            }
            parent = parent(parent);

        }
        // int index = findIndex(parent);
        // int index = Arrays.asList(sets).indexOf(parent);
        sets[v] = parent;
        return parent;
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing a item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        // TODO: YOUR CODE HERE
        if(find(v1) == find(v2)) {
            return;
        }
        if (sizeOf(v1) > sizeOf(v2)) {
            int parent1 = find(v1);
            int parent2 = find(v2);
            sets[parent1] += sets[parent2];
            sets[parent2] = parent1;
        } else {
            int parent1 = find(v1);
            int parent2 = find(v2);
            sets[parent2] += sets[parent1];
            sets[parent1] = parent2;
        }
    }
}
