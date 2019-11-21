package contest;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;

public class TaskB {
    Modular mod = new Modular(1e9 + 7);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        int[] bits = new int[64];
        BitOperator bo = new BitOperator();
        for (int i = 0; i < 64; i++) {
            bits[i] = bo.bitAt(n, i);
        }

        long[][] dp = new long[64][3];
        // 0 equal, 1 less 1, 2 less more than 1
        dp[63][0] = 1;
        for (int i = 62; i >= 0; i--) {
            if (bits[i] == 1) {
                //a put and b put
                dp[i][0] = dp[i + 1][0];
                //b not put
                //b and a put
                dp[i][1] += dp[i + 1][0] + dp[i + 1][1];
                //b put or not
                //b and a put or not
                dp[i][2] += dp[i + 1][1] * 2 + dp[i + 1][2] * 3;
            }else{
                //a not put and b not put
                //a put and b put
                dp[i][0] += dp[i + 1][0] + dp[i + 1][1];
                //b put
                dp[i][1] += dp[i + 1][1];
                //b and a not put
                dp[i][2] += dp[i + 1][1] + dp[i + 1][2] * 3;
            }

            for(int j = 0; j < 3; j++){
                dp[i][j] = mod.valueOf(dp[i][j]);
            }
        }

        long ans = mod.valueOf(dp[0][0] + dp[0][1] + dp[0][2]);
        out.println(ans);
    }
}
