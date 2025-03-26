import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                LOGGER.severe("Usage: java Main <file>");
                System.exit(1);
            }
            Path filePath = FileManager.getPathFromFileName(args[0]);

            TraceTheoryData traceTheoryData = new TraceTheoryData(filePath);
            double[] solution = GaussSolver.solve(traceTheoryData.getM(), traceTheoryData.getFNF());
            GaussSolver.printSolution(solution);

        } catch (IllegalArgumentException e) {
            LOGGER.severe("Invalid argument: " + e.getMessage());
            System.exit(1);
        } catch (IOException | URISyntaxException e) {
            LOGGER.severe("File error: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error occurred", e);
            System.exit(1);
        }
    }
}
