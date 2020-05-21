package on2020_05.on2020_05_21_Codeforces___Codeforces_Round__415__Div__1_.C__Find_a_car;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerVersionArray;
import template.utils.Debug;

public class CFindACar {
    Modular mod = new Modular(1e9 + 7);

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.readInt();

        int highestBit = 29;
        for (int i = 0; i < q; i++) {
            int x1 = in.readInt();
            int y1 = in.readInt();
            int x2 = in.readInt();
            int y2 = in.readInt();
            int k = in.readInt() - 1;
            int a11 = dfs(highestBit, x2, y2, k, 0)[0];
            int a01 = dfs(highestBit, x2, y1 - 1, k, 0)[0];
            int a10 = dfs(highestBit, x1 - 1, y2, k, 0)[0];
            int a00 = dfs(highestBit, x1 - 1, y1 - 1, k, 0)[0];

            int ans = mod.plus(a11, a00);
            ans = mod.subtract(ans, a01);
            ans = mod.subtract(ans, a10);
            out.println(ans);
        }

        debug.debug("c1", c1);
        debug.debug("c2", c2);
    }

    public int sum(int n) {
        //1 + ... + n
        return mod.valueOf((long) (n + 1) * n / 2);
    }

    int c1 = 0;
    int c2 = 0;
    int[][] mem1 = new int[30][2];
    int[][] mem2 = new int[30][2];

    public int[] dfs(int bit, int n, int m, int k, int trace) {
        if (n > m) {
            int tmp = n;
            n = m;
            m = tmp;
        }
        if (n == 0 || m == 0 || trace > k) {
            return new int[2];
        }
        //full
        //
        int size = 1 << (bit + 1);
        if (n == size && m == size) {
            int[] ans = new int[2];
            int allow = Math.min((trace | (size - 1)), k) - trace;
            int cnt = mod.mul(size, allow + 1);
            ans[0] = cnt;
            ans[1] = mod.mul(sum(allow), size);
            ans[1] = mod.plus(ans[1], mod.mul(cnt, (trace + 1)));
            return ans;
        }

        if (n == size || m == size) {
            c1++;
        } else {
            c2++;
        }

        int[] nSub = mem1[bit];
        nSub[0] = Math.min(n, size / 2);
        nSub[1] = n - nSub[0];
        int[] mSub = mem2[bit];
        mSub[0] = Math.min(m, size / 2);
        mSub[1] = m - mSub[0];

        //00 or 11
        int[] ans = new int[2];

        if (m < size || (trace | (size - 1)) > k) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    int[] ret = dfs(bit - 1, nSub[i], mSub[j], k, Bits.setBit(trace, bit, (i ^ j) == 1));
                    ans[0] = mod.plus(ans[0], ret[0]);
                    ans[1] = mod.plus(ans[1], ret[1]);
                }
            }
        }else{
            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < 2; j++) {
                    int[] ret = dfs(bit - 1, nSub[i], mSub[j], k, Bits.setBit(trace, bit, (i ^ j) == 1));
                    ans[0] = mod.plus(ans[0], ret[0]);
                    ans[1] = mod.plus(ans[1], ret[1]);
                }
            }
            int[] ret = dfs(bit - 1, nSub[1], mSub[0], k, Bits.setBit(trace, bit, true));
            ans[0] = mod.plus(ans[0], ret[0]);
            ans[0] = mod.plus(ans[0], ret[0]);
            ans[1] = mod.plus(ans[1], ret[1]);
            ans[1] = mod.plus(ans[1], ret[1]);
            ans[1] = mod.plus(ans[1], mod.mul(ret[0], 1 << bit));
        }

        return ans;
    }

    public void printMat(int[][] mat, FastOutput out) {
        for (int i = 0; i < mat.length; i++) {
            for (int x : mat[i]) {
                out.append(x).append(' ');
            }
            out.println();
        }
    }

    public int[][] gen(int n, int m) {
        int[][] mat = new int[n][m];
        IntegerVersionArray iva = new IntegerVersionArray(1000);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                iva.clear();
                for (int k = 0; k < i; k++) {
                    iva.set(mat[k][j], 1);
                }
                for (int k = 0; k < j; k++) {
                    iva.set(mat[i][k], 1);
                }
                mat[i][j] = 0;
                while (iva.get(mat[i][j]) == 1) {
                    mat[i][j]++;
                }
            }
        }
        return mat;
    }
}
