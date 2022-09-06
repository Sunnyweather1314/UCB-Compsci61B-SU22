public class ArrayStack {
    private int[] stack;

    public ArrayStack(int[] elems) {
        stack = elems;
    }

    public int pop() {
        int top = stack[0];
        shiftLeft(stack);
        return top;
    }

    public static void shiftLeft(int[] arr) {
        for (int i = 1; i < arr.length; i += 1)
            arr[i - 1] = arr[i];
    }
    
    public static void main(String[] args) {
        int[] elems = new int[] {1, 3, 5, 7, 9};
        ArrayStack stk = new ArrayStack(elems);
        elems[0] = stk.pop();
        System.out.println(stk.pop());
        System.out.println(elems[0]);
        shiftLeft(elems);
        stk.shiftLeft(elems);
        System.out.println(elems[0] == elems[4]);
    }
}

