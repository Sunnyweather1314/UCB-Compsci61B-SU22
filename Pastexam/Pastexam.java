package Pastexam;

public class Pastexam {


    public static void main(String[] args) {

    }
/**
 * FA19 MT1 https://cdn-uploads.piazza.com/paste/j8w1fc76tv07ha/9afdc7f9a32d88793e71b34e72adecb37360d1ca4ee7f588dc94c7a30194a4bb/fa19mt1sol.pdf
 * The function partition performs a step used in a sort procedure. Given
 an IntList L such as
 [10, 5, 12, 1, 3, 20, 0]
 the call partition(L) will rearrange the values in L so that all values less than or equal to
 the first (10 in this case) will come first in the list, before all those greater than the first.
 This first value is called the pivot. For example, for L above, the result could be
 [0, 3, 1, 5, 10, 20, 12 ].
 For the particular way we partition in this problem, the portions of the result that are
 ≤ the pivot or > the pivot are in the reverse order from how they appeared in the original
 list L.
 We use a helper function called part. This takes a pivot, a list L of remaining items to be
 partitioned, two IntList objects—leftFirst and rightLast—that are the first and last
 objects in a list of values ≤ the pivot, and an IntList object right that is the first object
 in a list of values > the pivot (or null if there are none). It then adds each element of L in
 front of leftFirst or right, and finally returns the result of concatenating the two lists.
 Hence, if L contains [1, 3, 20, 0], leftFirst contains [5, 10,...], leftLast points
 at the second item (10) in the list leftFirst, and right contains [12], then part(10,
 L, leftFirst, leftLast, right) will return
 [0, 3, 1, 5, 10, 20, 12 ].
 (The list starting at leftFirst is allowed to contain additional stuff after the item pointed
 to by leftLast, but it gets thrown away in the result.)
 Fill in the definition of partition to meet its specification.
 Test #1 Login: Initials: 9 */
    /**
     * Given that L consists of the values x1 , ... , xn , where n >= 1,
     * return a list containing the same values , but with all values that
     * are <= x1 coming before all values that are > x1 . The operation
     * is destructive ; no new IntList objects are created .
     */


    public static IntList partition(IntList L) {
        return part(L.head, L.tail, L, L, null);
    }


    /**
     * Assuming that LEFTFIRST and LEFTLAST are the ( non - null ) first
     * and last IntList objects in a list , ( destructively ) return the
     * result of adding all elements of L that are <= PIVOT to the
     * front of LEFTFIRST , all elements > PIVOT to the front of RIGHT ,
     * and then concatenating the elements in RIGHT after those from
     * LEFTFIRST to LEFTLAST . The initial . tail of LEFTLAST
     * is overwritten .
     */


    private static IntList part(int pivot, IntList L,
                                IntList leftFirst, IntList leftLast,
                                IntList right) {

        if (L == null) {
            leftLast.tail = right;
            return leftFirst;
        }

        IntList next = L.tail;

        if (L.head <= pivot) {
            L.tail = leftFirst;
            return part(pivot, next, L, leftLast, right);
        } else {
            L.tail = right;
            return part(pivot, next, leftFirst, leftLast, L);
        }

    }


    //sp20

    /**
     * Return the result of applying the swapUp algorithm to L ,
     * destructively .
     */
    IntList dswapUpList(IntList L) {
        if (L == null || L.tail == null) {
            return L;
        }
        IntList holder = new IntList(0, L);
        /* Create no other IntList objects . */
        IntList prev;
        prev = holder;
        while (prev.tail.tail != null) {
            IntList L0 = prev.tail,
                    L1 = L0.tail;
            if (L0.head > L1.head) {
                L0.tail = L1.tail;
                L1.tail = L0;
                prev.tail = L1;
                prev = L1;
            } else {
                prev = L0;
            }
        }
        return holder.tail;
    }

}
