import org.junit.Test;
import static org.junit.Assert.*;

public class MinHeapPQTest {



    @Test
    public void basicinserttest() {
        MinHeapPQ<Integer> k = new MinHeapPQ();
        for (int i  = 0; i <7; i++) {
            k.insert(i, i);
        }

        for (int i  = 0; i < 7; i++) {
            assertTrue(k.contains(i));
        }
    }


    @Test
    public void bubbleUpTest() {
        MinHeapPQ<Integer> k = new MinHeapPQ();
        for (int i  = 6; i >0; i--) {
            k.insert(i, i);
        }

        for (int i  = 1; i < 7; i++) {
            assertTrue(k.contains(i));
        }
    }


    @Test
    public void removeMIn() {
        MinHeapPQ<Integer> k = new MinHeapPQ();
        for (int i  = 6; i >=0; i--) {
            k.insert(i, i);
        }
        assertEquals((int)k.poll(), 0);
        assertFalse(k.contains(0));

        k.insert(10,10);
        k.insert(9,9);
//        k.insert(103,103);
        k.insert(34, 34);
//        k.insert(35, 35);
        assertEquals((int)k.poll(), 1);
        assertEquals((int)k.poll(), 2);
        assertEquals((int)k.poll(), 3);
        assertEquals((int)k.poll(), 4);
        assertEquals((int)k.poll(), 5);
        assertEquals((int)k.poll(), 6);
        assertEquals((int)k.poll(), 9);
        assertEquals((int)k.poll(), 10);
        assertEquals((int)k.poll(), 34);
    }


    @Test
    public void testUpdate() {
        MinHeapPQ<Integer> k = new MinHeapPQ();
        for (int i  = 6; i >=0; i--) {
            k.insert(i, i);
        }
        k.changePriority(0,6);
        assertTrue(k.contains(0));
        assertEquals((int)k.poll(), 1);
        k.changePriority(5,3);
        int a = k.poll();
        a = k.poll();
        k.changePriority(0,1);
        assertEquals((int)k.poll(), 0);
        assertEquals((int)k.poll(), 3);
        assertEquals((int)k.poll(), 4);
        assertEquals((int)k.poll(), 6);
        assertFalse(k.contains(1));
    }

    @Test
    public void testTest() {
        MinHeapPQ<Integer> k = new MinHeapPQ<>();
        for (int i  = 10; i >=0; i--) {
            k.insert(i, i);
        }

        k.changePriority(9,3);
        k.changePriority(5,1);
        k.changePriority(1,13);
        assertEquals((int)k.poll(), 0);
        assertEquals((int)k.poll(), 5);
        assertEquals((int)k.poll(), 2);
        assertEquals((int)k.poll(), 9);
        assertEquals((int)k.poll(),3);

    }

}
