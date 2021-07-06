package template.math;

import template.primitve.generated.datastructure.DoubleArrayList;
import template.rand.RandomWrapper;

import java.util.Arrays;

public class SparseMatrix {
    private int[] x;
    private int[] y;
    private double[] elements;
    private int n;

    public int getSize() {
        return n;
    }

    public SparseMatrix(Matrix mat) {
        assert mat.square();
        this.n = mat.m;
        int m = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat.mat[i * n + j] != 0) {
                    m++;
                }
            }
        }
        x = new int[m];
        y = new int[m];
        elements = new double[m];
        int cur = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat.mat[i * n + j] != 0) {
                    x[cur] = i;
                    y[cur] = j;
                    elements[cur] = mat.mat[i * n + j];
                    cur++;
                }
            }
        }
    }

    /**
     * @param n size of the matrix
     * @param m how many non-zero elements
     */
    public SparseMatrix(int n, int m) {
        this.n = n;
        x = new int[m];
        y = new int[m];
        elements = new double[m];
    }

    public int getX(int i) {
        return x[i];
    }

    public int getY(int i) {
        return y[i];
    }

    public double getElement(int i) {
        return elements[i];
    }

    public void set(int i, int x, int y, double element) {
        this.x[i] = x;
        this.y[i] = y;
        this.elements[i] = element;
    }

    /**
     * Store Av => output
     */
    public void rightMul(double[] v, double[] output) {
        Arrays.fill(output, 0);
        for (int j = 0; j < elements.length; j++) {
            output[x[j]] += elements[j] * v[y[j]];
        }
    }

    /**
     * Store v^TA => output
     */
    public void leftMul(int[] v, int[] output) {
        Arrays.fill(output, 0);
        for (int j = 0; j < elements.length; j++) {
            output[y[j]] += elements[j] * v[x[j]];
        }
    }

    private double nextDouble() {
        return RandomWrapper.INSTANCE.nextDouble(1e2, 1e9);
    }

    /**
     * <p>Randomly get the minimal-polynomial of n * n matrix A with m non-zero entry. O(n(m+n))</p>
     */
    public DoubleArrayList getMinimalPolynomialByRandom() {
        double[] u = new double[n];
        double[] v = new double[n];
        double[] next = new double[n];
        for (int i = 0; i < n; i++) {
            u[i] = nextDouble();
            v[i] = nextDouble();
        }

        LinearFeedbackShiftRegister lfsr = new LinearFeedbackShiftRegister(2 * n, 1e-10);
        SumOfFloat summation = new SumOfFloat();
        for (int i = 0; i < 2 * n; i++) {
            summation.reset();
            for (int j = 0; j < n; j++) {
                summation.add(u[j] * v[j]);
            }
            lfsr.add(summation.sum());
            rightMul(v, next);
            double[] tmp = next;
            next = v;
            v = tmp;
        }

        DoubleArrayList polynomials = new DoubleArrayList(lfsr.length() + 1);
        for (int i = lfsr.length(); i >= 1; i--) {
            polynomials.add(-lfsr.codeAt(i));
        }
        polynomials.add(1);
        return polynomials;
    }

    /**
     * O(n(m+n))
     */
    public double determinant() {
        double[] rand = new double[n];
        for (int i = 0; i < n; i++) {
            rand[i] = nextDouble();
        }
        for (int i = 0; i < elements.length; i++) {
            elements[i] *= rand[x[i]];
        }
        DoubleArrayList minPoly = getMinimalPolynomialByRandom();
        double ans = minPoly.get(0);
        if (n % 2 == 1) {
            ans = -ans;
        }
        for (int i = 0; i < n; i++) {
            rand[i] = 1.0 / rand[i];
        }
        for (int i = 0; i < elements.length; i++) {
            elements[i] *= rand[x[i]];
        }
        for (int i = 0; i < n; i++) {
            ans = ans * rand[i];
        }
        return ans;
    }

    /**
     * return A^{-1}b in O(n(m+n)) time complexity, or null when there is no solution
     */
    public double[] solveLinearEquation(double[] b) {
        DoubleArrayList minPoly = getMinimalPolynomialByRandom();
        double c0 = minPoly.get(0);
        if (c0 == 0) {
            return null;
        }
        // double inv = -1 / c0;
        double[] sum = new double[n];
        double[] v = b;
        double[] next = new double[n];
        for (int i = 1; i < minPoly.size(); i++) {
            double c = minPoly.get(i);
            for (int j = 0; j < n; j++) {
                sum[j] += (c * v[j]);
            }
            rightMul(v, next);

            double[] tmp = v;
            v = next;
            next = tmp;
        }

        //double[] ans = new double[n];
        for (int i = 0; i < n; i++) {
            sum[i] /= -c0;
            //ans[i] = inv * sum[i].sum();
        }
        return sum;
    }
}
