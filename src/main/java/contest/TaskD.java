package contest;

import template.ArrayUtils;
import template.FastInput;
import template.FastOutput;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] vals = new int[n + 1];
        vals[1] = in.readInt();
        for (int i = 2; i <= n; i++) {
            char c = in.readChar();
            vals[i] = in.readInt();
            if (c == '-') {
                vals[i] = -vals[i];
            }
        }

        long[][] dp = new long[n + 1][3];
        ArrayUtils.deepFill(dp, (long) -1e18);
        dp[0][0] = 0;
        int[] sign = new int[]{1, -1, 1};
        for (int i = 1; i <= n; i++) {
            for (int j = 2; j >= 0; j--) {
                if(j < 2){
                    dp[i][j] = Math.max(dp[i][j], dp[i][j + 1]);
                }
                dp[i][j] = Math.max(dp[i][j], dp[i - 1][j] + sign[j] * vals[i]);
                if(vals[i] < 0 && j > 0){
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - 1] +
                            sign[j - 1] * vals[i]);
                }
            }
        }

        out.println(dp[n][0]);
    }
}
