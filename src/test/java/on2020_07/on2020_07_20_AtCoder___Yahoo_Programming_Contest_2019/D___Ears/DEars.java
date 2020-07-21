package on2020_07.on2020_07_20_AtCoder___Yahoo_Programming_Contest_2019.D___Ears;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class DEars {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] A = new long[n];
        in.populate(A);

        long[][] props = new long[3][n];
        for (int i = 0; i < n; i++) {
            props[0][i] = A[i];
            props[1][i] = A[i] == 0 ? 1 : (A[i] - 1) % 2;
            props[2][i] = A[i] < 2 ? 2 - A[i] : (A[i] - 2) % 2;
        }

        //02120
        int[] pick = new int[]{0, 2, 1, 2, 0};
        long inf = (long) 1e18;
        long[][] dp = new long[5][n + 1];
        SequenceUtils.deepFill(dp, inf);
        dp[0][0] = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 5; j++) {
                for (int t = j; t < 5; t++) {
                    dp[t][i + 1] = Math.min(dp[t][i + 1],
                            dp[j][i] + props[pick[t]][i]);
                }
            }
        }

        long ans = inf;
        for (int i = 0; i < 5; i++) {
            ans = Math.min(ans, dp[i][n]);
        }

        out.println(ans);
    }
}
