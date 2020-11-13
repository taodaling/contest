package contest;

import com.sun.org.apache.xpath.internal.operations.Mod;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModMatrix;
import template.math.ModSparseMatrix;
import template.math.ModVectorLinearRecurrenceSolver;
import template.polynomial.IntPolyFFT;

public class GraphPathsI {
    int mod = (int) 1e9 + 7;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        int[][] mat = new int[n][n];
        for (int i = 0; i < m; i++) {
            mat[in.readInt() - 1][in.readInt() - 1]++;
        }
        int[][] ans = pow(mat, k);
        out.println(ans[0][n - 1]);
    }

    public int[][] mul(int[][] a, int[][] b) {
        int n = a.length;
        int m = b[0].length;
        int mid = b.length;
        int[][] ans = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < mid; k++) {
                for (int j = 0; j < m; j++) {
                    ans[i][j] += (long) a[i][k] * b[k][j] % mod;
                    if (ans[i][j] >= mod) {
                        ans[i][j] -= mod;
                    }
                }
            }
        }
        return ans;
    }

    public int[][] pow(int[][] x, int n) {
        if (n == 0) {
            int[][] ans = new int[x.length][x.length];
            for (int i = 0; i < x.length; i++) {
                ans[i][i] = 1;
            }
            return ans;
        }
        int[][] ans = pow(x, n / 2);
        ans = mul(ans, ans);
        if (n % 2 == 1) {
            ans = mul(ans, x);
        }
        return ans;
    }
}
