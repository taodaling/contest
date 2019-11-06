package template;

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

    public void asStandard(NumberTheory.Modular mod) {
        fill(0);
        if(mod.getMod() == 1){
            return;
        }
        for (int i = 0; i < n && i < m; i++) {
            mat[i][i] = 1;
        }
    }

    public void set(int i, int j, int val) {
        mat[i][j] = val;
    }

    public void normalize(NumberTheory.Modular mod) {
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
    public static int determinant(ModMatrix x, NumberTheory.Modular modular) {
        if (x.n != x.m) {
            throw new RuntimeException("Matrix is not square");
        }
        int n = x.n;
        ModMatrix l = new ModMatrix(x);
        l.normalize(modular);
        int ans = 1;
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
    public static int determinant(ModMatrix x, NumberTheory.Power power) {
        if (x.n != x.m) {
            throw new RuntimeException("Matrix is not square");
        }
        NumberTheory.Modular modular = power.getModular();
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

    public static ModMatrix inverse(ModMatrix x, NumberTheory.Power power) {
        if (x.n != x.m) {
            throw new RuntimeException("Matrix is not square");
        }
        NumberTheory.Modular modular = power.getModular();
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

    void subtractRow(int i, int j, int f, NumberTheory.Modular modular) {
        for (int k = 0; k < m; k++) {
            mat[i][k] = modular.subtract(mat[i][k], modular.mul(mat[j][k], f));
        }
    }

    void mulRow(int i, int f, NumberTheory.Modular modular) {
        for (int k = 0; k < m; k++) {
            mat[i][k] = modular.mul(mat[i][k], f);
        }
    }

    public static ModMatrix mul(ModMatrix a, ModMatrix b, NumberTheory.Modular modular) {
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

    public static ModMatrix pow(ModMatrix x, long n, NumberTheory.Modular modular) {
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

    static ModMatrix transposition(ModMatrix x, NumberTheory.Modular modular) {
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
}