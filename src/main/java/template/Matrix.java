package template;

public class Matrix implements Cloneable {
    double[][] mat;
    int n;
    int m;

    public void set(int i, int j, double val) {
        mat[i][j] = val;
    }

    public double get(int i, int j) {
        return mat[i][j];
    }

    public Matrix(Matrix model) {
        n = model.n;
        m = model.m;
        mat = new double[n][m];
        asSame(model);
    }

    public Matrix(double[][] mat) {
        if (mat.length == 0 || mat[0].length == 0) {
            throw new IllegalArgumentException();
        }
        this.n = mat.length;
        this.m = mat[0].length;
        this.mat = mat;
    }

    public Matrix(int n, int m) {
        this.n = n;
        this.m = m;
        mat = new double[n][m];
    }

    public void fill(int v) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = v;
            }
        }
    }

    public void asStandard() {
        fill(0);
        for (int i = 0; i < n && i < m; i++) {
            mat[i][i] = 1;
        }
    }

    public static Matrix mul(Matrix a, Matrix b, Matrix c) {
        c.fill(0);
        for (int i = 0; i < c.n; i++) {
            for (int j = 0; j < c.m; j++) {
                for (int k = 0; k < a.m; k++) {
                    c.mat[i][j] = c.mat[i][j] + a.mat[i][k] * b.mat[k][j];
                }
            }
        }
        return c;
    }

    public static Matrix mul(Matrix a, Matrix b) {
        Matrix c = new Matrix(a.n, b.m);
        return mul(a, b, c);
    }

    public static Matrix region(Matrix x, int b, int t, int l, int r) {
        Matrix y = new Matrix(t - b + 1, r - l + 1);
        for (int i = b; i <= t; i++) {
            for (int j = l; j <= r; j++) {
                y.mat[i - b][j - l] = x.mat[i][j];
            }
        }
        return y;
    }

    public static Matrix pow(Matrix x, int n) {
        if (n == 0) {
            Matrix r = new Matrix(x.n, x.m);
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

    public static double determinant(Matrix x) {
        if (x.n != x.m) {
            throw new RuntimeException("Matrix is not square");
        }
        int n = x.n;
        Matrix l = new Matrix(x);
        double ans = 1;
        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int j = i; j < n; j++) {
                if (Math.abs(l.mat[j][i]) > Math.abs(l.mat[maxRow][i])) {
                    maxRow = j;
                }
            }

            if (l.mat[maxRow][i] == 0) {
                return 0;
            }
            if (i != maxRow) {
                l.swapRow(i, maxRow);
                ans = -ans;
            }
            ans *= l.mat[i][i];
            l.divideRow(i, l.mat[i][i]);

            for (int j = i + 1; j < n; j++) {
                if (j == i) {
                    continue;
                }
                if (l.mat[j][i] == 0) {
                    continue;
                }
                double f = l.mat[j][i];
                l.subtractRow(j, i, f);
            }
        }

        return ans;
    }

    public static Matrix inverse(Matrix x) {
        if (x.n != x.m) {
            throw new RuntimeException("Matrix is not square");
        }
        int n = x.n;
        Matrix l = new Matrix(x);
        Matrix r = new Matrix(n, n);
        r.asStandard();
        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int j = i; j < n; j++) {
                if (Math.abs(l.mat[j][i]) > Math.abs(l.mat[maxRow][i])) {
                    maxRow = j;
                }
            }

            if (l.mat[maxRow][i] == 0) {
                throw new RuntimeException("Can't inverse current matrix");
            }
            r.swapRow(i, maxRow);
            l.swapRow(i, maxRow);

            r.divideRow(i, l.mat[i][i]);
            l.divideRow(i, l.mat[i][i]);

            for (int j = 0; j < n; j++) {
                if (j == i) {
                    continue;
                }
                if (l.mat[j][i] == 0) {
                    continue;
                }
                double f = l.mat[j][i];
                r.subtractRow(j, i, f);
                l.subtractRow(j, i, f);
            }
        }
        return r;
    }

    public static Matrix transposition(Matrix x) {
        int n = x.n;
        int m = x.m;
        Matrix t = new Matrix(m, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                t.mat[j][i] = x.mat[i][j];
            }
        }
        return t;
    }

    private void swapRow(int i, int j) {
        double[] row = mat[i];
        mat[i] = mat[j];
        mat[j] = row;
    }

    private void subtractRow(int i, int j, double f) {
        for (int k = 0; k < m; k++) {
            mat[i][k] -= mat[j][k] * f;
        }
    }

    private void divideRow(int i, double f) {
        for (int k = 0; k < m; k++) {
            mat[i][k] /= f;
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                builder.append(mat[i][j]).append(' ');
            }
            builder.append('\n');
        }
        return builder.toString();
    }


    public void asSame(Matrix matrix) {
        if (matrix.n != n || matrix.m != m) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = matrix.mat[i][j];
            }
        }
    }

    private void swapCol(int i, int j) {
        for (int k = 0; k < n; k++) {
            double tmp = mat[k][i];
            mat[k][i] = mat[k][j];
            mat[k][j] = tmp;
        }
    }

    private void asTopHeisenbergMatrix() {
        for (int i = 0; i < n - 1; i++) {
            int maxRow = i + 1;
            for (int j = i + 2; j < n; j++) {
                if (Math.abs(mat[maxRow][i]) < Math.abs(mat[j][i])) {
                    maxRow = j;
                }
            }
            if (mat[maxRow][i] == 0) {
                continue;
            }
            if(maxRow != i + 1) {
                swapRow(maxRow, i + 1);
                swapCol(maxRow, i + 1);
            }
            for (int j = i + 2; j < n; j++) {
                if (mat[j][i] == 0) {
                    continue;
                }
                subtractRow(j, i + 1, mat[j][i] / mat[i + 1][i]);
            }
        }
    }

    private double topHeisenbergMatrixDeterminant() {
        double ans = 1;

        for (int i = 0; i < n - 1; i++) {
            if (Math.abs(mat[i][i]) < Math.abs(mat[i + 1][i])) {
                swapRow(i, i + 1);
                ans = -ans;
            }
            if (mat[i + 1][i] != 0) {
                subtractRow(i + 1, i, mat[i + 1][i] / mat[i][i]);
            }
            ans *= mat[i][i];
        }

        ans *= mat[n - 1][n - 1];
        return ans;
    }

    public GravityLagrangeInterpolation.Polynomial getCharacteristicPolynomial() {
        if (n != m) {
            throw new UnsupportedOperationException();
        }

        Matrix heisenberg = new Matrix(this);
        heisenberg.asTopHeisenbergMatrix();
        Matrix copy = new Matrix(n, m);

        GravityLagrangeInterpolation gli = new GravityLagrangeInterpolation(n + 1);
        for (int i = 0; i <= n; i++) {
            copy.asSame(heisenberg);
            for (int j = 0; j < n; j++) {
                copy.mat[j][j] = i - copy.mat[j][j];
            }
            gli.addPoint(i, copy.topHeisenbergMatrixDeterminant());
        }

        return gli.preparePolynomial();
    }
}
