import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParallelMatrixMultiplication {

    private static final int MATRIX_MAX_VALUE = 10;

    public static void run(String[] args) {
        int[] userInput = validateUserInput(args);
        int m = userInput[0];
        int p = userInput[1];
        int n = userInput[2];

        // Generate an NxM matrix
        int[][] matrixNM = generateMatrix(n, m);

        // Generate an MxP matrix
        int[][] matrixMP = generateMatrix(m, p);

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

    private static int[] validateUserInput(String[] args) {
        if (args.length != 3) {
            System.out.println("Illegal arguments length");
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

    private static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(String.format("%s ", matrix[i][j]));
            }
            System.out.println();
        }
        System.out.println();
    }

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

    private static void executeThreads(List<Thread> threads) throws InterruptedException {
        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}
