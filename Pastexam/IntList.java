package Pastexam;

public class IntList {
    /** First element of list. */
    public int head;
    /** Remaining elements of list. */
    public IntList tail;
    /** A List with head HEAD0 and tail TAIL0. */

    /** A List with null tail, and head = HEAD0. */
    public IntList(int head0) { this(head0, null); }
    /** Returns a new IntList containing the ints in ARGS. */

    /** Returns true iff L (together with the items reachable from it) is an
     * IntList with the same items as this list in the same order. */

    public	IntList(int	head,	IntList	tail)	{
        this.head	=	head;	this.tail =	tail;
    }
    public	static	void	swapTails(IntList	first,	IntList	second)	{
        IntList	temp	=	first.tail;
        first.tail	=	second.tail;
        second.tail	=	temp;
    }
    public	static	void	swapHeads(int	x,	int	y)	{
        int	temp	=	x;
        x	=	y;
        y	=	temp;
    }
    /** Return the length of the non-circular list headed by L. */
    public int size() {
// Implementation not shown
        return 1;
    }


    public static void main(String[] args) {
        IntList	L	=	new	IntList(1,	new	IntList(2,	new
                IntList(3,	new	IntList(4,	null))));
        IntList	M	=	L.tail.tail;
        M.tail.tail	=	new	IntList(5,	null);
        IntList.swapTails(L,	M);
        M.tail.tail.head	=	6;
        IntList.swapHeads(M.head,	L.head);
    }
}