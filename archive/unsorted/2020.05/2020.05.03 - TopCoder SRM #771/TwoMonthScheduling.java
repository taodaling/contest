package contest;

import template.primitve.generated.datastructure.LongPreSum;
import template.utils.Debug;

public class TwoMonthScheduling {

    //Debug debug = new Debug(true);

    public int minimumMonths(int workers, int[] firstMonth0, int[] firstMonth1, int[] secondMonth0, int[] secondMonth1) {
        int L0 = firstMonth0.length;
        int L1 = firstMonth1.length;
        int n = L1 * L0;
        long[] firstMonth = new long[n];
        long[] secondMonth = new long[n];
        for (int i1 = 0; i1 <= L1 - 1; i1++) {
            for (int i0 = 0; i0 <= L0 - 1; i0++) {
                firstMonth[i1 * L0 + i0] = Math.min(workers, firstMonth0[i0] ^ firstMonth1[i1]);
                secondMonth[i1 * L0 + i0] = Math.min(workers, secondMonth0[i0] ^ secondMonth1[i1]);
            }
        }

        long[] A = firstMonth;
        long[] B = secondMonth;
        int W = workers;
        LongPreSum psA = new LongPreSum(A);
        LongPreSum psB = new LongPreSum(B);

        int inf = (int) 1e8;
        int[][] dp = new int[n][n];
        for (int l = 0; l < n; l++) {
            if (l == 0) {
                for (int r = 0; r < n; r++) {
                    if (psA.prefix(r) <= W) {
                        dp[l][r] = 1;
                    } else {
                        dp[l][r] = inf;
                    }
                }
                continue;
            }

            int min = inf;
            int prev = l;
            long sum = 0;
            for (int r = n - 1; r >= l; r--) {
                long s = psA.intervalSum(l, r);
                while (prev - 1 >= 0 && sum + B[prev - 1] + s <= W) {
                    sum += B[prev - 1];
                    min = Math.min(min, dp[prev - 1][l - 1]);
                    prev--;
                }
                dp[l][r] = min + 1;
            }

            while (prev - 1 >= 0) {
                min = Math.min(min, dp[prev - 1][l - 1]);
                prev--;
            }
            for (int r = n - 1; r >= l; r--) {
                if (psA.intervalSum(l, r) > W) {
                    continue;
                }
                dp[l][r] = Math.min(dp[l][r], min + 2);
            }
        }

        int ans = inf;
        for (int l = 0; l < n; l++) {
            if (psB.intervalSum(l, n - 1) <= W) {
                ans = Math.min(ans, dp[l][n - 1]);
            }
        }

//        debug.debug("A", A);
//        debug.debug("B", B);
//        debug.debug("dp", dp);


        return ans + 1;
    }
}
