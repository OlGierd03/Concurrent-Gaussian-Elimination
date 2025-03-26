import operations.AbstractOperation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class GaussSolver {

    public static double[] solve(double[][] M, List<List<AbstractOperation>> FNF) {
        // results - results of previously executed operations
        Map<AbstractOperation, Double> results = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            for (List<AbstractOperation> operations : FNF) {
                executeOperations(M, operations, results, executor);
            }
            return backSubstitution(M);
        } finally {
            shutdownExecutor(executor);
        }
    }


    private static void executeOperations(
            double[][] M,
            List<AbstractOperation> operations,
            Map<AbstractOperation, Double> results,
            ExecutorService executor) {
        
        List<? extends Future<?>> futures = operations.stream()
                .map(op -> executor.submit(() -> op.execute(M, results)))
                .toList();

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } catch (ExecutionException e) {
                throw new RuntimeException("Error during operation execution", e);
            }
        }
    }


    private static void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


    private static double[] backSubstitution(double[][] M) {
        int sizeM = M.length;
        double[] solution = new double[sizeM];

        for (int i = sizeM - 1; i > -1; i--) {
            double sum = M[i][sizeM];
            for (int j = i + 1; j < sizeM; j++) {
                sum -= M[i][j] * solution[j];
            }
            solution[i] = sum / M[i][i];
        }

        return solution;
    }


    public static void printSolution(double[] solution) {
        System.out.println("\nSolution: ");
        for (int i = 0; i < solution.length; i++) {
            System.out.printf("x_%d = %.4g\n", (i+1), solution[i]);
        }
    }
}
