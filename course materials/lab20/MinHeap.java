import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

/* A MinHeap class of Comparable elements backed by an ArrayList. */
public class MinHeap<E extends Comparable<E>> {

    /* An ArrayList that stores the elements in this MinHeap. */
    private ArrayList<E> contents;
    private int size;
    // TODO: YOUR CODE HERE (no code should be needed here if not 
    // implementing the more optimized version)

    /* Initializes an empty MinHeap. */
    public MinHeap() {
        contents = new ArrayList<>();
        contents.add(null);
    }

    /* Returns the element at index INDEX, and null if it is out of bounds. */
    private E getElement(int index) {
        if (index >= contents.size()|| index<0) {
            return null;
        } else {
            return contents.get(index);
        }
    }

    /* Sets the element at index INDEX to ELEMENT. If the ArrayList is not big
       enough, add elements until it is the right size. */
    private void setElement(int index, E element) {
        while (index >= contents.size()) {
            contents.add(null);
        }
        contents.set(index, element);
    }

    /* Swaps the elements at the two indices. */
    private void swap(int index1, int index2) {
        E element1 = getElement(index1);
        E element2 = getElement(index2);
        setElement(index2, element1);
        setElement(index1, element2);
    }

    /* Prints out the underlying heap sideways. Use for debugging. */
    @Override
    public String toString() {
        return toStringHelper(1, "");
    }

    /* Recursive helper method for toString. */
    private String toStringHelper(int index, String soFar) {
        if (getElement(index) == null) {
            return "";
        } else {
            String toReturn = "";
            int rightChild = getRightOf(index);
            toReturn += toStringHelper(rightChild, "        " + soFar);
            if (getElement(rightChild) != null) {
                toReturn += soFar + "    /";
            }
            toReturn += "\n" + soFar + getElement(index) + "\n";
            int leftChild = getLeftOf(index);
            if (getElement(leftChild) != null) {
                toReturn += soFar + "    \\";
            }
            toReturn += toStringHelper(leftChild, "        " + soFar);
            return toReturn;
        }
    }

    /* Returns the index of the left child of the element at index INDEX. */
    private int getLeftOf(int index) {
        // TODO: YOUR CODE HERE
        return index * 2+1;
    }

    /* Returns the index of the right child of the element at index INDEX. */
    private int getRightOf(int index) {
        // TODO: YOUR CODE HERE
        return index * 2 + 2;
    }

    /* Returns the index of the parent of the element at index INDEX. */
    private int getParentOf(int index) {
        // TODO: YOUR CODE HERE
        return (index-1) / 2;
    }

    /* Returns the index of the smaller element. At least one index has a
       non-null element. If the elements are equal, return either index. */
    private int min(int index1, int index2) {
        // TODO: YOUR CODE HERE
        if (index1 >= contents.size() || getElement(index1) == null) {
            return index2;
        } else if (index2 >= contents.size() || getElement(index2) == null) {
            return index1;
        } else if (getElement(index2).compareTo(getElement(index1)) > 0) {
            return index1;
        } else {
            return index2;
        }
    }

    /* Returns but does not remove the smallest element in the MinHeap. */
    public E findMin() {
        // TODO: YOUR CODE HERE

        // for (E con: contents) {
        //     if (con != null )
        // }
        return getElement(0);
        // return Collections. min(contents);
    }

    /* Bubbles up the element currently at index INDEX. */
    private void bubbleUp(int index) {
        // TODO: YOUR CODE HERE
        int parentIndex = getParentOf(index);
        E parentContent = getElement(parentIndex);
        E element = getElement(index);
        while (!(index == 0) && parentContent != null && element.compareTo(parentContent) < 0) {
            swap(parentIndex, index);
            index = parentIndex;
            parentIndex = getParentOf(index);
            element = getElement(index);
            parentContent = getElement(parentIndex);
        }
    }

