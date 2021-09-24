package on2021_08.on2021_08_22_AtCoder___AtCoder_Regular_Contest_125.D___Unique_Subsequence;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongBIT;
import template.utils.Debug;

import java.util.Arrays;

public class DUniqueSubsequence {
    int mod = 998244353;
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        for (int i = 0; i < n; i++) {
            a[i]--;
        }
        int[] reg = new int[n];
        Arrays.fill(reg, -1);
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) {
            int x = a[i];
            prev[i] = reg[x];
            reg[x] = i;
        }
        debug.debug("prev", prev);
        LongBIT bit = new LongBIT(n);
        long[] dp = new long[n];
        for (int i = 0; i < n; i++) {
            long sum = bit.query(prev[i] + 1, i);
            if (prev[i] == -1) {
                sum++;
            }
            sum %= mod;
            dp[i] = sum;
            if (prev[i] != -1) {
                bit.update(prev[i] + 1, -dp[prev[i]]);
            }
            bit.update(i + 1, dp[i]);
        }
        debug.debug("dp", dp);
        long ans = 0;
        for (int i = 0; i < n; i++) {
            int x = a[i];
            if (reg[x] == i) {
                ans += dp[i];
            }
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
