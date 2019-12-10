package template.math;

import template.polynomial.GravityModLagrangeInterpolation;

public class ModMatrix {
    int[][] mat;
    int n;
    int m;

    public ModMatrix(ModMatrix model) {
        n = model.n;
        m = model.m;
        mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = model.mat[i][j];
            }
        }
    }

    public ModMatrix(int n, int m) {
        this.n = n;
        this.m = m;
        mat = new int[n][m];
    }

    public ModMatrix(int[][] mat) {
        if (mat.length == 0 || mat[0].length == 0) {
            throw new IllegalArgumentException();
        }
        this.n = mat.length;
        this.m = mat[0].length;
        this.mat = mat;
    }

    public void fill(int v) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = v;
            }
        }
    }

    public ModMatrix getTransposeMatrix() {
        ModMatrix mat = new ModMatrix(m, n);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                mat.mat[j][i] = this.mat[i][j];
            }
        }
        return mat;
    }

    public void asStandard(Modular mod) {
        fill(0);
        if (mod.getMod() == 1) {
            return;
        }
        for (int i = 0; i < n && i < m; i++) {
            mat[i][i] = 1;
        }
    }

    public void set(int i, int j, int val) {
        mat[i][j] = val;
    }

    public int get(int i, int j) {
        return mat[i][j];
    }

    public void normalize(Modular mod) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = mod.valueOf(mat[i][j]);
            }
        }
    }

    public static ModMatrix region(ModMatrix x, int b, int t, int l, int r) {
        ModMatrix y = new ModMatrix(t - b + 1, r - l + 1);
        for (int i = b; i <= t; i++) {
            for (int j = l; j <= r; j++) {
                y.mat[i - b][j - l] = x.mat[i][j];
            }
        }
        return y;
    }

    /**
     * |x| while mod a non-zero number
     */
    public static int determinant(ModMatrix x, Modular modular) {
        if (x.n != x.m) {
            throw new RuntimeException("ModMatrix is not square");
        }
        int n = x.n;
        ModMatrix l = new ModMatrix(x);
        l.normalize(modular);
        int ans = 1;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (modular.valueOf(l.mat[j][i]) == 0) {
                    continue;
                }
                if (l.mat[i][i] == 0 || l.mat[i][i] > l.mat[j][i]) {
                    l.swapRow(i, j);
                    ans = -ans;
                }
                l.subtractRow(j, i, l.mat[j][i] / l.mat[i][i], modular);
                j--;
            }

            if (l.mat[i][i] == 0) {
                return 0;
            }
            ans = modular.mul(ans, l.mat[i][i]);
        }

        return ans;
    }

    /**
     * |x| while mod prime
     */
    public static int determinant(ModMatrix x, Power power) {
        if (x.n != x.m) {
            throw new RuntimeException("ModMatrix is not square");
        }
        Modular modular = power.getModular();
        int n = x.n;
        ModMatrix l = new ModMatrix(x);
        l.normalize(modular);
        int ans = 1;
        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int j = i; j < n; j++) {
                if (modular.valueOf(l.mat[j][i]) != 0) {
                    maxRow = j;
                    break;
                }
            }

            if (l.mat[maxRow][i] == 0) {
                return 0;
            }
            if (i != maxRow) {
                l.swapRow(i, maxRow);
                ans = -ans;
            }
            ans = modular.mul(ans, l.mat[i][i]);
            l.mulRow(i, power.inverse(l.mat[i][i]), modular);

            for (int j = i + 1; j < n; j++) {
                if (j == i) {
                    continue;
                }
                if (l.mat[j][i] == 0) {
                    continue;
                }
                int f = l.mat[j][i];
                l.subtractRow(j, i, f, modular);
            }
        }

        return ans;
    }

    public static ModMatrix inverse(ModMatrix x, Power power) {
        if (x.n != x.m) {
            throw new RuntimeException("ModMatrix is not square");
        }
        Modular modular = power.getModular();
        int n = x.n;
        ModMatrix l = new ModMatrix(x);
        l.normalize(modular);
        ModMatrix r = new ModMatrix(n, n);
        r.asStandard(modular);
        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int j = i; j < n; j++) {
                if (modular.valueOf(l.mat[j][i]) != 0) {
                    maxRow = j;
                    break;
                }
            }

            if (l.mat[maxRow][i] == 0) {
                throw new RuntimeException("Can't inverse current matrix");
            }
            r.swapRow(i, maxRow);
            l.swapRow(i, maxRow);

            int inv = power.inverse(l.mat[i][i]);
            r.mulRow(i, inv, modular);
            l.mulRow(i, inv, modular);

            for (int j = 0; j < n; j++) {
                if (j == i) {
                    continue;
                }
                if (l.mat[j][i] == 0) {
                    continue;
                }
                int f = l.mat[j][i];
                r.subtractRow(j, i, f, modular);
                l.subtractRow(j, i, f, modular);
            }
        }
        return r;
    }

    void swapRow(int i, int j) {
        int[] row = mat[i];
        mat[i] = mat[j];
        mat[j] = row;
    }

    void subtractRow(int i, int j, int f, Modular modular) {
        for (int k = 0; k < m; k++) {
            mat[i][k] = modular.subtract(mat[i][k], modular.mul(mat[j][k], f));
        }
    }

    void subtractCol(int i, int j, int f, Modular modular) {
        for (int k = 0; k < n; k++) {
            mat[k][i] = modular.subtract(mat[k][i], modular.mul(mat[k][j], f));
        }
    }

    void mulRow(int i, int f, Modular modular) {
        for (int k = 0; k < m; k++) {
            mat[i][k] = modular.mul(mat[i][k], f);
        }
    }

    public static ModMatrix mul(ModMatrix a, ModMatrix b, Modular modular) {
        ModMatrix c = new ModMatrix(a.n, b.m);
        for (int i = 0; i < c.n; i++) {
            for (int j = 0; j < c.m; j++) {
                for (int k = 0; k < a.m; k++) {
                    c.mat[i][j] = modular.plus(c.mat[i][j], modular.mul(a.mat[i][k], b.mat[k][j]));
                }
            }
        }
        return c;
    }

    public static ModMatrix pow(ModMatrix x, long n, Modular modular) {
        if (n == 0) {
            ModMatrix r = new ModMatrix(x.n, x.m);
            r.asStandard(modular);
            return r;
        }
        ModMatrix r = pow(x, n >> 1, modular);
        r = ModMatrix.mul(r, r, modular);
        if (n % 2 == 1) {
            r = ModMatrix.mul(r, x, modular);
        }
        return r;
    }

    public static ModMatrix plus(ModMatrix a, ModMatrix b, Modular mod) {
        if (a.n != b.n || a.m != b.m) {
            throw new IllegalArgumentException();
        }
        ModMatrix ans = new ModMatrix(a.n, a.m);
        for (int i = 0; i < a.n; i++) {
            for (int j = 0; j < a.m; j++) {
                ans.mat[i][j] = mod.plus(a.mat[i][j], b.mat[i][j]);
            }
        }
        return ans;
    }

    public static ModMatrix subtract(ModMatrix a, ModMatrix b, Modular mod) {
        if (a.n != b.n || a.m != b.m) {
            throw new IllegalArgumentException();
        }
        ModMatrix ans = new ModMatrix(a.n, a.m);
        for (int i = 0; i < a.n; i++) {
            for (int j = 0; j < a.m; j++) {
                ans.mat[i][j] = mod.subtract(a.mat[i][j], b.mat[i][j]);
            }
        }
        return ans;
    }

    public static ModMatrix mul(ModMatrix a, int k, Modular mod) {
        ModMatrix ans = new ModMatrix(a.n, a.m);
        for (int i = 0; i < a.n; i++) {
            for (int j = 0; j < a.m; j++) {
                ans.mat[i][j] = mod.mul(a.mat[i][j], k);
            }
        }
        return ans;
    }

    public static ModMatrix transposition(ModMatrix x) {
        int n = x.n;
        int m = x.m;
        ModMatrix t = new ModMatrix(m, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                t.mat[j][i] = x.mat[i][j];
            }
        }
        return t;
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

    public void asSame(ModMatrix matrix) {
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
            int tmp = mat[k][i];
            mat[k][i] = mat[k][j];
            mat[k][j] = tmp;
        }
    }

    private void asTopHeisenbergModMatrix(Power pow) {
        Modular mod = pow.getModular();

        for (int i = 0; i < n - 1; i++) {
            int maxRow = i + 1;
            for (int j = i + 1; j < n; j++) {
                if (mat[j][i] != 0) {
                    maxRow = j;
                    break;
                }
            }
            if (mat[maxRow][i] == 0) {
                continue;
            }
            if (maxRow != i + 1) {
                swapRow(maxRow, i + 1);
                swapCol(maxRow, i + 1);
            }
            int inv = pow.inverse(mat[maxRow][i]);
            for (int j = i + 2; j < n; j++) {
                if (mat[j][i] == 0) {
                    continue;
                }
                int c = mod.mul(mat[j][i], inv);
                subtractRow(j, i + 1, c, mod);
                subtractCol(i + 1, j, mod.valueOf(-c), mod);
            }
        }
    }

    private int topHeisenbergModMatrixDeterminant(Power pow) {
        Modular mod = pow.getModular();
        int ans = mod.valueOf(1);

        for (int i = 0; i < n - 1; i++) {
            if (mat[i][i] == 0 && mat[i + 1][i] != 0) {
                swapRow(i, i + 1);
                ans = -ans;
            }
            if (mat[i + 1][i] != 0) {
                subtractRow(i + 1, i, mod.mul(mat[i + 1][i], pow.inverse(mat[i][i])), mod);
            }
            ans = mod.mul(ans, mat[i][i]);
        }

        ans = mod.mul(mat[n - 1][n - 1], ans);
        return ans;
    }

    public GravityModLagrangeInterpolation.Polynomial getCharacteristicPolynomial(Power pow) {
        Modular mod = pow.getModular();
        if (n != m) {
            throw new UnsupportedOperationException();
        }

        ModMatrix heisenberg = new ModMatrix(this);
        heisenberg.asTopHeisenbergModMatrix(pow);
        ModMatrix copy = new ModMatrix(n, m);

        GravityModLagrangeInterpolation gli = new GravityModLagrangeInterpolation(mod, n + 1);
        for (int i = 0; i <= n; i++) {
            copy.asSame(heisenberg);
            for (int j = 0; j < n; j++) {
                copy.mat[j][j] = mod.subtract(copy.mat[j][j], i);
            }
            int y = copy.topHeisenbergModMatrixDeterminant(pow);
            if (n % 2 == 1) {
                y = mod.valueOf(-y);
            }
            gli.addPoint(i, y);
        }

        return gli.preparePolynomial();
    }
}
