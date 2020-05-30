/**
 * Schedules threads prints according to the order of the matrix multiplication process.
 */
public class PrintMonitor {

    /**
     * Number of rows in the result matrix.
     */
    private int expectedRows;

    /**
     * Number of columns in the result matrix.
     */
    private int expectedColumns;

    /**
     * The next row that should be printed
     */
    private int currentRow;

    /**
     * The next column that should be printed
     */
    private int currentColumn;

    /**
     * Constructor.
     * @param expectedRows number of rows to be printed
     * @param expectedColumns number of columns to be printed
     */
    public PrintMonitor(int expectedRows, int expectedColumns) {
        this.expectedRows = expectedRows;
        this.expectedColumns = expectedColumns;
        this.currentRow = 0;
        this.currentColumn = 0;
    }

    /**
     * Prints the given thread's message if it turn has arrived, otherwise it's request is queued.
     * @param multiplier thread that wants to print
     * @param message thread's message to print
     * @throws InterruptedException
     */
    public synchronized void print(MatrixIndexMultiplier multiplier, String message) throws InterruptedException {

        // Check if the current thread's turn came to print
        while (!shouldPrint(multiplier.getRowIndex(), multiplier.getColumnIndex())) {
            wait();
        }
        System.out.println(message);
        this.increment();

        notifyAll();
    }

    /**
     * Checks if the given row and column numbers are the ones that should be printed in the current turn.
     * @param row row index of value to print
     * @param column column index of value to print
     * @return should print
     */
    private boolean shouldPrint(int row, int column) {
        return this.currentRow == row && this.currentColumn == column;
    }

    /**
     * Increments the current row and column values according to the expected rows and columns.
     */
    private void increment() {
        if (this.currentColumn < this.expectedColumns - 1) {
            this.currentColumn++;
        }
        else if (this.currentRow < this.expectedRows - 1) {
            this.currentColumn = 0;
            this.currentRow++;
        }
    }
}
