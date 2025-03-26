package operations;

import java.util.Map;
import java.util.Objects;

public class A extends AbstractOperation {
    private final int i;
    private final int k;

    public A(int i, int k) {
        this.i = i;
        this.k = k;
    }

    @Override
    public double execute(double[][] M, Map<AbstractOperation, Double> results) {
        double result = M[k - 1][i - 1] / M[i - 1][i - 1];
        results.put(this, result);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        A a = (A) o;
        return i == a.i && k == a.k;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, k);
    }

    @Override
    public String toString() {
        return super.toString() + "(" + i + ", " + k + ")";
    }
}