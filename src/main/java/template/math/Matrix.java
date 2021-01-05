package template.math;

import template.polynomial.GravityLagrangeInterpolation;
import template.utils.CloneSupportObject;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class Matrix extends CloneSupportObject<Matrix> {
    double[] mat;
    int m;

    public int getHeight() {
        return mat.length / m;
    }

    public int getWidth() {
        return m;
    }

    public void set(int i, int j, double val) {
        mat[i * m + j] = val;
    }

    public double get(int i, int j) {
        return mat[i * m + j];
    }

    public Matrix(Matrix model) {
        m = model.m;
        mat = model.mat.clone();
    }

    public Matrix(double[] mat, int m) {
        assert mat.length % m == 0;
        this.mat = mat;
        this.m = m;
    }

    public Matrix(double[][] mat) {
        this.m = mat[0].length;
        this.mat = new double[mat.length * m];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < m; j++) {
                this.mat[i * m + j] = mat[i][j];
            }
        }
    }

    public Matrix(int n, int m) {
        this.m = m;
        mat = new double[n * m];
    }

    public void fill(int v) {
        Arrays.fill(mat, v);
    }

    public void asStandard() {
        fill(0);
        for (int i = 0; i < mat.length; i += m + 1) {
            mat[i] = 1;
        }
    }


    public static Matrix mul(Matrix a, Matrix b) {
        assert a.getWidth() == b.getWidth();
        int h = a.getHeight();
        int mid = a.getWidth();
        int w = b.getWidth();
        Matrix c = new Matrix(h, w);

        for (int i = 0; i < h; i++) {
            for (int k = 0; k < mid; k++) {
                for (int j = 0; j < w; j++) {
                    c.mat[i * w + j] += a.mat[i * mid + k] * b.mat[k * w + j];
                }
            }
        }
        return c;
    }

    public static Matrix pow(Matrix x, long n) {
        if (n == 0) {
            assert x.square();
            Matrix r = new Matrix(x.getHeight(), x.getWidth());
            r.asStandard();
            return r;
        }
        Matrix r = pow(x, n >> 1);
        r = Matrix.mul(r, r);
        if (n % 2 == 1) {
            r = Matrix.mul(r, x);
        }
        return r;
    }

    public static Matrix plus(Matrix a, Matrix b) {
        assert a.getHeight() == b.getHeight();
        assert a.getWidth() == b.getWidth();
        Matrix ans = new Matrix(a.getHeight(), a.getWidth());
        for (int i = 0; i < ans.mat.length; i++) {
            ans.mat[i] = a.mat[i] + b.mat[i];
        }
        return ans;
    }

    public static Matrix subtract(Matrix a, Matrix b) {
        assert a.getHeight() == b.getHeight();
        assert a.getWidth() == b.getWidth();
        Matrix ans = new Matrix(a.getHeight(), a.getWidth());
        for (int i = 0; i < ans.mat.length; i++) {
            ans.mat[i] = a.mat[i] - b.mat[i];
        }
        return ans;
    }

    public static Matrix mul(Matrix a, double k) {
        Matrix ans = new Matrix(a.getHeight(), a.getWidth());
        for (int i = 0; i < ans.mat.length; i++) {
            ans.mat[i] = a.mat[i] * k;
        }
        return ans;
    }

    public static double determinant(Matrix x) {
        assert x.getHeight() == x.getWidth();
        int n = x.getWidth();
        Matrix l = new Matrix(x);
        double ans = 1;
        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int j = i; j < n; j++) {
                if (Math.abs(l.mat[j * n + i]) > Math.abs(l.mat[maxRow * n + i])) {
                    maxRow = j;
                }
            }

            if (l.mat[maxRow * n + i] == 0) {
                return 0;
            }
            if (i != maxRow) {
                l.swapRow(i, maxRow);
                ans = -ans;
            }
            ans *= l.mat[i * n + i];
            l.divideRow(i, l.mat[i * n + i]);

            for (int j = i + 1; j < n; j++) {
                if (j == i) {
                    continue;
                }
                if (l.mat[j * n + i] == 0) {
                    continue;
                }
                double f = l.mat[j * n + i];
                l.subtractRow(j, i, f);
            }
        }

        return ans;
    }

    public static Matrix inverse(Matrix x) {
        assert x.getHeight() == x.getWidth();
        int n = x.getWidth();
        Matrix l = new Matrix(x);
        Matrix r = new Matrix(n, n);
        r.asStandard();
        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int j = i; j < n; j++) {
                if (Math.abs(l.mat[j * n + i]) > Math.abs(l.mat[maxRow * n + i])) {
                    maxRow = j;
                }
            }

            if (l.mat[maxRow * n + i] == 0) {
                throw new RuntimeException("Can't inverse singular matrix");
            }
            r.swapRow(i, maxRow);
            l.swapRow(i, maxRow);

            r.divideRow(i, l.mat[i * n + i]);
            l.divideRow(i, l.mat[i * n + i]);

            for (int j = 0; j < n; j++) {
                if (j == i) {
                    continue;
                }
                if (l.mat[j * n + i] == 0) {
                    continue;
                }
                double f = l.mat[j * n + i];
                r.subtractRow(j, i, f);
                l.subtractRow(j, i, f);
            }
        }
        return r;
    }

    public static Matrix transpose(Matrix x) {
        int n = x.getHeight();
        int m = x.getWidth();
        Matrix t = new Matrix(m, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                t.mat[j * n + i] = x.mat[i * m + j];
            }
        }
        return t;
    }

    private void swapRow(int i, int j) {
        if (i == j) {
            return;
        }
        for (int ir = i * m, jr = j * m, k = 0; k < m; k++) {
            SequenceUtils.swap(mat, ir + k, jr + k);
        }
    }

    private void subtractRow(int i, int j, double f) {
        for (int ir = i * m, jr = j * m, k = 0; k < m; k++) {
            mat[ir + k] -= mat[jr + k] * f;
        }
    }

    private void divideRow(int i, double f) {
        for (int ir = i * m, k = 0; k < m; k++) {
            mat[ir + k] /= f;
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        int h = getHeight();
        int w = getWidth();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                builder.append(mat[i * w + j]).append(' ');
            }
            builder.append('\n');
        }
        return builder.toString();
    }


    public void asSame(Matrix matrix) {
        assert matrix.getWidth() == getWidth();
        assert matrix.getHeight() == getHeight();
        System.arraycopy(matrix.mat, 0, mat, 0, mat.length);
    }

    private void swapCol(int i, int j) {
        if (i == j) {
            return;
        }
        int h = getHeight();
        int w = getWidth();
        for (int k = 0; k < h; k++) {
            SequenceUtils.swap(mat, k * w + i, k * w + j);
        }
    }

    private void asTopHeisenbergMatrix() {
        assert square();
        int n = getWidth();
        for (int i = 0; i < n - 1; i++) {
            int maxRow = i + 1;
            for (int j = i + 2; j < n; j++) {
                if (Math.abs(mat[maxRow * n + i]) < Math.abs(mat[j * n + i])) {
                    maxRow = j;
                }
            }
            if (mat[maxRow * n + i] == 0) {
                continue;
            }
            if (maxRow != i + 1) {
                swapRow(maxRow, i + 1);
                swapCol(maxRow, i + 1);
            }
            for (int j = i + 2; j < n; j++) {
                if (mat[j * n + i] == 0) {
                    continue;
                }
                double c = mat[j * n + i] / mat[(i + 1) * n + i];
                subtractRow(j, i + 1, c);
                subtractCol(i + 1, j, -c);
            }
        }
    }

    public void subtractCol(int i, int j, double f) {
        int h = getHeight();
        int w = getWidth();
        for (int k = 0; k < h; k++) {
            mat[k * w + i] -= f * mat[k * w + j];
        }
    }

    private double topHeisenbergMatrixDeterminant() {
        assert square();
        double ans = 1;
        int n = getWidth();
        for (int i = 0; i < n - 1; i++) {
            if (Math.abs(mat[i * n + i]) < Math.abs(mat[(i + 1) * n + i])) {
                swapRow(i, i + 1);
                ans = -ans;
            }
            if (mat[(i + 1) * n + i] != 0) {
                subtractRow(i + 1, i, mat[(i + 1) * n + i] / mat[i * n + i]);
            }
            ans *= mat[i * n + i];
        }

        ans *= mat[(n - 1) * n + n - 1];
        return ans;
    }

    public boolean square() {
        return getHeight() == getWidth();
    }

    public GravityLagrangeInterpolation.Polynomial getCharacteristicPolynomial() {
        assert square();

        Matrix heisenberg = new Matrix(this);
        heisenberg.asTopHeisenbergMatrix();
        Matrix copy = new Matrix(m, m);

        GravityLagrangeInterpolation gli = new GravityLagrangeInterpolation(m + 1);
        for (int i = 0; i <= m; i++) {
            copy.asSame(heisenberg);
            for (int j = 0; j < m; j++) {
                copy.mat[j * m + j] = copy.mat[j * m + j] - i;
            }
            double y = copy.topHeisenbergMatrixDeterminant();
            if (m % 2 == 1) {
                y = -y;
            }
            gli.addPoint(i, y);
        }

        return gli.preparePolynomial();
    }
}
