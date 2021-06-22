package on2021_06.on2021_06_20_Codeforces___Codeforces_LATOKEN_Round_1__Div__1___Div__2_.Bank_Security_Unification;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class BankSecurityUnification {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int L = 50;
        int[] prev = new int[L];
        Arrays.fill(prev, -1);
        int n = in.ri();
        long[] a = in.rl(n);
        long[] dp = new long[n + 1];
        dp[0] = 0;
        for (int i = 1; i <= n; i++) {
            long v = a[i - 1];
            dp[i] = Math.max(dp[i], dp[i - 1]);
            for (int j = 0; j < L; j++) {
                if (prev[j] < 0) {
                    continue;
                }
                dp[i] = Math.max(dp[i], dp[prev[j]] + (v & a[prev[j] - 1]));
            }
            for (int j = 0; j < L; j++) {
                if (Bits.get(v, j) == 1) {
                    prev[j] = i;
                }
            }
        }

        long ans = dp[n];
        out.println(ans);
    }
}
