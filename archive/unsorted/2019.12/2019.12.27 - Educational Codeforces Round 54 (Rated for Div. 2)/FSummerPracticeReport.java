package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Arrays;

public class FSummerPracticeReport {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] x = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            x[i] = in.readInt();
        }
        int[] y = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            y[i] = in.readInt();
        }

        int inf = k + 1;
        int[][] dp = new int[n + 1][2];
        for (int i = 1; i <= n; i++) {
            int[] ab = new int[]{x[i], y[i]};
            dp[i][0] = inf;
            dp[i][1] = inf;
            for (int j = 0; j < 2; j++) {
                for (int t = 0; t < 2; t++) {
                    if (dp[i - 1][t] > k) {
                        continue;
                    }
                    long atLeast;
                    long atMost;
                    if (j == t && ab[1 - j] == 0) {
                        if (ab[j] + dp[i - 1][t] > k) {
                            continue;
                        }
                        dp[i][j] = Math.min(dp[i][j], dp[i - 1][t] + ab[j]);
                        continue;
                    }

                    if (j == t) {
                        atLeast = DigitUtils.ceilDiv(ab[1 - j], k) - 1;
                        atMost = k - dp[i - 1][t] + (long) (ab[1 - j] - 1) * k;
                    } else {
                        if (k - dp[i - 1][t] >= ab[1 - j]) {
                            atLeast = 0;
                        } else {
                            atLeast = DigitUtils.ceilDiv(ab[1 - j] - (k - dp[i - 1][t]), k);
                        }
                        atMost = (long) ab[1 - j] * k;
                    }
                    if (atLeast + 1 > ab[j]) {
                        continue;
                    }
                    long remain = Math.max(1, ab[j] - atMost);
                    dp[i][j] = (int) Math.min(remain, dp[i][j]);
                }
            }
        }

        //System.err.println(Arrays.deepToString(dp));
        if(dp[n][0] <= k || dp[n][1] <= k){
            out.println("YES");
        }else{
            out.println("NO");
        }
    }
}
