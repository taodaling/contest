package on2021_06.on2021_06_26_AtCoder___AtCoder_Beginner_Contest_203_Sponsored_by_Panasonic_.F___Weed;



import template.algo.BinarySearch;
import template.algo.WQSBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.function.IntPredicate;

public class FWeed {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        a = in.ri(n);
        Randomized.shuffle(a);
        Arrays.sort(a);
        go = new int[n];
        for (int i = 0, pt = 0; i < n; i++) {
            while (a[pt] * 2 <= a[i]) {
                pt++;
            }
            go[i] = pt - 1;
        }

        int maxStep = 40;
        long[][] dp = new long[maxStep][n + 1];
        long inf = (long) 1e18;
        SequenceUtils.deepFill(dp, inf);
        dp[0][n] = 0;
        for (int i = n; i > 0; i--) {
            for (int j = 0; j < maxStep; j++) {
                //move
                dp[j][i - 1] = Math.min(dp[j][i - 1], dp[j][i] + 1);
                //jump
                if (j + 1 < maxStep) {
                    dp[j + 1][go[i - 1] + 1] = Math.min(dp[j + 1][go[i - 1] + 1],
                            dp[j][i]);
                }
            }
        }
        int minStep = 0;
        while (dp[minStep][0] > k) {
            minStep++;
        }
        out.println(minStep);
        out.println(dp[minStep][0]);
    }

    int[] go;
    int[] a;

}
