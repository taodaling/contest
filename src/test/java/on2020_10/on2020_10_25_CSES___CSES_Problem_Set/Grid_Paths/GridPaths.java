package on2020_10.on2020_10_25_CSES___CSES_Problem_Set.Grid_Paths;



import template.io.FastInput;
import template.math.DigitUtils;

import java.io.PrintWriter;

public class GridPaths {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        char[][] mat = new char[n][n];
        for (int i = 0; i < n; i++) {
            in.readString(mat[i], 0);
        }
        int mod = (int) 1e9 + 7;
        int[][] dp = new int[n][n];
        dp[0][0] = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(mat[i][j] == '*'){
                    dp[i][j] = 0;
                }
                if (i + 1 < n) {
                    dp[i + 1][j] = DigitUtils.modplus(dp[i + 1][j], dp[i][j], mod);
                }
                if (j + 1 < n) {
                    dp[i][j + 1] = DigitUtils.modplus(dp[i][j + 1], dp[i][j], mod);
                }
            }
        }
        out.println(dp[n - 1][n - 1]);
    }
}
