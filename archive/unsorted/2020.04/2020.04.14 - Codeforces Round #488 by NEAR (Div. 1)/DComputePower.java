package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.primitve.generated.datastructure.LongPreSum;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.math.BigDecimal;
import java.util.Arrays;

public class DComputePower {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Task[] tasks = new Task[n + 1];
        for (int i = 1; i <= n; i++) {
            tasks[i] = new Task();
            tasks[i].a = in.readInt();
        }

        int bSum = 0;
        for (int i = 1; i <= n; i++) {
            tasks[i].b = in.readInt();
            bSum += tasks[i].b;
        }

        Arrays.sort(tasks, 1, n + 1, (a, b) -> a.a == b.a ? -Long.compare(a.b, b.b) : -Long.compare(a.a, b.a));
        long[][][] dp = new long[n + 1][n + 1][bSum + 1];
        long inf = (long) 1e15;
        SequenceUtils.deepFill(dp, (long) inf);
        dp[0][0][0] = 0;
        for (int i = 1; i <= n; i++) {
            int r = i;
            while (r + 1 <= n && tasks[r + 1].a == tasks[i].a) {
                r++;
            }
            int m = r - i + 1;
            long[] prefixA = new long[m + 1];
            int[] prefixB = new int[m + 1];
            for (int j = 0; j < m; j++) {
                prefixA[j + 1] = tasks[i + j].a;
                prefixB[j + 1] = tasks[i + j].b;
            }
            LongPreSum aPs = new LongPreSum(prefixA);
            IntegerPreSum bPs = new IntegerPreSum(prefixB);

            for (int t = 0; t <= m; t++) {
                int frontCnt = t;
                int backCnt = m - t;
                long frontA = aPs.prefix(t);
                int frontB = bPs.prefix(t);

                for (int j = 0; j <= n; j++) {
                    for (int k = 0; k <= bSum; k++) {
                        // add as first
                        if (j >= backCnt && j - backCnt + frontCnt <= n && k + frontB <= bSum) {
                            dp[r][j - backCnt + frontCnt][k + frontB] = Math.min(dp[r][j - backCnt + frontCnt][k + frontB], dp[i - 1][j][k] + frontA);
                        }
                    }
                }
            }
        }


        debug.debug("dp", dp);
        long min = inf;
        for (int i = 0; i <= n; i++) {
            for (int j = 1; j <= bSum; j++) {
                min = Math.min(min, DigitUtils.ceilDiv(dp[n][i][j] * 1000, j));
            }
        }

        out.println(min);
    }
}

class Task {
    long a;
    int b;
}