package on2021_07.on2021_07_18_Codeforces___Codeforces_Round__733__Div__1___Div__2__based_on_VK_Cup_2021___Elimination__Engine__.F__Bingo;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Power;
import template.polynomial.FastWalshHadamardTransform;

import java.util.Arrays;

public class FBingo {
    int mod = 31607;
    Power pow = new Power(mod);

    int[] prob;
    int[] dp;

    public void consider(int i, int state, int p) {
        if (i < 0) {
            dp[state] = p;
            return;
        }
        consider(i - 1, state | (1 << i), p * prob[i] % mod);
        consider(i - 1, state, p * (1 - prob[i]) % mod);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] prob = new int[n][n];
        int inv10000 = pow.inverse(10000);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                prob[i][j] = in.ri() * inv10000 % mod;
            }
        }
        int[] state = new int[1 << n + 2];
        int[] sum = new int[1 << n + 2];
        Arrays.fill(sum, 1);
        dp = new int[1 << n];
        for (int j = 0; j < n; j++) {
            this.prob = prob[j];
            consider(n - 1, 0, 1);
            dp[dp.length - 1] = 0;
            FastWalshHadamardTransform.andFWT(dp, 0, dp.length - 1, mod);
            int l = j;
            int r = n - 1 - j;
            int m1 = ((1 << l) | (1 << n));
            int m2 = ((1 << r) | (1 << n + 1));
            int mask = (1 << n) - 1;
            for (int i = 0; i < 1 << n + 2; i++) {
                int actual = i;
                if (Bits.get(i, l) != Bits.get(i, n)) {
                    actual |= m1;
                }
                if (Bits.get(i, r) != Bits.get(i, n + 1)) {
                    actual |= m2;
                }
                state[i] = dp[actual & mask];
            }
            for (int i = 0; i < 1 << n + 2; i++) {
                sum[i] = sum[i] * state[i] % mod;
            }
        }

        FastWalshHadamardTransform.andIFWT(sum, 0, sum.length - 1, mod);
        int ans = 1 - sum[0];
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }

}
