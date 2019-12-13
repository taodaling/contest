package on2019_12.on2019_12_11_.AntiprimeNumbers;



import template.io.FastInput;
import template.io.FastOutput;

import java.math.BigInteger;
import java.util.BitSet;

public class AntiprimeNumbers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.println(countAntiPrimes(in.readString(), in.readInt()));
    }
    public int countAntiPrimes(String N, int D) {
        Modular mod = new Modular(1e9 + 7);
        int[][] matArray = new int[4][4];
        if (D == 8) {
            matArray[0][0]++;
        }

        matArray[1][0] = 1;
        if (D == 8) {
            matArray[1][1] = 1;
        }
        if (D >= 4) {
            matArray[2][0]++;
            matArray[2][2]++;
        }
        if (D >= 6) {
            matArray[2][0]++;
            matArray[2][2]++;
        }
        if (D >= 8) {
            matArray[2][2]++;
        }

        if (D >= 4) {
            matArray[3][1]++;
            matArray[3][3]++;
        }
        if (D >= 6) {
            matArray[3][1]++;
            matArray[3][3]++;
        }
        if (D >= 8) {
            matArray[3][3]++;
        }

        String s = new BigInteger(N).toString(2);
        BitSet bs = new BitSet(s.length());
        for (int i = 0; i < s.length(); i++) {
            bs.set(i, '1' == s.charAt(s.length() - i - 1));
        }
        ModMatrix mat = new ModMatrix(matArray);
        ModMatrix trans = ModMatrix.pow(mat, bs, 0, mod);
        ModMatrix vec = new ModMatrix(4, 1);
        vec.set(0, 0, 1);

        ModMatrix finalState = ModMatrix.mul(trans, vec, mod);
        int ans = 0;
        for(int i = 0; i < 4; i++){
            ans = mod.plus(ans, finalState.get(i, 0));
        }

        return ans;
    }
    public static class ModMatrix {
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

        public static ModMatrix pow(ModMatrix x, BitSet n, int i, Modular modular) {
            if (i == n.size()) {
                ModMatrix r = new ModMatrix(x.n, x.m);
                r.asStandard(modular);
                return r;
            }
            ModMatrix r = pow(x, n, i + 1, modular);
            r = ModMatrix.mul(r, r, modular);
            if (n.get(i)) {
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
    public static class Modular {
        int m;

        public int getMod() {
            return m;
        }

        public Modular(int m) {
            this.m = m;
        }

        public Modular(long m) {
            this.m = (int) m;
            if (this.m != m) {
                throw new IllegalArgumentException();
            }
        }

        public Modular(double m) {
            this.m = (int) m;
            if (this.m != m) {
                throw new IllegalArgumentException();
            }
        }

        public int valueOf(int x) {
            x %= m;
            if (x < 0) {
                x += m;
            }
            return x;
        }

        public int valueOf(long x) {
            x %= m;
            if (x < 0) {
                x += m;
            }
            return (int) x;
        }

        public int mul(int x, int y) {
            return valueOf((long) x * y);
        }

        public int mul(long x, long y) {
            return valueOf(x * y);
        }

        public int plus(int x, int y) {
            return valueOf(x + y);
        }

        public int plus(long x, long y) {
            return valueOf(x + y);
        }

        public int subtract(int x, int y) {
            return valueOf(x - y);
        }

        public int subtract(long x, long y) {
            return valueOf(x - y);
        }

        @Override
        public String toString() {
            return "mod " + m;
        }
    }
}
