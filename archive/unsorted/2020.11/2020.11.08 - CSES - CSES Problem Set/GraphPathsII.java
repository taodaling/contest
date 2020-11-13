package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class GraphPathsII {
    long inf = (long) 2e18;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        long[][] mat = new long[n][n];
        SequenceUtils.deepFill(mat, inf);
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            int c = in.readInt();
            mat[u][v] = Math.min(mat[u][v], c);
        }
        long[][] ans = pow(mat, k);
        if (ans[0][n - 1] >= inf) {
            out.println(-1);
            return;
        }
        out.println(ans[0][n - 1]);
    }

    public long[][] mul(long[][] a, long[][] b) {
        int n = a.length;
        long[][] ans = new long[n][n];
        SequenceUtils.deepFill(ans, inf);
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                for (int j = 0; j < n; j++) {
                    ans[i][j] = Math.min(ans[i][j],
                            a[i][k] + b[k][j]);
                }
            }
        }
        return ans;
    }

    public long[][] pow(long[][] x, long n) {
        if (n == 0) {
            long[][] ans = new long[x.length][x.length];
            SequenceUtils.deepFill(ans, inf);
            for (int i = 0; i < x.length; i++) {
                ans[i][i] = 0;
            }
            return ans;
        }
        long[][] ans = pow(x, n / 2);
        ans = mul(ans, ans);
        if (n % 2 == 1) {
            ans = mul(ans, x);
        }
        return ans;
    }
}
