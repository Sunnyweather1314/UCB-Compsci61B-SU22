import java.util.ArrayList;

public class BinaryTree<T> {

    protected TreeNode root;

    public BinaryTree() {
        root = null;
    }

    public BinaryTree(TreeNode t) {
        root = t;
    }

    /* Print the values in the tree in preorder. */
    public void printPreorder() {
        if (root == null) {
            System.out.println("(empty tree)");
        } else {
            root.printPreorder();
            System.out.println();
        }
    }

    /* Print the values in the tree in inorder. */
    public void printInorder() {
        // TODO: YOUR CODE HERE
        if (root == null) {
            System.out.println("(empty tree)");
        } else {
            root.printInorder();
            System.out.println();
        }
    }

    /* Prints the BinaryTree in preorder or in inorder. Used for your testing. */
    protected static void print(BinaryTree t, String description) {
        System.out.println(description + " in preorder");
        t.printPreorder();
        System.out.println(description + " in inorder");
        t.printInorder();
        System.out.println();
    }

    protected class TreeNode {

        T item;
        TreeNode left;
        TreeNode right;
        int size = 0;

        public TreeNode(T item) {
            this.item = item; left = right = null;
        }

        public TreeNode(T item, TreeNode left, TreeNode right) {
            this.item = item;
            this.left = left;
            this.right = right;
        }

        /* Prints the nodes of the BinaryTree in preorder. Used for your testing. */
        private void printPreorder() {
            System.out.print(item + " ");
            if (left != null) {
                left.printPreorder();
            }
            if (right != null) {
                right.printPreorder();
            }
        }

        /* Prints the nodes of the BinaryTree in inorder. Used for your testing. */
        private void printInorder() {
            // TODO: YOUR CODE HER
            if (left != null) {
                left.printInorder();
            }
            System.out.print(item + " ");
            if (right != null) {
                right.printInorder();
            }
        }

        public Boolean contains(T key){
            if(((Comparable<T>) item).compareTo(key) == 0){
                return true;
            }else if(((Comparable<T>) item).compareTo(key) > 0 && left != null){
                return left.contains(key);
            }else if(((Comparable<T>) item).compareTo(key) < 0 && right != null){
                return right.contains(key);
            }else{
                return false;
            }
        }


        public void addHelper(T key){
            if(((Comparable<T>) item).compareTo(key) < 0){
                if(right == null){
                    right = new TreeNode(key);
                }else{
                    right.addHelper(key);
                }
                // if(((Comparable<T>) right.item).compareTo(key) < 0){
            }else{

                if(left == null){
                    left = new TreeNode(key);
                }else{
                    left.addHelper(key);
                }
            }
        }


    }//TreeNode class end

}