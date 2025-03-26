public class MatrixPrinter {

    public static void printMatrix(double[][] M) {
        int sizeM = M.length;
        for (double[] row : M) {
            for (double elem : row) {
                System.out.printf(elem + " ");
            }
            System.out.printf("| " + row[sizeM] + "\n");
        }
    }
}
