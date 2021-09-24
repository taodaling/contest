package on2021_07.on2021_07_21_DMOJ___Wesley_s_Anger_Contest_3.Problem_5___Triangle__The_Root_of_All_Evil;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.Comparator;

public class Problem5TriangleTheRootOfAllEvil {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Line[] lines = new Line[n + 1];
        for (int i = 0; i < n; i++) {
            lines[i] = new Line(in.ri(), in.ri());
        }
        lines[n] = new Line(-(int) 1e9 - 10, 0);
        Arrays.sort(lines, Comparator.comparingInt(x -> x.l));
        long[][] dp = new long[n + 1][n + 1];
        long inf = (long) 1e18;
        SequenceUtils.deepFill(dp, -inf);
        dp[0][0] = 0;
        int[] scan = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                while (scan[j] < j && lines[j].l + lines[scan[j] + 1].l <= lines[i].l) {
                    scan[j]++;
                }
                dp[i][j] = dp[j][scan[j]] + lines[i].v;
            }
            for (int j = 1; j <= i; j++) {
                dp[i][j] = Math.max(dp[i][j], dp[i][j - 1]);
            }
        }

        long best = 0;
        for (int i = 0; i <= n; i++) {
            best = Math.max(best, dp[i][i]);
        }

        long sum = Arrays.stream(lines).mapToLong(x -> x.v).sum();
        long ans = sum - best;
        out.println(ans);
    }
}

class Line {
    int l;
    int v;

    public Line(int l, int v) {
        this.l = l;
        this.v = v;
    }
}
