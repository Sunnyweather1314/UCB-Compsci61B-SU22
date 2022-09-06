package Pastexam;

import Pastexam.IntList;


public class Main {

    public static void main(String[] args) {
        int[][] A =
                { { 3, 1, 3, 3,  0, 1 }, { 3, 3, -1, 9 }, { 2, 4, 8,0 } };
        int[] B = new int[16];
        riffle(A, B);
        System.out.print(B);
    }

    //Q3
    static void riffle ( int [][] mlist , int [] result ) {
        int k ;
        result [0] = 0;
        k = 1;
        while ( true ) {
            int len0 = result [0];
            for ( int [] row : mlist ) {
                if ( k <= row [0]) {
                    result [0] += 1;
                    result [ result [0]] = row [k ];
                }
            }
            if ( len0 == result [0]) {
                break ;
            }
            k += 1;
        }
    }


    //Q5 IntList manipulation
    /** Return a list of all items in SOURCE that are not in REMOVE .
     * Assumes that the items in SOURCE and REMOVE are both sorted in
     * ascending order ( no duplicates ). Does not disturb the original
     * data . */
    static IntList demerge ( IntList source , IntList remove ) {
        if ( source == null ) {
            return null ;
        } else if ( remove == null ) {
            return source ;
        } else if ( source . head == remove . head ) {
            return demerge ( source . tail , remove . tail );
        } else if ( source . head < remove . head ) {
            return new IntList ( source . head , demerge ( source . tail , remove ));
        } else {
            return demerge ( source , remove . tail );
        }
    }


    /** Return a list of all items in SOURCE that are not in REMOVE .
     * Assumes that the items in SOURCE and REMOVE are both sorted in
     * ascending order ( no duplicates ). Does not disturb the original
     * data . */
    static IntList demergei ( IntList source , IntList remove ) {
        IntList result , last ;
        result = last = null ;
        while ( source != null ) {
            if ( remove == null || source . head < remove . head ) {
                IntList item = new IntList ( source . head );
                if ( last == null ) {
                    result = item ;
                } else {
                    last . tail = item ;
                }
                last = item ;
                source = source . tail ;
            } else if ( source . head == remove . head ) {
                remove = remove . tail ;
                source = source . tail ;
            } else {
                remove = remove . tail ;
            }
        }
        return result ;
    }
}
