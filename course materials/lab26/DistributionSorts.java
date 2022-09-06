import java.util.ArrayList;
import java.util.Arrays;

public class DistributionSorts {

    /* Destructively sorts ARR using counting sort. Assumes that ARR contains
       only 0, 1, ..., 9. */
    public static void countingSort(int[] arr) {
        // TODO: YOUR CODE HERE
        int[] items = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] itemCount = new int[10];
        for (int i = 0; i < arr.length; i++) {
            itemCount[arr[i]]++;
        }
        int pos = 0;
        for (int r = 0; r < 10; r++) {
            for (int k = 0; k < itemCount[r]; k++) {
                arr[pos] = items[r];
                pos++;
            }
        }
    }

    /* Destructively sorts ARR using LSD radix sort. */
    public static void lsdRadixSort(int[] arr) {
        int maxDigit = mostDigitsIn(arr);
        for (int d = 0; d < maxDigit; d++) {
            countingSortOnDigit(arr, d);
        }
    }

    /* A helper method for radix sort. Modifies ARR to be sorted according to
       DIGIT-th digit. When DIGIT is equal to 0, sort the numbers by the
       rightmost digit of each number. */
    private static void countingSortOnDigit2(int[] arr, int digit) {
        // TODO: YOUR CODE HERE
        // int pos = 0;
        // int[] arrD = new int[arr.length];
        // for (int i = 0; i < arr.length; i++) {
        //     arrD[i] = getDigit(arr[i], digit);
        // }
        // countingSort(arrD);

        // for (int i = 0; i < arr.length; i++) {
        //     if (getDigit(arr[i], digit) == arrD[i]) {
        //         pushBack
        //     }
        // }

        int[][] items = new int[10][10];
        int[] itemCount = new int[10];
        for (int i = 0; i < arr.length; i++) {
            itemCount[getDigit(arr[i], digit)]++;
            if (itemCount[getDigit(arr[i], digit)] > items[getDigit(arr[i], digit)].length) {
                items[getDigit(arr[i], digit)] = resize(items[getDigit(arr[i], digit)]);
            }
            items[getDigit(arr[i], digit)][itemCount[getDigit(arr[i], digit)]-1] = arr[i];

        }
        int pos = 0;
        for (int r = 0; r < items.length; r++) {
            for (int k = 0; k < itemCount[r]; k++) {
                arr[pos] = items[r][k];
                pos++;
                if (pos == arr.length) {
                    return;
                }
            }
        }
    }



    private static void countingSortOnDigit(int[] arr, int digit) {


        int[] starts = new int[10];
        int[] itemCount = new int[10];
        int[] output = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            itemCount[getDigit(arr[i], digit)]++;
        }
        
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                starts[i] = 0;
            } else {
                starts[i] = starts[i - 1] + itemCount[i - 1];
            }
        }


        for (int r = 0; r < arr.length; r++) {
            output[starts[getDigit(arr[r], digit)]] = arr[r];
            starts[getDigit(arr[r], digit)]++;
        }

        for (int k = 0; k < arr.length; k++) {
            arr[k] = output[k];
        }
    }



    private static int[] resize(int[] arr) {
        int[] doubled = Arrays.copyOf(arr, arr.length * 2);
        return doubled;
    }

    private static int getDigit(int num, int digit) {
        int rr = num % 10;
        if ((int) (Math.log10(num) + 1) < digit) {
            return 0;
        }

        while (digit != 0) {
            num = num /10;
            rr = num % 10;
            digit--;
        }
        return rr;
    }

    /* Returns the largest number of digits that any integer in ARR has. */
    private static int mostDigitsIn(int[] arr) {
        int maxDigitsSoFar = 0;
        for (int num : arr) {
            int numDigits = (int) (Math.log10(num) + 1);
            if (numDigits > maxDigitsSoFar) {
                maxDigitsSoFar = numDigits;
            }
        }
        return maxDigitsSoFar;
    }

    /* Returns a random integer between 0 and 9999. */
    private static int randomInt() {
        return (int) (10000 * Math.random());
    }

    /* Returns a random integer between 0 and 9. */
    private static int randomDigit() {
        return (int) (10 * Math.random());
    }

    private static void runCountingSort(int len) {
        int[] arr1 = new int[len];
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = randomDigit();
        }
        System.out.println("Original array: " + Arrays.toString(arr1));
        countingSort(arr1);
        if (arr1 != null) {
            System.out.println("Should be sorted: " + Arrays.toString(arr1));
        }
    }

    private static void runLSDRadixSort(int len) {
        int[] arr2 = new int[len];
        for (int i = 0; i < arr2.length; i++) {
            arr2[i] = randomInt();
        }
        System.out.println("Original array: " + Arrays.toString(arr2));
        lsdRadixSort(arr2);
        System.out.println("Should be sorted: " + Arrays.toString(arr2));

    }

    public static void main(String[] args) {
        runCountingSort(20);
        runLSDRadixSort(3);
        runLSDRadixSort(30);
    }
}