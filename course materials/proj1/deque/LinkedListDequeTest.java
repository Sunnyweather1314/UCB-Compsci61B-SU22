package deque;
import deque.Node;
import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list deque tests. */
public class LinkedListDequeTest {

    /** You MUST use the variable below for all of your tests. If you test
     *      * using a local variable, and not this static variable below, the
     *      * autograder will not grade that test. If you would like to test
     *      * LinkedListDeques with types other than Integer (and you should),
     * you can define a new local variable. However, the autograder will
     * not grade that test. Please do not import java.util.Deque here!*/

    public static Deque<Integer> lld = new LinkedListDeque<Integer>();

    @Test
    /** Adds a few things to the list, checks that isEmpty() is correct.
     * This is one simple test to remind you how junit tests work. You
     * should write more tests of your own.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

//        System.out.println("Make sure to uncomment the lines below (and delete this print statement)." +
//                " The code you submit to the AG shouldn't have any print statements!");
        /*


		assertTrue("A newly initialized LLDeque should be empty", lld.isEmpty());
		lld.addFirst(0);

        assertFalse("lld1 should now contain 1 item", lld.isEmpty());

        lld = new LinkedListDeque<Integer>(); //Assigns lld equal to a new, clean LinkedListDeque!
		*/
        assertTrue(lld.isEmpty());
        lld.addFirst(1);
        assertFalse(lld.isEmpty());
        lld.removeFirst();
        assertTrue(lld.isEmpty());

    }

    /** Adds an item, removes an item, and ensures that dll is empty afterwards. */
    @Test
    public void addRemoveTest() {
        lld.addFirst(1);
        lld.addLast(2);
        lld.addFirst(3);
        assertTrue(lld.removeFirst() == 3);
        assertTrue(lld.removeLast() == 2);
        assertTrue(lld.removeLast() == 1);

    }
    /** Make sure that removing from an empty LinkedListDeque does nothing */
    @Test
    public void removeEmptyTest() {
        lld.removeFirst();
        assertTrue(lld.isEmpty());
        lld.removeLast();
        assertTrue(lld.isEmpty());
    }
    /** Make sure your LinkedListDeque also works on non-Integer types */
    @Test
    public void multipleParamsTest() {
        LinkedListDeque<String> llds = new LinkedListDeque<>();

        // empty test
        assertTrue(llds.isEmpty());
        llds.addFirst("123");
        assertFalse(llds.isEmpty());
        llds.removeFirst();
        assertTrue(llds.isEmpty());

        //add & remove
        llds.addFirst("adafafa");
        llds.addLast("asdafa");
        llds.addFirst("123");
        assertTrue(llds.removeFirst() == "123");
        assertTrue(llds.removeLast() == "asdafa");
        assertTrue(llds.removeLast() == "adafafa");

        llds = new LinkedListDeque<String>();
        Deque<String> ad = new ArrayDeque<String>();
        llds.addFirst("abc");
        llds.addFirst("cde");
        llds.addFirst("def");
        ad.addFirst("abc");
        ad.addFirst("cde");
        ad.addFirst("def");
        assertEquals(true, llds.equals(ad));

    }
    /** Make sure that removing from an empty LinkedListDeque returns null */
    @Test
    public void emptyNullReturn() {
        assertTrue(lld.removeLast() == null);
        assertTrue(lld.removeFirst() == null);
    }

    /** TODO: Write tests to ensure that your implementation works for really large
     * numbers of elements, and test any other methods you haven't yet tested!
     */

    @Test
    public void largeTests(){
        for(int i = 0; i < 10000; i++){
            lld.addFirst(i);
        }
        for(int i = 0; i < 10000; i++){
            lld.removeLast();
        }
    }

}