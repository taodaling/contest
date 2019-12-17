package template.math;

public class LongModMatrix {
    long[][] mat;
    int n;
    int m;

    public LongModMatrix(LongModMatrix model) {
        n = model.n;
        m = model.m;
        mat = new long[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = model.mat[i][j];
            }
        }
    }

    public LongModMatrix(int n, int m) {
        this.n = n;
        this.m = m;
        mat = new long[n][m];
    }

    public LongModMatrix(long[][] mat) {
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

    public void asStandard() {
        fill(0);
        for (int i = 0; i < n && i < m; i++) {
            mat[i][i] = 1;
        }
    }

    public void set(int i, int j, int val) {
        mat[i][j] = val;
    }

    public void normalize(ILongModular mod) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = mod.valueOf(mat[i][j]);
            }
        }
    }


    public static LongModMatrix region(LongModMatrix x, int b, int t, int l, int r) {
        LongModMatrix y = new LongModMatrix(t - b + 1, r - l + 1);
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
    public static long determinant(LongModMatrix x, LongModular modular) {
        if (x.n != x.m) {
            throw new RuntimeException("Matrix is not square");
        }
        int n = x.n;
        LongModMatrix l = new LongModMatrix(x);
        l.normalize(modular);
        long ans = 1;
        for (int i = 0; i < n; i++) {
            int maxRow = i;
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
    public static long determinant(LongModMatrix x, LongPower power) {
        if (x.n != x.m) {
            throw new RuntimeException("Matrix is not square");
        }
        ILongModular modular = power.getModular();
        int n = x.n;
        LongModMatrix l = new LongModMatrix(x);
        l.normalize(modular);
        long ans = 1;
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
                long f = l.mat[j][i];
                l.subtractRow(j, i, f, modular);
            }
        }

        return ans;
    }

    public static LongModMatrix inverse(LongModMatrix x, LongPower power) {
        if (x.n != x.m) {
            throw new RuntimeException("Matrix is not square");
        }
        ILongModular modular = power.getModular();
        int n = x.n;
        LongModMatrix l = new LongModMatrix(x);
        l.normalize(modular);
        LongModMatrix r = new LongModMatrix(n, n);
        r.asStandard();
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

            long inv = power.inverse(l.mat[i][i]);
            r.mulRow(i, inv, modular);
            l.mulRow(i, inv, modular);

            for (int j = 0; j < n; j++) {
                if (j == i) {
                    continue;
                }
                if (l.mat[j][i] == 0) {
                    continue;
                }
                long f = l.mat[j][i];
                r.subtractRow(j, i, f, modular);
                l.subtractRow(j, i, f, modular);
            }
        }
        return r;
    }

    void swapRow(int i, int j) {
        long[] row = mat[i];
        mat[i] = mat[j];
        mat[j] = row;
    }

    void subtractRow(int i, int j, long f, ILongModular modular) {
        for (int k = 0; k < m; k++) {
            mat[i][k] = modular.subtract(mat[i][k], modular.mul(mat[j][k], f));
        }
    }

    void mulRow(int i, long f, ILongModular modular) {
        for (int k = 0; k < m; k++) {
            mat[i][k] = modular.mul(mat[i][k], f);
        }
    }

    public static LongModMatrix mul(LongModMatrix a, LongModMatrix b, ILongModular modular) {
        LongModMatrix c = new LongModMatrix(a.n, b.m);
        for (int i = 0; i < c.n; i++) {
            for (int j = 0; j < c.m; j++) {
                for (int k = 0; k < a.m; k++) {
                    c.mat[i][j] = modular.plus(c.mat[i][j], modular.mul(a.mat[i][k], b.mat[k][j]));
                }
            }
        }
        return c;
    }

    public static LongModMatrix pow(LongModMatrix x, long n, ILongModular modular) {
        if (n == 0) {
            LongModMatrix r = new LongModMatrix(x.n, x.m);
            r.asStandard();
            return r;
        }
        LongModMatrix r = pow(x, n >> 1, modular);
        r = LongModMatrix.mul(r, r, modular);
        if (n % 2 == 1) {
            r = LongModMatrix.mul(r, x, modular);
        }
        return r;
    }

    static LongModMatrix transposition(LongModMatrix x, LongModular modular) {
        int n = x.n;
        int m = x.m;
        LongModMatrix t = new LongModMatrix(m, n);
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
}