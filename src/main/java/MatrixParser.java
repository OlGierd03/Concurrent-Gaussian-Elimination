import java.util.List;

public class MatrixParser {

    public static double[][] parseGivenMatrix(List<String> lines) {
        int sizeM = Integer.parseInt(lines.get(0));
        double[][] givenMatrix = new double[sizeM][sizeM+1];

        for (int i = 1; i < sizeM+1; i++) {
            String[] values = lines.get(i).split(" ");

            for (int j = 0; j < values.length; j++) {
                givenMatrix[i-1][j] = Double.parseDouble(values[j]);
            }
        }

        String[] values = lines.get(sizeM+1).split(" ");
        for (int i = 0; i < values.length; i++) {
            givenMatrix[i][sizeM] = Double.parseDouble(values[i]);
        }
        
        return givenMatrix;
    }
}
