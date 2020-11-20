package contest;

import template.algo.MaximumContinuousSubsequence;
import template.io.FastInput;
import template.io.FastOutput;

public class SashaVilkin {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] s = new long[n];
        in.populate(s);
        long[][] suffix = new long[2][n + 1];
        for (int i = 0; i < 2; i++) {
            for (int j = n - 1; j >= 0; j--) {
                long v = s[j];
                if (j % 2 != i) {
                    v = -v;
                }
                suffix[i][j] = Math.max(0, v + suffix[i][j + 1]);
            }
        }

        long ans = 0;
        for (int i = 0; i < n; i++) {
            ans = Math.max(ans, s[i] + suffix[i % 2][i + 1]);
        }

        out.println(ans);
    }
}
