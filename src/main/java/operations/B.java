package operations;

import java.util.Map;
import java.util.Objects;

public class B extends AbstractOperation {
    private final int i;
    private final int k;
    private final int j;

    public B(int i, int j, int k) {
        this.i = i;
        this.j = j;
        this.k = k;
    }


    @Override
    public double execute(double[][] M, Map<AbstractOperation, Double> results) {
        A dependency = new A(i, k);
        double result = M[i - 1][j - 1] * results.get(dependency);
        results.put(this, result);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        B b = (B) o;
        return i == b.i && k == b.k && j == b.j;
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