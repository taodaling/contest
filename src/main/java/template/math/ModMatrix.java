package template.math;

import template.polynomial.ModGravityLagrangeInterpolation;
import template.primitve.generated.datastructure.Int2ToIntegerFunction;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class ModMatrix {
    int[] mat;
    int m;

    public ModMatrix(ModMatrix model) {
        m = model.m;
        mat = model.mat.clone();
    }

    public ModMatrix(int n, int m) {
        this.m = m;
        mat = new int[n * m];
    }

    public ModMatrix(int[] mat, int m) {
        assert mat.length % m == 0;
        this.mat = mat;
        this.m = m;
    }

    public ModMatrix(Int2ToIntegerFunction func, int n, int m) {
        this.mat = new int[n * m];
        this.m = m;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i * m + j] = func.apply(i, j);
            }
        }
    }

    public int getHeight() {
        return mat.length / m;
    }

    public int getWidth() {
        return m;
    }

    public boolean square() {
        return getHeight() == getWidth();
    }

    public void fill(int v) {
        Arrays.fill(mat, v);
    }

    public void asStandard(int mod) {
        fill(0);
        if (1 % mod == 0) {
            return;
        }
        for (int i = 0; i < mat.length; i += m + 1) {
            mat[i] = 1;
        }
    }

    public void set(int i, int j, int val) {
        mat[i * m + j] = val;
    }

    public int get(int i, int j) {
        return mat[i * m + j];
    }

    public void increment(int i, int j, int x, int mod) {
        int index = i * m + j;
        mat[index] = DigitUtils.modplus(mat[index], x, mod);
    }


    /**
     * <pre>
     * O(n^3)
     * |x|
     * </pre>
     */
    public static int determinant(ModMatrix x, int mod) {
        assert x.square();
        int n = x.getWidth();
        ModMatrix l = new ModMatrix(x);
        long ans = 1;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (l.mat[j * n + i] == 0) {
                    continue;
                }
                if (l.mat[i * n + i] == 0 || l.mat[i * n + i] > l.mat[j * n + i]) {
                    l.swapRow(i, j);
                    ans = -ans;
                }
                l.subtractRow(j, i, l.mat[j * n + i] / l.mat[i * n + i], mod);
                j--;
            }

            if (l.mat[i * n + i] == 0) {
                return 0;
            }
            ans = ans * l.mat[i * n + i] % mod;
        }

        return DigitUtils.mod(ans, mod);
    }

    /**
     * <pre>
     * O(n^3)
     * |x| while mod prime
     * </pre>
     */
    public static int determinant(ModMatrix x, Power power) {
        assert x.square();
        int mod = power.getMod();
        int n = x.m;
        ModMatrix l = new ModMatrix(x);
        long ans = 1;
        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int j = i; j < n; j++) {
                if (l.mat[j * n + i] != 0) {
                    maxRow = j;
                    break;
                }
            }

            if (l.mat[maxRow * n + i] == 0) {
                return 0;
            }
            if (i != maxRow) {
                l.swapRow(i, maxRow);
                ans = -ans;
            }
            ans = ans * l.mat[i * n + i] % mod;
            l.mulRow(i, power.inverse(l.mat[i * n + i]), mod);

            for (int j = i + 1; j < n; j++) {
                if (j == i) {
                    continue;
                }
                if (l.mat[j * n + i] == 0) {
                    continue;
                }
                int f = l.mat[j * n + i];
                l.subtractRow(j, i, f, mod);
            }
        }

        return (int) ans;
    }

    public static ModMatrix inverse(ModMatrix x, Power power) {
        assert x.square();
        int mod = power.getMod();
        int n = x.m;
        ModMatrix l = new ModMatrix(x);
        ModMatrix r = new ModMatrix(n, n);
        r.asStandard(mod);
        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int j = i; j < n; j++) {
                if (l.mat[j * n + i] != 0) {
                    maxRow = j;
                    break;
                }
            }

            if (l.mat[maxRow * n + i] == 0) {
                throw new RuntimeException("Can't inverse singular matrix");
            }
            r.swapRow(i, maxRow);
            l.swapRow(i, maxRow);

            int inv = power.inverse(l.mat[i * n + i]);
            r.mulRow(i, inv, mod);
            l.mulRow(i, inv, mod);

            for (int j = 0; j < n; j++) {
                if (j == i) {
                    continue;
                }
                if (l.mat[j * n + i] == 0) {
                    continue;
                }
                int f = l.mat[j * n + i];
                r.subtractRow(j, i, f, mod);
                l.subtractRow(j, i, f, mod);
            }
        }
        return r;
    }

    void swapRow(int i, int j) {
        for (int k = 0; k < m; k++) {
            SequenceUtils.swap(mat, i * m + k, j * m + k);
        }
    }

    void subtractRow(int i, int j, long f, int mod) {
        for (int k = 0; k < m; k++) {
            mat[i * m + k] = DigitUtils.mod(mat[i * m + k] - mat[j * m + k] * f, mod);
        }
    }

    void subtractCol(int i, int j, long f, int mod) {
        int h = getHeight();
        for (int k = 0; k < h; k++) {
            mat[k * m + i] = DigitUtils.mod(mat[k * m + i] - mat[k * m + j] * f, mod);
        }
    }

    void mulRow(int i, long f, int mod) {
        for (int k = 0; k < m; k++) {
            mat[i * m + k] = (int) (mat[i * m + k] * f % mod);
        }
    }

    public static ModMatrix mul(ModMatrix a, ModMatrix b, int mod) {
        assert a.getWidth() == b.getHeight();
        int h = a.getHeight();
        int mid = a.getWidth();
        int w = b.getWidth();
        ModMatrix c = new ModMatrix(h, w);

        for (int i = 0; i < h; i++) {
            for (int k = 0; k < mid; k++) {
                for (int j = 0; j < w; j++) {
                    c.mat[i * w + j] = DigitUtils.modplus(c.mat[i * w + j], (int) ((long) a.mat[i * mid + k] * b.mat[k * w + j] % mod), mod);
                }
            }
        }
        return c;
    }

    public static ModMatrix pow(ModMatrix x, long n, int mod) {
        if (n == 0) {
            assert x.square();
            ModMatrix r = new ModMatrix(x.getHeight(), x.getWidth());
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
        assert a.getHeight() == b.getHeight();
        assert a.getWidth() == b.getWidth();
        ModMatrix ans = new ModMatrix(a.getHeight(), a.getWidth());
        for (int i = 0; i < ans.mat.length; i++) {
            ans.mat[i] = DigitUtils.modplus(a.mat[i], b.mat[i], mod);
        }
        return ans;
    }

    public static ModMatrix subtract(ModMatrix a, ModMatrix b, int mod) {
        assert a.getHeight() == b.getHeight();
        assert a.getWidth() == b.getWidth();
        ModMatrix ans = new ModMatrix(a.getHeight(), a.getWidth());
        for (int i = 0; i < ans.mat.length; i++) {
            ans.mat[i] = DigitUtils.modsub(a.mat[i], b.mat[i], mod);
        }
        return ans;
    }

    public static ModMatrix mul(ModMatrix a, int k, int mod) {
        ModMatrix ans = new ModMatrix(a.getHeight(), a.getWidth());
        for (int i = 0; i < ans.mat.length; i++) {
            ans.mat[i] = (int) ((long) a.mat[i] * k % mod);
        }
        return ans;
    }

    public static ModMatrix transpose(ModMatrix x) {
        int n = x.getHeight();
        int m = x.getWidth();
        ModMatrix t = new ModMatrix(m, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                t.mat[j * n + i] = x.mat[i * m + j];
            }
        }
        return t;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                builder.append(mat[i * m + j]).append(' ');
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    public void asSame(ModMatrix matrix) {
        assert getHeight() == matrix.getHeight();
        assert getWidth() == matrix.getWidth();
        System.arraycopy(matrix.mat, 0, mat, 0, mat.length);
    }

    private void swapCol(int i, int j) {
        int h = getHeight();
        for (int k = 0; k < h; k++) {
            SequenceUtils.swap(mat, k * m + i, k * m + j);
        }
    }

    private void asTopHeisenbergModMatrix(Power pow) {
        assert square();
        int mod = pow.getMod();
        int n = m;
        for (int i = 0; i < n - 1; i++) {
            int maxRow = i + 1;
            for (int j = i + 1; j < n; j++) {
                if (mat[j * n + i] != 0) {
                    maxRow = j;
                    break;
                }
            }
            if (mat[maxRow * n + i] == 0) {
                continue;
            }
            if (maxRow != i + 1) {
                swapRow(maxRow, i + 1);
                swapCol(maxRow, i + 1);
            }
            long inv = pow.inverse(mat[maxRow * n + i]);
            for (int j = i + 2; j < n; j++) {
                if (mat[j * n + i] == 0) {
                    continue;
                }
                long c = mat[j * n + i] * inv % mod;
                subtractRow(j, i + 1, c, mod);
                subtractCol(i + 1, j, DigitUtils.negate((int) c, mod), mod);
            }
        }
    }

    private int topHeisenbergModMatrixDeterminant(Power pow) {
        assert square();
        int mod = pow.getMod();
        long ans = 1 % mod;
        int n = m;

        for (int i = 0; i < n - 1; i++) {
            if (mat[i * n + i] == 0 && mat[(i + 1) * n + i] != 0) {
                swapRow(i, i + 1);
                ans = -ans;
            }
            if (mat[(i + 1) * n + i] != 0) {
                subtractRow(i + 1, i, (long) mat[(i + 1) * n + i] * pow.inverse(mat[i * n + i]) % mod, mod);
            }
            ans = ans * mat[i * n + i] % mod;
        }

        ans = mat[(n - 1) * n + n - 1] * ans % mod;
        return (int) ans;
    }

    /**
     * <p>Get the minimal-polynomial of this, O(n^3)</p>
     */
    public ModGravityLagrangeInterpolation.Polynomial getCharacteristicPolynomial(Power pow) {
        assert square();
        int mod = pow.getMod();

        ModMatrix heisenberg = new ModMatrix(this);
        heisenberg.asTopHeisenbergModMatrix(pow);
        ModMatrix copy = new ModMatrix(m, m);

        ModGravityLagrangeInterpolation gli = new ModGravityLagrangeInterpolation(mod, m + 1);
        for (int i = 0; i <= m; i++) {
            copy.asSame(heisenberg);
            for (int j = 0; j < m; j++) {
                copy.mat[j * m + j] = DigitUtils.modsub(copy.mat[j * m + j], i, mod);
            }
            int y = copy.topHeisenbergModMatrixDeterminant(pow);
            if (m % 2 == 1) {
                y = DigitUtils.modsub(0, y, mod);
            }
            gli.addPoint(i, y);
        }
        return gli.preparePolynomial();
    }
}
