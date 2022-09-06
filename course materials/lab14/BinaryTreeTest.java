import org.junit.Test;
import static org.junit.Assert.*;

import java.beans.Transient;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BinaryTreeTest {


    @Test
    public void containTest(){
        BinarySearchTree<String> t1 = new BinarySearchTree<String>();
        t1.add("C");
        t1.add("D");
        t1.add("E");
        t1.add("C");
        t1.add("A");
        t1.add("E");
        t1.add("B");
        t1.add("D");
        assertTrue(t1.contains("C"));
        assertTrue(t1.contains("E"));
        assertTrue(t1.contains("A"));
        assertTrue(t1.contains("D"));
        assertFalse(t1.contains("K"));
    }




    @Test
    public void treeFormatTest() {
        BinarySearchTree<String> x = new BinarySearchTree<String>();
        x.add("C");
        x.add("A");
        x.add("E");
        x.add("B");
        x.add("D");
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(outContent));
        BinaryTree.print(x, "x");
        System.setOut(oldOut);
        assertEquals("x in preorder\nC A B E D \nx in inorder\nA B C D E \n\n".trim(),
                     outContent.toString().trim());
    }
}