package operations;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractOperation {
    private final Set<AbstractOperation> dependents = new HashSet<>();
    private int FNFClass = 0;

    public void setFNFClass(int FNFClass) {
        this.FNFClass = FNFClass;
    }

    public int getFNFClass() {
        return FNFClass;
    }

    public Set<AbstractOperation> getDependents() {
        return dependents;
    }

    public void addDependent(AbstractOperation operation) {
        dependents.add(operation);
    }

    public abstract double execute(double[][] M, Map<AbstractOperation, Double> results);

    public abstract boolean equals(Object o);

    public abstract int hashCode();

    public String toString() {
        return getClass().getSimpleName();
    }
}