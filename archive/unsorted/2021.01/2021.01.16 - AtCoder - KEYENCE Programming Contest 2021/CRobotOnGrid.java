package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.primitve.generated.datastructure.IntegerPreSum2D;
import template.utils.SequenceUtils;

public class CRobotOnGrid {
    int mod = 998244353;
    int[][] dp;
    int[][] mat;
    int h;
    int w;
    IntegerPreSum2D ps;
    long[] p3;

    public int dp(int i, int j) {
        if (i >= h || j >= w) {
            return 0;
        }
        if (dp[i][j] == -1) {
            long sum = 0;
            long a = dp(i + 1, j) * p3[ps.rect(i, i, j + 1, w - 1)] % mod;
            long b = dp(i, j + 1) * p3[ps.rect(i + 1, h - 1, j, j)] % mod;
            if (mat[i][j] == -1 || mat[i][j] == 'D') {
                sum += a;
            }
            if (mat[i][j] == -1 || mat[i][j] == 'R') {
                sum += b;
            }
            if (mat[i][j] == -1 || mat[i][j] == 'X') {
                sum += a;
                sum += b;
            }
            while (sum >= mod) {
                sum -= mod;
            }
            dp[i][j] = (int) sum;
        }
        return dp[i][j];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        h = in.ri();
        w = in.ri();
        int k = in.ri();
        dp = new int[h][w];
        mat = new int[h][w];
        p3 = new long[5001];
        p3[0] = 1;
        for (int i = 1; i <= 5000; i++) {
            p3[i] = p3[i - 1] * 3 % mod;
        }
        SequenceUtils.deepFill(dp, -1);
        SequenceUtils.deepFill(mat, -1);
        for (int i = 0; i < k; i++) {
            int x = in.ri() - 1;
            int y = in.ri() - 1;
            int c = in.rc();
            mat[x][y] = c;
        }
        ps = new IntegerPreSum2D(h, w);
        ps.init((i, j) -> mat[i][j] == -1 ? 1 : 0, h, w);

        dp[h - 1][w - 1] = mat[h - 1][w - 1] == -1 ? 3 : 1;
        int ans = dp(0, 0);
        out.println(ans);
    }
}
