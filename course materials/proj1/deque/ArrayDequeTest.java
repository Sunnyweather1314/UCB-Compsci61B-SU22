package deque;

import org.junit.Test;


import static org.junit.Assert.*;

/* Performs some basic array deque tests. */
public class ArrayDequeTest {

    /** You MUST use the variable below for all of your tests. If you test
     * using a local variable, and not this static variable below, the
     * autograder will not grade that test. If you would like to test
     * ArrayDeques with types other than Integer (and you should),
     * you can define a new local variable. However, the autograder will
     * not grade that test. */

    public static Deque<Integer> ad = new ArrayDeque<Integer>();

    @Test
    public void addIsEmptySizeTest() {

//        System.out.println("Make sure to uncomment the lines below (and delete this print statement)." +
//                " The code you submit to the AG shouldn't have any print statements!");
        /*


		assertTrue("A newly initialized LLDeque should be empty", lld.isEmpty());
		lld.addFirst(0);

        assertFalse("lld1 should now contain 1 item", lld.isEmpty());

        lld = new LinkedListDeque<Integer>(); //Assigns lld equal to a new, clean LinkedListDeque!
		*/
        assertTrue(ad.isEmpty());
        ad.addFirst(1);
        assertFalse(ad.isEmpty());
        ad.removeFirst();
        assertTrue(ad.isEmpty());

    }

    /** Adds an item, removes an item, and ensures that dll is empty afterwards. */
    @Test
    public void addRemoveTest() {
        ad.addFirst(1);
        ad.addLast(2);
        ad.addFirst(3);
        assertTrue(ad.removeFirst() == 3);
        assertTrue(ad.removeLast() == 2);
        assertTrue(ad.removeLast() == 1);

    }
    /** Make sure that removing from an empty LinkedListDeque does nothing */
    @Test
    public void removeEmptyTest() {
        ad.removeFirst();
        assertTrue(ad.isEmpty());
        ad.removeLast();
        assertTrue(ad.isEmpty());
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

    }
    /** Make sure that removing from an empty LinkedListDeque returns null */
    @Test
    public void emptyNullReturn() {
        assertTrue(ad.removeLast() == null);
        assertTrue(ad.removeFirst() == null);
    }

    /** TODO: Write tests to ensure that your implementation works for really large
     * numbers of elements, and test any other methods you haven't yet tested!
     */

    @Test
    public void largeTests(){
        int N = 2 ^ 20;
        for(int i = 0; i < N; i++){
            ad.addFirst(i);
        }
        for(int i = 0; i < N - 1; i++){
            ad.removeLast();
//            ad.removeFirst();
        }
        System.out.println(ad.size());
    }

    //test for correctness resize
    @Test
    public void resizetests(){
        for(int i = 0; i < 8; i++){
            ad.addFirst(i);
        }
        ad.printDeque();
        ad.addFirst(8);
        ad.printDeque();
        ad.removeFirst();
        for(int i = 0; i < 5; i++){
            ad.removeLast();
        }
        ad.printDeque();
    }

    @Test // test for get index function
    public void gettests1() {
        ad.addFirst(0);
        ad.addFirst(1);
        assertTrue(ad.get(0) == 1);
        ad.addFirst(3);
        ad.addFirst(4);
        assertTrue(ad.get(3) == 0);
        ad.addFirst(6);
        assertTrue(ad.removeFirst() == 6);
        ad.printDeque();
        ad.addFirst(8);
        ad.addLast(9);
        ad.addFirst(10);
        assertTrue(ad.get(6) == 9);

    }

    @Test
    public void gettests2(){
        ad.addFirst(0);
        ad.addFirst(1);
        ad.addFirst(2);
        ad.addLast(3);
        ad.addLast(4);
        assertTrue(ad.removeFirst() == 2);
        ad.addLast(6);
        ad.addLast(7);
        ad.addLast(8);
        ad.addFirst(9);
        ad.addFirst(10);
        assertTrue(ad.removeFirst() == 10);
        assertTrue(ad.removeFirst() == 9);
        assertTrue(ad.removeLast() == 8);
        assertTrue(ad.removeFirst() == 1);
        assertTrue(ad.get(4) == 7);
        assertTrue(ad.get(3) == 6);
        assertTrue(ad.removeLast() == 7);
        ad.printDeque();
        assertTrue(ad.removeFirst() == 0);
        ad.printDeque();
        ad.addFirst(19);
        ad.printDeque();
        assertTrue(ad.get(2) == 4);
    }


    @Test
    public void equalsadlld(){
        Deque<Integer> lld = new LinkedListDeque<>();
        ad.addLast(1);
        ad.addLast(2);
        ad.addFirst(3);
//        ad.printDeque();
        lld.addLast(1);
        lld.addLast(2);
        lld.addFirst(3);
        lld.printDeque();
        assertTrue(ad.equals(lld));
        assertTrue(ad.equals(ad));


        Deque<String> llds = new LinkedListDeque<String>();
        Deque<String> ad2 = new ArrayDeque<String>();
        llds.addFirst("abc");
        llds.addFirst("cde");
        llds.addFirst("def");
        ad2.addFirst("abc");
        ad2.addFirst("cde");
        ad2.addFirst("def");
        assertEquals(true, ad2.equals(llds));

    }

    @Test // test for get index function
    public void gettests3(){

        //random test
//        for(int i = 0; i < 20; i++){
//            ad.addFirst(i);
//        }
////        ad.printDeque();
//
//        for(int i = 0; i < 15; i++){
//            ad.addLast(20 + i);
//        }
////        ad.printDeque();
////        System.out.println(ad.get(10));
//        assertTrue(ad.get(10) == 9);
//        assertTrue(ad.get(30) == 30);

//        for(int i = 0; i < 16; i++){
//            ad.addLast(i);
//        }
////        ad.printDeque();
////        System.out.println(ad.get(9));
//        for(int i = 0; i < 16; i++){
//
//            assertTrue(ad.get(i) == i);
//
////            System.out.println(ad.get(i));
//        }

        //fill-up empty test
        for(int i = 0; i < 128; i++){
            ad.addFirst(i);
        }
        ad.printDeque();
        for(int i = 0; i < 128; i++){
            ad.removeLast();
        }
        ad.printDeque();
        for(int i = 0; i < 128; i++){
            ad.addFirst(i);
        }
        ad.printDeque();

        //clear
        for(int i = 0; i < 128; i++){
            ad.removeLast();
        }

    }



}