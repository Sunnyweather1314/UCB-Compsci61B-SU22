import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Tests {

    @Test
    public void heightTest() {
        BinaryTree t;
        t = new BinaryTree();
        t.root = new BinaryTree.TreeNode("a",
                new BinaryTree.TreeNode("bc", new BinaryTree.TreeNode("d", new BinaryTree.TreeNode("e"),
                        new BinaryTree.TreeNode("f")), null), new BinaryTree.TreeNode("c"));
        assertEquals(t.height(), 4);

    }

    @Test
    public void longestNameTest(){

    }


}