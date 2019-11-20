package on2019_11.on2019_11_20_Codeforces_Global_Round_1.D___Jongmah;



import template.ArrayIndex;
import template.FastInput;
import template.FastOutput;
import template.SequenceUtils;

import java.util.Arrays;

public class TaskD {
    int[] a;
    int[] dp;
    int inf = (int) 1e8;
    ArrayIndex ai;

    public int dp(int i, int j, int k) {
        if (j < 0 || j >= 3 || k < 0 || k >= 5) {
            return -inf;
        }
        if (i == 0) {
            return j == 0 && k == 0 ? 0 : -inf;
        }
        int index = ai.indexOf(i, j, k);
        if (dp[index] == -1) {
            int num = a[i];
            if (k > num) {
                return dp[index] = -inf;
            }
            dp[index] = Math.max(dp(i, j + 1, k), dp(i, j, k + 1));
            for (int used = 0; used <= 2 && used + k <= num; used++) {
                for (int t = 0; t < 3; t++) {
                    dp[index] = Math.max(dp[index], dp(i - 1, t + used, j + used) + used + (num - used - k) / 3);
                }
            }
        }
        return dp[index];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        a = new int[m + 1];
        for (int i = 1; i <= n; i++) {
            a[in.readInt()]++;
        }
        dp = new int[(m + 1) * 3 * 5];
        ai = new ArrayIndex(m + 1, 3, 5);
        SequenceUtils.deepFill(dp, -1);

        // System.out.println(Arrays.deepToString(dp));
//        for (int i = 1; i <= m; i++) {
//            for (int j = 0; j < 3; j++) {
//                for (int k = 0; k < 5; k++) {
//                    dp(i, j, k);
//                }
//            }
//        }
        int ans = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                ans = Math.max(ans, dp(m, i, j));
            }
        }

        out.println(ans);
    }
}
