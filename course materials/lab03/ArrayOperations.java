import java.util.Arrays;


public class ArrayOperations {

    public static void main(String[] args){
//        int[] values = {1, 2, 3, 4, 5};
//        ArrayOperations.delete(values, 2);
//        int i = 0;
//        while(i<values.length){
//            System.out.print(values[i]);
//            i ++;
//        }
    }
        /**
         * Delete the value at the given position in the argument array, shifting
         * all the subsequent elements down, and storing a 0 as the last element of
         * the array.
         */
    public static void delete(int[] values, int pos) {
        for(int i = 0; i <= values.length; i ++){
            if (pos < 0 || pos >= values.length) {
                values[values.length-1] = 0;
                break;
            }
            if(i == values.length){
                values[i-1]=0;
                break;
            }

            if(i>pos){
                values[i-1] = values[i];
            }
            System.out.println(values);
        }
        // TODO: YOUR CODE HERE
    }

    /**
     * Insert newInt at the given position in the argument array, shifting all
     * the subsequent elements up to make room for it. The last element in the
     * argument array is lost.
     */
    public static void insert(int[] values, int pos, int newInt) {
        for(int i = values.length-1; i >= 0; i --){
            if (pos < 0 || pos >= values.length) {
                break;
            }
            if (i>pos){
                values[i]=values[i-1];
            }

            if(i==pos){
                values[i] = newInt;
                break;
            }
        }

        // TODO: YOUR CODE HERE
    }

    /**
     * Returns a new array consisting of the elements of A followed by the
     *  the elements of B.
     */
    public static int[] catenate(int[] A, int[] B) {
        int len = A.length+B.length;
        int[] newlst = new int[len];
        int index = 0;
        while(index < len){
            if (index < A.length) {
                newlst[index] = A[index];
            }else{
                newlst[index] = B[index-A.length];
            }
            index ++;
        }
        return newlst;

        // TODO: YOUR CODE HERE

    }


}
