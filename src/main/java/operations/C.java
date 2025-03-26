package operations;

import java.util.Map;
import java.util.Objects;

public class C extends AbstractOperation {
    private final int i;
    private final int k;
    private final int j;

    public C(int i, int j, int k) {
        this.i = i;
        this.j = j;
        this.k = k;
    }

    @Override
    public double execute(double[][] M, Map<AbstractOperation, Double> results) {
        B dependency = new B(i, j, k);
        double adjustment = results.get(dependency);
        M[k - 1][j - 1] -= adjustment;
        results.put(this, adjustment);
        return adjustment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        C c = (C) o;
        return i == c.i && k == c.k && j == c.j;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, k, j);
    }

    @Override
    public String toString() {
        return super.toString() + "(" + i + "," + j + "," + k + ")";
    }
}