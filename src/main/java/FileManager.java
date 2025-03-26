import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.logging.Logger;

public class FileManager {
    private static final Logger LOGGER = Logger.getLogger(FileManager.class.getName());
    private static final String FILE_EXTENSION_ERROR = "Error: The file must have a .txt extension.";
    private static final String FILE_NOT_FOUND_ERROR = "Error: File does not exist.";
    private static final String FILE_NOT_READABLE_ERROR = "Error: File is not readable.";


    private static boolean isTxtFile(String fileName) {
        if (!fileName.endsWith(".txt")) {
            LOGGER.warning(FILE_EXTENSION_ERROR);
            return false;
        }
        return true;
    }


    private static boolean isReadable(Path path) {
        if (!Files.isReadable(path)) {
            LOGGER.warning(FILE_NOT_READABLE_ERROR);
            return false;
        }
        return true;
    }


    private static URL getURL(String fileName) throws IOException {
        URL url = FileManager.class.getClassLoader().getResource(fileName);
        if (url == null) {
            LOGGER.warning(FILE_NOT_FOUND_ERROR);
            throw new IOException("File not found: " + fileName);
        }
        return url;
    }


    private static Path getPathFromURL(URL url) throws URISyntaxException {
        return Paths.get(url.toURI());
    }


    public static Path getPathFromFileName(String fileName) throws IllegalArgumentException, IOException, URISyntaxException {
        if (!isTxtFile(fileName)) {
            throw new IllegalArgumentException(FILE_EXTENSION_ERROR);
        }
        URL url = FileManager.getURL(fileName);
        Path path = FileManager.getPathFromURL(url);

        if (!isReadable(path)) {
            throw new IOException(FILE_NOT_READABLE_ERROR);
        }
        return path;
    }


    public static void createGvFile(String fileName, String content) throws IOException{
        Path filePath = Paths.get("src/main/resources", fileName + ".gv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writer.write(content);
        }
    }


    public static void getPngFromGvFile(String fileName, String outputFileName) throws IOException, InterruptedException {
        Path filePath = Paths.get("src/main/resources", fileName + ".gv");
        Path outputFilePath = Paths.get("src/main/resources", outputFileName + ".png");
        String[] command = {"dot", "-Tpng", filePath.toString(), "-o", outputFilePath.toString()};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        process.waitFor();
    }
}
