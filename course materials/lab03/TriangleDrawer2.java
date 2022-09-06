public class TriangleDrawer2 {


    static int SIZE = 10;

    public static void main(String[] args) {
        for (int row = 0; row <= SIZE; row++) {
            for (int col = 0; col < row; col++) {
                System.out.print('*');
            }
            System.out.println();
        }
    }
}

