package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class DTopSecretTask {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int s = in.ri();
        int[] qs = new int[n];
        in.populate(qs);
        s = Math.min(s, n * (n - 1) / 2);
        int[][] left;
        {
            int[] v = new int[k];
            int[] w = new int[k];
            for (int i = 0; i < k; i++) {
                v[i] = -qs[i];
                w[i] = k - i;
            }
            left = dp(v, w, s);
        }
        int[][] right;
        {
            int[] v = new int[n - k];
            int[] w = new int[n - k];
            for (int i = 0; i < n - k; i++) {
                v[i] = qs[i + k];
                w[i] = i;
            }
            right = dp(v, w, s);
        }
        long dec = 0;
        for (int i = 0; i < Math.min(left.length, right.length); i++) {
            for (int j = 0; j <= s; j++) {
                long v = left[i][j] + right[i][s - j];
                dec = Math.min(dec, v);
            }
        }
        long sum = 0;
        for (int i = 0; i < k; i++) {
            sum += qs[i];
        }
        sum += dec;
        out.println(sum);
    }

    int inf = (int) 4e8;

    public int[][] dp(int[] v, int[] w, int s) {
        int n = v.length;
        int[][] dp = new int[n + 1][s + 1];
        SequenceUtils.deepFill(dp, inf);
        dp[0][0] = 0;
        for (int i = 0; i < n; i++) {
            for (int j = n - 1; j >= 0; j--) {
                for (int k = 0; k + w[i] <= s; k++) {
                    dp[j + 1][k + w[i]] = Math.min(dp[j + 1][k + w[i]], dp[j][k] + v[i]);
                }
            }
        }
        for (int i = 0; i <= n; i++) {
            for (int j = 1; j <= s; j++) {
                dp[i][j] = Math.min(dp[i][j], dp[i][j - 1]);
            }
        }
        return dp;
    }
}
