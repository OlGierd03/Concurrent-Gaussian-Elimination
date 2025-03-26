import operations.AbstractOperation;

import java.io.IOException;
import java.util.*;

public class DiekertGraph {
    private final Map<AbstractOperation, List<AbstractOperation>> G = new HashMap<>();
    private final Map<AbstractOperation, Integer> OperationToIdx = new HashMap<>();

    public Map<AbstractOperation, List<AbstractOperation>> getG() { return G; }


    public DiekertGraph(Set<AbstractOperation> alphabet) {
        // creating vertexes
        int i = 0;
        for (AbstractOperation operation : alphabet) {
            G.put(operation, new ArrayList<>());
            OperationToIdx.put(operation, i);
            i++;
        }

        // adding edges
        for (AbstractOperation operation : alphabet) {
            for (AbstractOperation dependence : operation.getDependents()) {
                G.get(dependence).add(operation);
            }
        }

        // deleting unnecessary edges
        for (AbstractOperation operation : G.keySet()) {
            List<AbstractOperation> iterateOver = new ArrayList<>(G.get(operation));
            for (AbstractOperation child : iterateOver) {
                G.get(operation).remove(child);
                if (!verticesConnected(operation, child)) {
                    G.get(operation).add(child);
                }
            }
        }
    }


    private boolean verticesConnected(AbstractOperation operation, AbstractOperation child) {
        return dfs(operation, child);
    }


    // dfs is simplified because the graph has no cycles
    private boolean dfs(AbstractOperation curr, AbstractOperation target){
        if (curr.equals(target)) { return true; }
        for (AbstractOperation child : G.get(curr)) {
            if (dfs(child, target)) { return true; }
        }
        return false;
    }


    public void createGraphPng(String fileName) throws IOException, InterruptedException {
        StringBuilder content = new StringBuilder("digraph g {\n");
        for (AbstractOperation operation : G.keySet()) {
            for (AbstractOperation child : G.get(operation)) {
                content.append("\t").append(OperationToIdx.get(operation)).append(" -> ").append(OperationToIdx.get(child)).append(";\n");
            }
        }
        content.append("\n");
        for (AbstractOperation operation : G.keySet()) {
            content.append("\t").append(OperationToIdx.get(operation)).append(" [label=\"").append(operation.toString()).append("\"];\n");
        }
        content.append("}");

        String fileNameTrimmed = fileName.substring(0, fileName.length() - ".txt".length());
        FileManager.createGvFile(fileNameTrimmed, String.valueOf(content));
        FileManager.getPngFromGvFile(fileNameTrimmed, fileNameTrimmed);
    }
}
