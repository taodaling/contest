package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class ElevatorRides {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        x = in.readInt();
        w = new int[n];
        in.populate(w);
        dp = new int[1 << n];
        dp2 = new long[1 << n];
        SequenceUtils.deepFill(dp, -1);
        dp(dp.length - 1);
        out.println(dp[dp.length - 1]);
    }

    int n;
    int x;
    int[] w;
    int[] dp;
    long[] dp2;

    public void dp(int s) {
        if (dp[s] == -1) {
            if (s == 0) {
                dp[s] = 0;
                dp2[s] = x;
                return;
            }
            dp[s] = (int) 1e9;
            for (int i = 0; i < n; i++) {
                if (Bits.get(s, i) == 0) {
                    continue;
                }
                int sub = Bits.clear(s, i);
                dp(sub);
                int size = dp[sub];
                long used = dp2[sub];
                if (used + w[i] > x) {
                    used = w[i];
                    size++;
                } else {
                    used += w[i];
                }
                if (size < dp[s]) {
                    dp[s] = size;
                    dp2[s] = x;
                }
                if (dp[s] == size && dp2[s] > used) {
                    dp2[s] = used;
                }
            }
        }
    }

}
