package template.math;

import template.polynomial.ModGravityLagrangeInterpolation;

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

    public void asStandard(int mod) {
        fill(0);
        if (mod == 1) {
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
    public static int determinant(ModMatrix x, int mod) {
        if (x.n != x.m) {
            throw new RuntimeException("ModMatrix is not square");
        }
        int n = x.n;
        ModMatrix l = new ModMatrix(x);
        long ans = 1;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (l.mat[j][i] % mod == 0) {
                    continue;
                }
                if (l.mat[i][i] == 0 || l.mat[i][i] > l.mat[j][i]) {
                    l.swapRow(i, j);
                    ans = -ans;
                }
                l.subtractRow(j, i, l.mat[j][i] / l.mat[i][i], mod);
                j--;
            }

            if (l.mat[i][i] == 0) {
                return 0;
            }
            ans = ans * l.mat[i][i] % mod;
        }

        return (int) ans;
    }

    /**
     * |x| while mod prime
     */
    public static int determinant(ModMatrix x, Power power) {
        if (x.n != x.m) {
            throw new RuntimeException("ModMatrix is not square");
        }
        int mod = power.getMod();
        int n = x.n;
        ModMatrix l = new ModMatrix(x);
        long ans = 1;
        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int j = i; j < n; j++) {
                if (l.mat[j][i] % mod != 0) {
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
            ans = ans * l.mat[i][i] % mod;
            l.mulRow(i, power.inverseByFermat(l.mat[i][i]), mod);

            for (int j = i + 1; j < n; j++) {
                if (j == i) {
                    continue;
                }
                if (l.mat[j][i] == 0) {
                    continue;
                }
                int f = l.mat[j][i];
                l.subtractRow(j, i, f, mod);
            }
        }

        return (int) ans;
    }

    public static ModMatrix inverse(ModMatrix x, Power power) {
        if (x.n != x.m) {
            throw new RuntimeException("ModMatrix is not square");
        }
        int mod = power.getMod();
        int n = x.n;
        ModMatrix l = new ModMatrix(x);
        ModMatrix r = new ModMatrix(n, n);
        r.asStandard(mod);
        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int j = i; j < n; j++) {
                if (l.mat[j][i] % mod != 0) {
                    maxRow = j;
                    break;
                }
            }

            if (l.mat[maxRow][i] == 0) {
                throw new RuntimeException("Can't inverse current matrix");
            }
            r.swapRow(i, maxRow);
            l.swapRow(i, maxRow);

            int inv = power.inverseByFermat(l.mat[i][i]);
            r.mulRow(i, inv, mod);
            l.mulRow(i, inv, mod);

            for (int j = 0; j < n; j++) {
                if (j == i) {
                    continue;
                }
                if (l.mat[j][i] == 0) {
                    continue;
                }
                int f = l.mat[j][i];
                r.subtractRow(j, i, f, mod);
                l.subtractRow(j, i, f, mod);
            }
        }
        return r;
    }

    void swapRow(int i, int j) {
        int[] row = mat[i];
        mat[i] = mat[j];
        mat[j] = row;
    }

    void subtractRow(int i, int j, long f, int mod) {
        for (int k = 0; k < m; k++) {
            mat[i][k] = DigitUtils.mod(mat[i][k] - mat[j][k] * f, mod);
        }
    }

    void subtractCol(int i, int j, long f, int mod) {
        for (int k = 0; k < n; k++) {
            mat[k][i] = DigitUtils.mod(mat[k][i] - mat[k][j] * f, mod);
        }
    }

    void mulRow(int i, long f, int mod) {
        for (int k = 0; k < m; k++) {
            mat[i][k] = (int) (mat[i][k] * f % mod);
        }
    }

    public static ModMatrix mul(ModMatrix a, ModMatrix b, int mod) {
        ModMatrix c = new ModMatrix(a.n, b.m);
        for (int i = 0; i < c.n; i++) {
            for (int j = 0; j < c.m; j++) {
                long sum = 0;
                for (int k = 0; k < a.m; k++) {
                    sum += (long) a.mat[i][k] * b.mat[k][j] % mod;
                }
                c.mat[i][j] = (int) (sum % mod);
            }
        }
        return c;
    }

    public static ModMatrix pow(ModMatrix x, long n, int mod) {
        if (n == 0) {
            ModMatrix r = new ModMatrix(x.n, x.m);
            r.asStandard(mod);
            return r;
        }
        ModMatrix r = pow(x, n >> 1, mod);
        r = ModMatrix.mul(r, r, mod);
        if (n % 2 == 1) {
            r = ModMatrix.mul(r, x, mod);
        }
        return r;
    }

    public static ModMatrix plus(ModMatrix a, ModMatrix b, int mod) {
        if (a.n != b.n || a.m != b.m) {
            throw new IllegalArgumentException();
        }
        ModMatrix ans = new ModMatrix(a.n, a.m);
        for (int i = 0; i < a.n; i++) {
            for (int j = 0; j < a.m; j++) {
                ans.mat[i][j] = DigitUtils.modplus(a.mat[i][j], b.mat[i][j], mod);
            }
        }
        return ans;
    }

    public static ModMatrix subtract(ModMatrix a, ModMatrix b, int mod) {
        if (a.n != b.n || a.m != b.m) {
            throw new IllegalArgumentException();
        }
        ModMatrix ans = new ModMatrix(a.n, a.m);
        for (int i = 0; i < a.n; i++) {
            for (int j = 0; j < a.m; j++) {
                ans.mat[i][j] = DigitUtils.modsub(a.mat[i][j], b.mat[i][j], mod);
            }
        }
        return ans;
    }

    public static ModMatrix mul(ModMatrix a, int k, int mod) {
        ModMatrix ans = new ModMatrix(a.n, a.m);
        for (int i = 0; i < a.n; i++) {
            for (int j = 0; j < a.m; j++) {
                ans.mat[i][j] = (int) ((a.mat[i][j] * (long) k) % mod);
            }
        }
        return ans;
    }

    public static ModMatrix transpose(ModMatrix x) {
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
            System.arraycopy(matrix.mat[i], 0, mat[i], 0, m);
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
        int mod = pow.getMod();

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
            long inv = pow.inverseByFermat(mat[maxRow][i]);
            for (int j = i + 2; j < n; j++) {
                if (mat[j][i] == 0) {
                    continue;
                }
                long c = mat[j][i] * inv % mod;
                subtractRow(j, i + 1, c, mod);
                subtractCol(i + 1, j, DigitUtils.negate((int) c, mod), mod);
            }
        }
    }

    private int topHeisenbergModMatrixDeterminant(Power pow) {
        int mod = pow.getMod();
        long ans = 1 % mod;

        for (int i = 0; i < n - 1; i++) {
            if (mat[i][i] == 0 && mat[i + 1][i] != 0) {
                swapRow(i, i + 1);
                ans = -ans;
            }
            if (mat[i + 1][i] != 0) {
                subtractRow(i + 1, i, (long) mat[i + 1][i] * pow.inverseByFermat(mat[i][i]) % mod, mod);
            }
            ans = ans * mat[i][i] % mod;
        }

        ans = mat[n - 1][n - 1] * ans % mod;
        return (int) ans;
    }

    /**
     * <p>Get the minimal-polynomial of this, O(n^3)</p>
     */
    public ModGravityLagrangeInterpolation.Polynomial getCharacteristicPolynomial(Power pow) {
        int mod = pow.getMod();
        if (n != m) {
            throw new UnsupportedOperationException();
        }

        ModMatrix heisenberg = new ModMatrix(this);
        heisenberg.asTopHeisenbergModMatrix(pow);
        ModMatrix copy = new ModMatrix(n, m);

        ModGravityLagrangeInterpolation gli = new ModGravityLagrangeInterpolation(mod, n + 1);
        for (int i = 0; i <= n; i++) {
            copy.asSame(heisenberg);
            for (int j = 0; j < n; j++) {
                copy.mat[j][j] = DigitUtils.modsub(copy.mat[j][j], i, mod);
            }
            int y = copy.topHeisenbergModMatrixDeterminant(pow);
            if (n % 2 == 1) {
                y = DigitUtils.modsub(0, y, mod);
            }
            gli.addPoint(i, y);
        }
        return gli.preparePolynomial();
    }
}
