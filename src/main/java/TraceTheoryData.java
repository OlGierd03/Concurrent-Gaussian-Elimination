import operations.AbstractOperation;
import operations.A;
import operations.B;
import operations.C;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class TraceTheoryData {
    private final Set<AbstractOperation> alphabet = new HashSet<>();
    private final int sizeM;
    private final double[][] M;
    private final List<List<AbstractOperation>> FNF = new ArrayList<>();

    public TraceTheoryData(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        M = MatrixParser.parseGivenMatrix(lines);
        sizeM = M.length;
        determineAlphabet();
        DiekertGraph graph = new DiekertGraph(alphabet);
        determineFNF(graph.getG());
        printAll();

        try {
            graph.createGraphPng(path.getFileName().toString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public double[][] getM() {
        return M;
    }


    public List<List<AbstractOperation>> getFNF() {
        return FNF;
    }


    // determine Alphabet along with dependency relationships needed to construct Diekert Graph
    private void determineAlphabet() {
        for (int i = 1; i < sizeM; i++) {
            for (int k = i+1; k < sizeM +1; k++) {
                AbstractOperation a = new A(i, k);
                if (i > 1) {
                    a.addDependent(new C(i-1, i, k));
                    a.addDependent(new C(i-1, i, i));
                }
                alphabet.add(a);
                for (int j = i; j < sizeM +2; j++) {
                    AbstractOperation b = new B(i, j, k);
                    AbstractOperation c = new C(i, j, k);
                    b.addDependent(new A(i, k));
                    c.addDependent(new B(i, j, k));
                    if (i > 1) {
                        c.addDependent(new C(i-1, j, k));
                        if (k > 1) {
                            b.addDependent(new C(i-1, j, k-1));
                        }
                    }
                    alphabet.add(b);
                    alphabet.add(c);
                }
            }
        }
    }


    private void determineFNF(Map<AbstractOperation, List<AbstractOperation>> G) {
        Set<AbstractOperation> startOperations = new HashSet<>(G.keySet());
        for (AbstractOperation operation : G.keySet()) {
            for (AbstractOperation child : G.get(operation)) {
                startOperations.remove(child);
            }
        }
        determineClasses(startOperations, G);
    }


    private void determineClasses(Set<AbstractOperation> startOperations, Map<AbstractOperation, List<AbstractOperation>> G){
        Queue<AbstractOperation> queue = new LinkedList<>(startOperations);
        int biggestClass = 0;
        FNF.add(new ArrayList<>(startOperations));

        // bfs
        while (!queue.isEmpty()) {
            AbstractOperation currOperation = queue.poll();
            for (AbstractOperation child : G.get(currOperation)) {
                int currClass = currOperation.getFNFClass()+1;
                if (child.getFNFClass() < currClass) {
                    child.setFNFClass(currClass);
                    if (currClass > biggestClass) {
                        FNF.add(new ArrayList<>());
                        biggestClass++;
                    }
                    FNF.get(currClass).add(child);
                    queue.offer(child);
                }
            }
        }

        Set<AbstractOperation> operationsOccurred = new HashSet<>();
        // remove previous occurrences
        for (int i = FNF.size() - 1; i > -1; i--) {
            for (int j = FNF.get(i).size() - 1; j > -1; j--) {
                AbstractOperation operation = FNF.get(i).get(j);
                if (operationsOccurred.contains(operation)) {
                    FNF.get(i).remove(j);
                } else {
                    operationsOccurred.add(operation);
                }
            }
        }
    }


    private void printAll() {
        System.out.println("Alphabet: " + alphabet);
        System.out.println("FNF: " + FNF);
        System.out.println("\nGiven Matrix:");
        MatrixPrinter.printMatrix(M);
    }
}
