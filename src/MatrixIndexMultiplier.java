/**
 * A thread that performs matrix multiplication on a specific index.
 */
public class MatrixIndexMultiplier implements Runnable {

    /**
     * Result row index.
     */
    private int rowIndex;

    /**
     * Result column index.
     */
    private int columnIndex;

    /**
     * The first matrix in the multiplication.
     */
    private int[][] firstMatrix;

    /**
     * The second matrix in the multiplication.
     */
    private int[][] secondMatrix;

    /**
     * A printing monitor to schedule prints.
     */
    private PrintMonitor printMonitor;

    /**
     * Constructor.
     * @param rowIndex result row index
     * @param columnIndex result column index
     * @param firstMatrix first multiplication matrix
     * @param secondMatrix second multiplication matrix
     * @param printMonitor printing monitor to schedule prints
     */
    public MatrixIndexMultiplier(int rowIndex, int columnIndex,
                                 int[][] firstMatrix, int[][] secondMatrix,
                                 PrintMonitor printMonitor) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
        this.printMonitor = printMonitor;
    }

    @Override
    public void run() {
        int result = calculateResult();
        printResult(result);
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * Calculates the result of the matrix multiplication in the specific index.
     * @return the dot product of the row and column vectors that represent the result index
     */
    private int calculateResult() {
        int[] rowVector = this.firstMatrix[this.rowIndex];
        int[] columnVector = getColumnVector(this.columnIndex, this.secondMatrix);
        int result = 0;

        for (int i = 0; i < rowVector.length; i++) {
            result += rowVector[i] * columnVector[i];
        }

        return result;
    }

    /**
     * Prints the result at the right turn using a printing monitor.
     * @param result result to print
     */
    private void printResult(int result) {
        String messageToPrint = String.format("Thread of indices (%s,%s) returned: %s",
                this.rowIndex, this.columnIndex, result);
        try {
            this.printMonitor.print(this, messageToPrint);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the column vector from the second matrix to perform the dot product.
     * @param columnIndex index of the column to get
     * @param matrix matrix from which to get the column vector
     * @return column vector
     */
    private int[] getColumnVector(int columnIndex, int[][] matrix) {
        int[] columnVector = new int[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            columnVector[i] = matrix[i][columnIndex];
        }

        return columnVector;
    }
}
