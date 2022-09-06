/** A data structure to represent a Linked List of Integers.
 * Each IntList represents one node in the overall Linked List.
 *
 * @author Maurice Lee and Wan Fung Chui
 */

public class IntList {

    /**
     * The integer stored by this node.
     */
    public int item;
    /**
     * The next node in this IntList.
     */
    public IntList next;

    /**
     * Constructs an IntList storing ITEM and next node NEXT.
     */
    public IntList(int item, IntList next) {
        this.item = item;
        this.next = next;
    }

    /**
     * Constructs an IntList storing ITEM and no next node.
     */
    public IntList(int item) {
        this(item, null);
    }


    public static void main(String[] args) {
        IntList test = IntList.of(1, 2, 3);
        test.squaredSum();
    }

    /**
     * Returns an IntList consisting of the elements in ITEMS.
     * IntList L = IntList.list(1, 2, 3);
     * System.out.println(L.toString()) // Prints 1 2 3
     */
    public static IntList of(int... items) {
        /** Check for cases when we have no element given. */
        if (items.length == 0) {
            return null;
        }
        /** Create the first element. */
        IntList head = new IntList(items[0]);
        IntList last = head;
        /** Create rest of the list. */
        for (int i = 1; i < items.length; i++) {
            last.next = new IntList(items[i]);
            last = last.next;
        }
        return head;
    }

    /**
     * Returns [position]th item in this list. Throws IllegalArgumentException
     * if index out of bounds.
     *
     * @param position, the position of element.
     * @return The element at [position]
     */
    public int get(int position) {
        //TODO: YOUR CODE HERE
        if (position < 0) {
            throw new IllegalArgumentException("Cannot have negative indices");
        }
        int tempNum = item;
        IntList tempNext = next;
        for (int i = 1; i <= position; i++) {
            if (tempNext == null) {
                throw new IllegalArgumentException("Longer than the IntList");

            }
            tempNum = tempNext.item;
            tempNext = tempNext.next;
        }

        return tempNum;
    }

    /**
     * Returns the string representation of the list. For the list (1, 2, 3),
     * returns "1 2 3".
     *
     * @return The String representation of the list.
     */
    public String toString() {
        //TODO: YOUR CODE HERE
        String message = "";
        IntList tempNext = next;
        int tempNum = item;
        while (tempNext != null) {
            message = message + tempNum + " ";
            tempNum = tempNext.item;
            tempNext = tempNext.next;
        }
        message = message + tempNum;
        return message;
    }

    /**
     * Returns whether this and the given list or object are equal.
     * <p>
     * NOTE: A full implementation of equals requires checking if the
     * object passed in is of the correct type, as the parameter is of
     * type Object. This also requires we convert the Object to an
     * IntList, if that is legal. The operation we use to do this is called
     * casting, and it is done by specifying the desired type in
     * parenthesis. An example of this is on line 84.
     *
     * @param obj, another list (object)
     * @return Whether the two lists are equal.
     */
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof IntList)) {
            return false;
        }
        IntList otherLst = (IntList) obj;

        //TODO: YOUR CODE HERE

        int tempNum = item;
        if (tempNum != otherLst.item) {
            return false;
        } else if (next == null && otherLst.next == null) {
            return true;

        } else {
            return next.equals(otherLst.next);
        }
    }

    /**
     * Adds the given value at the end of the list.
     *
     * @param value, the int to be added.
     */
    public void add(int value) {
        //TODO: YOUR CODE HERE
        IntList tempNext = next;
        IntList added = new IntList(value);

        if (next == null) {
            next = added;
            return;
        }
        while (tempNext.next != null) {

            tempNext = tempNext.next;
        }

        tempNext.next = added;
    }

    /**
     * Returns the smallest element in the list.
     *
     * @return smallest element in the list
     */
    public int smallest() {
        //TODO: YOUR CODE HERE
        IntList tempNext = next;
        int tempNum = item;
        int smallest = item;
        while (tempNext != null) {

            tempNum = tempNext.item;
            tempNext = tempNext.next;
            if (smallest > tempNum) {
                smallest = tempNum;
            }
        }
        return smallest;
    }

    /**
     * Returns the sum of squares of all elements in the list.
     *
     * @return The sum of squares of all elements.
     */
    public int squaredSum() {
        //TODO: YOUR CODE HERE
        IntList tempNext = next;
        int tempNum = item;
        int sum = item * item;
        while (tempNext != null) {

            tempNum = tempNext.item;
            tempNext = tempNext.next;
            sum += tempNum * tempNum;
        }
        return sum;
    }

    /**
     * Destructively squares each item of the list.
     *
     * @param L list to destructively square.
     */
    public static void dSquareList(IntList L) {
        while (L != null) {
            L.item = L.item * L.item;
            L = L.next;
        }
    }

    /**
     * Returns a list equal to L with all elements squared. Non-destructive.
     *
     * @param L list to non-destructively square.
     * @return the squared list.
     */
    public static IntList squareListIterative(IntList L) {
        if (L == null) {
            return null;
        }
        IntList res = new IntList(L.item * L.item, null);
        IntList ptr = res;
        L = L.next;
        while (L != null) {
            ptr.next = new IntList(L.item * L.item, null);
            L = L.next;
            ptr = ptr.next;
        }
        return res;
    }

    /**
     * Returns a list equal to L with all elements squared. Non-destructive.
     *
     * @param L list to non-destructively square.
     * @return the squared list.
     */
    public static IntList squareListRecursive(IntList L) {
        if (L == null) {
            return null;
        }
        return new IntList(L.item * L.item, squareListRecursive(L.next));
    }

    /**
     * Returns a new IntList consisting of A followed by B,
     * destructively.
     *
     * @param A list to be on the front of the new list.
     * @param B list to be on the back of the new list.
     * @return new list with A followed by B.
     */
    public static IntList dcatenate(IntList A, IntList B) {
        //TODO: YOUR CODE HERE

        if (A == null) {
            return B;
        }
        if (B == null){
            return A;
        }
        IntList connectpoint = A;
        while (connectpoint.next != null) {
            connectpoint = connectpoint.next;
        }
        connectpoint.next = B;
        return A;
    }



    /**
     * Returns a new IntList consisting of A followed by B,
     * non-destructively.
     *
     * @param A list to be on the front of the new list.
     * @param B list to be on the back of the new list.
     * @return new list with A followed by B.
     */
     public static IntList catenate(IntList A, IntList B) {
        //TODO: YOUR CODE HERE
         if (A == null) {
             return B;
         }
         if (B == null){
             return A;
         }

         int currNum = A.item;
         IntList created = new IntList(currNum);
         IntList addPointer = created;
         IntList pointer = A;
         while (pointer.next != null) {
             pointer = pointer.next;
             currNum = pointer.item;
             IntList added = new IntList(currNum);
             addPointer.next= added;
             addPointer = addPointer.next;


         }
         pointer = B;
         currNum = B.item;
         IntList added = new IntList(currNum);
         addPointer.next= added;
         addPointer = addPointer.next;
         while (pointer.next != null) {
             pointer = pointer.next;
             currNum = pointer.item;
             added = new IntList(currNum);
             addPointer.next= added;
             addPointer = addPointer.next;

         }
         return created;
     }
}