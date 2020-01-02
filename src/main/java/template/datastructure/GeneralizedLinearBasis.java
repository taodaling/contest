package template.datastructure;

import java.util.Arrays;

public class GeneralizedLinearBasis {
    private boolean[][] basis;
    private int size;
    private int dimension;
    private boolean[] or;
    private boolean[] buf;
    public GeneralizedLinearBasis(int dimension) {
        this.dimension = dimension;
        basis = new boolean[dimension][dimension];
        or = new boolean[dimension];
        buf = new boolean[dimension];
    }

    public void clear() {
        size = 0;
        Arrays.fill(or, false);
    }

    public void add(boolean[] bits) {
        System.arraycopy(bits, 0, buf, 0, dimension);
        bits = buf;
        for (int i = dimension - 1; i >= 0; i--) {
            if (!bits[i]) {
                continue;
            }
            if (or[i]) {
                for (int j = 0; j < dimension; j++) {
                    bits[j] = bits[j] != basis[i][j];
                }
                continue;
            }
            or[i] = true;
            size++;
            System.arraycopy(bits, 0, basis[i], 0, dimension);
            for (int j = i + 1; j < dimension; j++) {
                if (!or[j] || !basis[j][i]) {
                    continue;
                }
                for (int k = 0; k < dimension; k++) {
                    basis[j][k] = basis[j][k] != basis[i][k];
                }
            }
            return;
        }
    }

    public int size() {
        return size;
    }
}
