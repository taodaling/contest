package contest;

import template.FastInput;
import template.FastOutput;
import template.PreSum;
import template.PreXor;
import template.SequenceUtils;

import java.util.Arrays;

public class XORPartitioning {
    Modular mod = new Modular(1e9 + 7);
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }

        PreXor pre = new PreXor(a);
        int[] zeroCnts = new int[n + 1];
        for(int i = 1; i <= n; i++){
            if(pre.intervalSum(1, i) == 0){
                zeroCnts[i] = 1;
            }
        }
        PreSum zeroPreSum = new PreSum(zeroCnts);
        int[] registries = new int[1 << 20];
        SequenceUtils.deepFill(registries, -1);

        int[] last = new int[n + 1];
        int[] lastSkip = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int p = (int) pre.intervalSum(1, i);
            last[i] = registries[p];
            if (registries[0] < last[i]) {
                lastSkip[i] = lastSkip[last[i]];
            } else {
                lastSkip[i] = last[i];
            }
            registries[p] = i;
        }

        int[][] dp = new int[2][1 << 20];
        Arrays.fill(dp[0], 1);
        for (int i = 1; i <= n; i++) {
            int p = (int) pre.intervalSum(1, i);
            if (last[i] == -1) {
                dp[1][p] = 1;
                continue;
            }
            if (last[i] == lastSkip[i]) {
                int zeroBetween = (int)zeroPreSum.intervalSum(last[i], i);
                dp[0][p] = mod.plus(dp[0][p], mod.mul(dp[1][p], zeroBetween));
            }
            dp[1][p] = mod.plus(dp[1][p], dp[0][p]);
        }

        if (pre.intervalSum(1, n) != 0) {
            int ans = dp[0][(int)pre.intervalSum(1, n)];
            out.println(ans);
            return;
        }

        int ans = 0;
        for(int i = 1; i < (1 << 20); i++){
            ans = mod.plus(ans, dp[1][i]);
        }
        int zeroCnt = (int)zeroPreSum.intervalSum(1, n - 1);
        ans = mod.plus(ans, pow.pow(2, zeroCnt));
        out.println(ans);
    }
}
