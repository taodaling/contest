package on2020_01.on2020_01_05_Codeforces_Round__612__Div__1_.A__Garland;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class AGarland {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] parity = new int[n + 1];

        int[] unknown = {n / 2, n - n / 2};
        for (int i = 1; i <= n; i++) {
            int x = in.readInt();
            parity[i] = x;
            if (parity[i] != 0) {
                unknown[parity[i] % 2]--;
            }
        }

        int[][][] dp = new int[n + 1][n + 1][2];
        SequenceUtils.deepFill(dp, (int) 1e8);
        dp[0][0][0] = dp[0][0][1] = 0;
        for (int i = 1; i <= n; i++) {
            int x = parity[i];
            for (int j = 0; j <= n; j++) {
                for (int k = 0; k < 2; k++) {
                    if (x == 0) {
                        if (j > 0 && k == 0) {
                            dp[i][j][k] = Math.min(dp[i][j][k], dp[i - 1][j - 1][k]);
                            dp[i][j][k] = Math.min(dp[i][j][k], dp[i - 1][j - 1][1 - k] + 1);
                        }
                        if(k == 1) {
                            dp[i][j][k] = Math.min(dp[i - 1][j][k], dp[i][j][k]);
                            dp[i][j][k] = Math.min(dp[i - 1][j][1 - k] + 1, dp[i][j][k]);
                        }
                    } else {
                        if(x % 2 != k){
                            continue;
                        }
                        dp[i][j][k] = Math.min(dp[i - 1][j][k], dp[i][j][k]);
                        dp[i][j][k] = Math.min(dp[i - 1][j][1 - k] + 1, dp[i][j][k]);
                    }
                }
            }
        }

        int ans = Math.min(dp[n][unknown[0]][0], dp[n][unknown[0]][1]);
        out.println(ans);
    }
}
