import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParallelMatrixMultiplication {

    // The maximum value a matrix index can have
    private static final int MATRIX_MAX_VALUE = 10;

    public static void run(String[] args) {
        int[] userInput = validateUserInput(args);
        int n = userInput[0];
        int m = userInput[1];
        int p = userInput[2];

        // Generate an NxM matrix
        int[][] matrixNM = generateMatrix(n, m);

        // Generate an MxP matrix
        int[][] matrixMP = generateMatrix(m, p);

        // Print the randomly generated matrices
        printMatrix(matrixNM);
        printMatrix(matrixMP);

        PrintMonitor printMonitor = new PrintMonitor(n, p);

        List<Thread> multiplierThreads = generateThreads(n, p, matrixNM, matrixMP, printMonitor);

        try {
            executeThreads(multiplierThreads);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receives and validates the user's input.
     * @param args command line arguments
     * @return array of arguments converted to integers
     */
    private static int[] validateUserInput(String[] args) {
        if (args.length != 3) {
            System.out.println("Illegal arguments length, expected to receive: n, m, p");
            System.exit(1);
        }
        int[] userInput = new int[3];

        for (int i = 0; i < args.length; i++) {

            try {
                userInput[i] = Integer.parseInt(args[i]);
            } catch (Exception e) {
                System.out.println("Arguments must be integers");
                System.exit(1);
            }
        }

        return userInput;
    }

    /**
     * Generates a random matrix according to the given input.
     * @param rows number of rows
     * @param columns number of columns
     * @return random matrix
     */
    private static int[][] generateMatrix(int rows, int columns) {
        Random random = new Random();
        int[][] matrix = new int[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = random.nextInt(MATRIX_MAX_VALUE);
            }
        }

        return matrix;
    }

    /**
     * Prints the given matrix.
     * @param matrix matrix to print
     */
    private static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(String.format("%s ", matrix[i][j]));
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Generates a list of matrix multiplication threads threads.
     * @param n number of rows for threads
     * @param p number of columns for threads
     * @param firstMatrix first matrix of the multiplication
     * @param secondMatrix second matrix of the multiplication
     * @param printMonitor the printing monitor
     * @return list of threads
     */
    private static List<Thread> generateThreads(int n, int p,
                                                int[][] firstMatrix, int[][] secondMatrix,
                                                PrintMonitor printMonitor) {
        List<Thread> threads = new ArrayList<>();
        Runnable matrixIndexMultiplier;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                matrixIndexMultiplier = new MatrixIndexMultiplier(i, j, firstMatrix, secondMatrix,
                        printMonitor);
                threads.add(new Thread(matrixIndexMultiplier));
            }
        }

        return threads;
    }

    /**
     * Executes the threads in the given list.
     * @param threads threads to execute
     * @throws InterruptedException thread interrupt exception
     */
    private static void executeThreads(List<Thread> threads) throws InterruptedException {
        for (Thread thread : threads) {
            thread.start();
        }

        // Wait for threads to finish
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
