package on2020_03.on2020_03_21_AtCoder_Grand_Contest_043.A___Range_Flip_Find_Route;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class ARangeFlipFindRoute {
    int[][][] dp;
    int[][] mat;
    int n;
    int m;

    public int dp(int i, int j, int s) {
        if (i >= n || j >= m) {
            return (int) 1e8;
        }

        if (mat[i][j] != s) {
            return dp(i, j, mat[i][j]) + mat[i][j];
        }
        if (dp[i][j][s] == -1) {
            if (i == n - 1 && j == m - 1) {
                return dp[i][j][s] = 0;
            }
            dp[i][j][s] = Math.min(dp(i + 1, j, s), dp(i, j + 1, s));
        }
        return dp[i][j][s];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();
        mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readChar() == '.' ? 0 : 1;
            }
        }

        dp = new int[n][m][2];
        SequenceUtils.deepFill(dp, -1);
        int ans = dp(0, 0, 0);
        out.println(ans);
    }
}
