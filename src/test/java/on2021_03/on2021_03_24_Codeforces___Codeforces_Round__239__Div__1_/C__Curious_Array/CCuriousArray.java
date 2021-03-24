package on2021_03.on2021_03_24_Codeforces___Codeforces_Round__239__Div__1_.C__Curious_Array;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;

public class CCuriousArray {
    int mod = (int) 1e9 + 7;
    Combination comb = new Combination((int) 2e5, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = in.ri(n);
        int lim = 100;
        long[][] mat = new long[lim + 1][n];
        for (int i = 0; i < m; i++) {
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            int k = in.ri();
            mat[k][l]++;
            if (r + 1 < n) {
                for (int j = k; j >= 0; j--) {
                    mat[j][r + 1] -= comb.combination(r - l + k - j, k - j);
                }
            }
        }
        for (int i = lim; i >= 0; i--) {
            for (int j = 0; j < n; j++) {
                if (i < lim) {
                    mat[i][j] += mat[i + 1][j];
                }
                if (j > 0) {
                    mat[i][j] += mat[i][j - 1];
                }
                mat[i][j] %= mod;
            }
        }

        for (int i = 0; i < n; i++) {
            long ans = mat[0][i] + a[i];
            if (ans >= mod) {
                ans -= mod;
            }
            if (ans < 0) {
                ans += mod;
            }
            out.append(ans).append(' ');
        }
    }
}
