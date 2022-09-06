import java.util.Arrays;


public class Star {



    static boolean allEquiv(int[] arr, int target) {
        for (int x : arr) {
            if (x != target) {
                return false;
            } 
        }
        return true;
    }

    static boolean starAdjacent(boolean[][] grid, int row, int col) {
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                if (r == 0 && c == 0) {
                    continue;
                }
                if (r + row < 0 || r + row >= grid.length) {
                    continue;
                }
                if (c + col < 0 || c + col >= grid.length) {
                    continue;
                }
                if (grid[r + row][c + col]) {
                    return true;
                }
            } 
        }
        return false;
    }






    static boolean validStarBattleSolution(int[][] areas, int s, boolean[][] stars) {
        int[] areaCounts = new int[areas.length];
        int[] rowCounts = new int[areas.length];
        int[] colCounts = new int[areas.length];
        for (int r = 0; r < areas.length; r++) {
            for (int c = 0; c < areas.length; c++) {
                if (stars[r][c]) {
                    int area = areas[r][c];
                    if (!starAdjacent(stars, r, c)) {
                        rowCounts[r] = r+1;
                        colCounts[c] = c+1;
                        areaCounts[area] = area+1;

                    } 
                } 
            } 
        }
        for (int n:areaCounts) {
            System.out.print(n+" | ");

        }
        System.out.println();

        for (int n:rowCounts) {
            System.out.print(n+" | ");

        }
        System.out.println();

        for (int n:colCounts) {
            System.out.print(n+" | ");

        }
        System.out.println();


        boolean k = Arrays.asList(areaCounts).contains(0);

        return ! (Boolean) Arrays.asList(areaCounts).contains(0) &&
                ! (Boolean) Arrays.asList(rowCounts).contains(0) &&
                !(Boolean) Arrays.asList(colCounts).contains(0);
    }


    public static void main(String[] args) {
        int[][] arr = {{0,0,0,1,2,2,},
                        {0,0,1,1,3,2},
                        {0,4,1,1,5,2},
                        {0,4,1,1,5,2},
                        {0,4,4,1,5,5},
                        {4,4,4,5,5,5}};

        boolean[][] stars = {{true, false, false, false, false, false},
                {false, false, false, false,true, false},
                {false, false,true, false, false, false},
                {false, false, false, false, false, true},
                {true,false, false, false, false, false},
                {false, false, false,true, false, false}
            };

//
//        for (int[] k:arr) {
//            for(int kk: k){
//                System.out.print(kk);
//            }
//            System.out.println();
//        }
//
//
//        for (boolean[] s:stars) {
//            for(boolean ss: s){
//                System.out.print(Boolean.toString(ss)+" | ");
//            }
//            System.out.println();
//        }



        System.out.println(validStarBattleSolution(arr,6, stars));

    }


}
