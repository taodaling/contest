package template.math;

import template.primitve.generated.datastructure.IntegerList;
import template.rand.RandomWrapper;

import java.util.Arrays;

public class ModSparseMatrix {
    private int[] x;
    private int[] y;
    private int[] elements;
    private int n;

    public int getSize() {
        return n;
    }

    public ModSparseMatrix(ModMatrix mat) {
        this.n = mat.n;
        int m = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat.mat[i][j] > 0) {
                    m++;
                }
            }
        }
        x = new int[m];
        y = new int[m];
        elements = new int[m];
        int cur = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat.mat[i][j] > 0) {
                    x[cur] = i;
                    y[cur] = j;
                    elements[cur] = mat.mat[i][j];
                    cur++;
                }
            }
        }
    }

    /**
     * @param n size of the matrix
     * @param m how many non-zero elements
     */
    public ModSparseMatrix(int n, int m) {
        this.n = n;
        x = new int[m];
        y = new int[m];
        elements = new int[m];
    }

    public int getX(int i) {
        return x[i];
    }

    public int getY(int i) {
        return y[i];
    }

    public int getElement(int i) {
        return elements[i];
    }

    public void set(int i, int x, int y, int element) {
        this.x[i] = x;
        this.y[i] = y;
        this.elements[i] = element;
    }

    /**
     * Store Av => output
     */
    public void rightMul(int[] v, int[] output, Modular mod) {
        Arrays.fill(output, 0);
        for (int j = 0; j < elements.length; j++) {
            output[x[j]] = mod.plus(output[x[j]], mod.mul(elements[j], v[y[j]]));
        }
    }

    /**
     * Store v^TA => output
     */
    public void leftMul(int[] v, int[] output, Modular mod) {
        Arrays.fill(output, 0);
        for (int j = 0; j < elements.length; j++) {
            output[y[j]] = mod.plus(output[y[j]], mod.mul(elements[j], v[x[j]]));
        }
    }

    /**
     * <p>Randomly get the minimal-polynomial of n * n matrix A with m non-zero entry. O(n(m+n))</p>
     */
    public IntegerList getMinimalPolynomialByRandom(Modular mod) {
        int modVal = mod.getMod();
        int m = x.length;
        int[] u = new int[n];
        int[] v = new int[n];
        int[] next = new int[n];
        for (int i = 0; i < n; i++) {
            u[i] = RandomWrapper.INSTANCE.nextInt(1, modVal - 1);
            v[i] = RandomWrapper.INSTANCE.nextInt(1, modVal - 1);
        }

        ModLinearFeedbackShiftRegister lfsr = new ModLinearFeedbackShiftRegister(mod, 2 * n);
        for (int i = 0; i < 2 * n; i++) {
            long ai = 0;
            for (int j = 0; j < n; j++) {
                ai += (long) u[j] * v[j] % modVal;
            }
            ai %= modVal;
            lfsr.add((int) ai);
            rightMul(v, next, mod);
            int[] tmp = next;
            next = v;
            v = tmp;
        }

        IntegerList polynomials = new IntegerList(lfsr.length() + 1);
        for (int i = lfsr.length(); i >= 1; i--) {
            polynomials.add(mod.valueOf(-lfsr.codeAt(i)));
        }
        polynomials.add(1);
        return polynomials;
    }

    /**
     * O(n(m+n))
     */
    public int determinant(Modular mod) {
        IntegerList minPoly = getMinimalPolynomialByRandom(mod);
        int ans = minPoly.get(0);
        if (n % 2 == 1) {
            ans = mod.valueOf(-ans);
        }
        return ans;
    }

}