    /* Bubbles down the element currently at index INDEX. */
    private void bubbleDown(int index) {
        // TODO: YOUR CODE HERE
        int childIndex1 = getLeftOf(index);
        int childIndex2 = getRightOf(index);
        E parentContent = getElement(index);
        int newIndex = index;

        // && (parentContent.compareTo(getElement(childIndex1)) > 0
        // || parentContent.compareTo(getElement(childIndex2)) > 0
        while ((getElement(childIndex1) != null || getElement(childIndex2) != null)) {

            newIndex = min(childIndex1, childIndex2);
            if(getElement(newIndex) != null && parentContent.compareTo(getElement(newIndex)) > 0) {
                swap(index, newIndex);
                index = newIndex;
                parentContent = getElement(index);
                childIndex1 = getLeftOf(index);
                childIndex2 = getRightOf(index);
            }else {
                break;
            }


        }
    }

    /* Returns the number of elements in the MinHeap. */
    public int size() {
        // TODO: YOUR CODE HERE
        int count = 0;
        for (E con: contents) {
            if (con != null) {
                count++;
            }
        }
        return count;
    }

    /* Inserts ELEMENT into the MinHeap. If ELEMENT is already in the MinHeap,
       throw an IllegalArgumentException.*/
    public void insert(E element) {
        // TODO: YOUR CODE HERE
        int addIndex = contents.size();
        if (this.contains(element)) {
            throw new IllegalArgumentException();
        } else {
            for (int i = 0; i< contents.size(); i++) {
                if (getElement(i) == null) {
                    addIndex = i;
                    break;
                }
            }
            setElement(addIndex, element);
            bubbleDown(addIndex);
            bubbleUp(addIndex);

        }
        // if (element.compareTo(getElement(getParentOf(addIndex))) < 0){
        //     bubbleUp(addIndex);
        // } else if (element.compareTo(getElement(getLeftOf(addIndex))) > 0 
        //     || element.compareTo(getElement(getRightOf(addIndex))) > 0) {
        //     bubbleDown(addIndex);
        // }
    }
    

    /* Returns and removes the smallest element in the MinHeap. */
    public E removeMin() {
        // TODO: YOUR CODE HERE
        E minVal = getElement(0);
        E rightMost = getElement(size()-1);
        setElement(0, rightMost);
        contents.remove(size()-1);
        int crrIndex = contents.indexOf(rightMost);
        if ((getElement(getLeftOf(crrIndex)) != null && getElement(getRightOf(crrIndex)) != null) && 
        rightMost.compareTo(getElement(getLeftOf(crrIndex))) > 0 
            && rightMost.compareTo(getElement(getRightOf(crrIndex))) > 0){
                bubbleDown(crrIndex);
        } else if((getElement(getLeftOf(crrIndex)) != null || getElement(getRightOf(crrIndex)) != null)){
            swap(crrIndex, min(crrIndex,min(getRightOf(crrIndex), getLeftOf(crrIndex))));
        }


//
//        if ((getElement(getLeftOf(crrIndex)) != null || getElement(getRightOf(crrIndex)) != null) &&
//                (rightMost.compareTo(getElement(getLeftOf(crrIndex))) > 0
//                        || rightMost.compareTo(getElement(getRightOf(crrIndex))) > 0))

        return minVal;
    }

    /* Replaces and updates the position of ELEMENT inside the MinHeap, which
       may have been mutated since the initial insert. If a copy of ELEMENT does
       not exist in the MinHeap, throw a NoSuchElementException. Item equality
       should be checked using .equals(), not ==. */
    public void update(E element) {
        // TODO: YOUR CODE HERE
        if (!contains(element)) {
            throw new NoSuchElementException();
        } else {
            for (E cons: contents) {
                if (cons.equals(element)) {
                    setElement(contents.indexOf(cons), element);
                    break;
                }
            }
            int currIndex = contents.indexOf(element);
            if (getElement(getParentOf(currIndex)) != null
                    && element.compareTo(getElement(getParentOf(currIndex))) < 0){
                bubbleUp(currIndex);
            } else if ((getElement(getLeftOf(currIndex)) != null
                    || getElement(getRightOf(currIndex)) != null)) {
                bubbleDown(currIndex);
            }
        }
    }

    /* Returns true if ELEMENT is contained in the MinHeap. Item equality should
       be checked using .equals(), not ==. */
    public boolean contains(E element) {
        // TODO: YOUR CODE HERE

        for (E con: contents) {
            if ( con!= null && con.equals(element)) {
                return true;
            }
        }
        return false;
    }

    // private boolean containHelper(E element, ) {

    // }
}
