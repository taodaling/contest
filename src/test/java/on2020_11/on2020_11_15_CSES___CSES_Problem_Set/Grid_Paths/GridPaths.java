package on2020_11.on2020_11_15_CSES___CSES_Problem_Set.Grid_Paths;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;

import java.util.Arrays;

public class GridPaths {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int mod = (int) 1e9 + 7;
        Combination comb = new Combination((int) 2e6, mod);
        int[][] trap = new int[m + 2][2];
        for (int i = 0; i < m; i++) {
            trap[i][0] = in.readInt();
            trap[i][1] = in.readInt();
            if(trap[i][0] == trap[i][1] && (trap[i][0] == 1 || trap[i][0] == n)){
                out.println(0);
                return;
            }
        }
        trap[m][0] = 1;
        trap[m][1] = 1;
        trap[m + 1][0] = n;
        trap[m + 1][1] = n;
        Arrays.sort(trap, (a, b) -> a[0] == b[0] ? Integer.compare(a[1], b[1]) : Integer.compare(a[0], b[0]));
        long[][] dp = new long[m + 2][2];
        dp[0][1] = 1;
        for (int i = 0; i < m + 2; i++) {
            for (int j = 0; j < 2; j++) {
                dp[i][j] %= mod;
            }
            for (int j = i + 1; j < m + 2; j++) {
                if (trap[i][0] <= trap[j][0] && trap[i][1] <= trap[j][1]) {
                    long way = comb.combination(trap[j][0] - trap[i][0] + trap[j][1] - trap[i][1],
                            trap[j][0] - trap[i][0]);
                    for (int k = 0; k < 2; k++) {
                        dp[j][k ^ 1] += dp[i][k] * way % mod;
                    }
                }
            }
        }

        long ans = DigitUtils.mod(dp[m + 1][0] - dp[m + 1][1], mod);
        out.println(ans);
    }
}
