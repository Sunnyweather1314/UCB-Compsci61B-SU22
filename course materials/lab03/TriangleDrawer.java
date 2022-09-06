public class TriangleDrawer {

    static int col = 0;
    static int row = 0;
    static int SIZE = 10;
    public static void main (String[] args) {
        while (row <= SIZE) {
            while (col < row) {
                System.out.print('*');

                col = col + 1;
            }
            col = 0;
            System.out.println();

            row = row + 1;
        }
    }
}
