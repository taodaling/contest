package contest;

import template.io.FastInput;
import template.primitve.generated.datastructure.LongComparator;
import template.primitve.generated.datastructure.LongMinQueue;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.io.PrintWriter;

public class G2LuckyNumbersHardVersion {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int k = in.readInt();
        long[] f = new long[6];
        in.populate(f);
        int limit = (int) 1e6;
        long[][] dp = new long[7][limit + 1];
        long inf = (long) 1e18;
        SequenceUtils.deepFill(dp, -inf);
        dp[0][0] = 0;
        LongMinQueue queue = new LongMinQueue(limit + 1, LongComparator.REVERSE_ORDER);

        for (int i = 1, base = 1; i <= 6; i++, base *= 10) {
            long profit = f[i - 1];
            int each = base * 3;
            int threshold = (k - 1) * 3 + 1;
            for (int j = 0; j < each; j++) {
                queue.reset();
                for (int t = j, x = 0; t <= limit; t += each, x++) {
                    queue.addLast(dp[i - 1][t] - x * profit);
                    if (threshold < queue.size()) {
                        queue.removeFirst();
                    }
                    dp[i][t] = queue.min() + x * profit;
                }
            }

            for (int j = limit; j >= 0; j--) {
                for (int t = 0; t <= 9; t++) {
                    if (j - t * base < 0) {
                        continue;
                    }
                    long cand = dp[i][j - t * base];
                    if (t % 3 == 0) {
                        cand += t / 3 * profit;
                    }
                    dp[i][j] = Math.max(dp[i][j], cand);
                }
            }
        }

        debug.debug("dp", dp);
        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int n = in.readInt();
            long ans = dp[6][n];
            out.println(ans);
        }
    }
}
