package contest;

import template.binary.Bits;
import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.KahanSummation;
import template.utils.Debug;

import java.util.Arrays;

public class CGameWithStrings {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        String[] ss = new String[n];
        for (int i = 0; i < n; i++) {
            ss[i] = in.rs();
        }
        if (n == 1) {
            out.println(0);
            return;
        }

        int m = ss[0].length();
        int[] distinct = new int[1 << m];
        long[] different = new long[1 << m];
        long[] bitValue = new long[m];
        for (int pick = 0; pick < n; pick++) {
            Arrays.fill(bitValue, 0);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (ss[pick].charAt(j) != ss[i].charAt(j)) {
                        bitValue[j] |= 1L << i;
                    }
                }
            }
            Arrays.fill(different, 0);
            for (int i = 1; i < 1 << m; i++) {
                different[i] = different[i - Integer.lowestOneBit(i)] | bitValue[Log2.floorLog(Integer.lowestOneBit(i))];
            }
            long target = ((1L << n) - 1) ^ (1L << pick);
            for (int i = 0; i < 1 << m; i++) {
                if (different[i] == target) {
                    distinct[i]++;
                }
            }
        }

        debug.debug("distinct", distinct);
        //dp
        double[] dp = new double[1 << m];
        dp[0] = 1;
        double exp = 0;
        for (int i = 0; i < (1 << m) - 1; i++) {
            if(dp[i] <= 1e-12){
                continue;
            }
            int step = Integer.bitCount(i);
            int cand = m - step;
            double probTransfer = 1d / cand * dp[i];
            for (int j = 0; j < m; j++) {
                if (Bits.get(i, j) == 1) {
                    continue;
                }
                int to = Bits.set(i, j);
                double endProb = 0;
                if (n > distinct[i]) {
                    endProb = (double) (distinct[to] - distinct[i]) / (n - distinct[i]);
                    assert endProb >= 0;
                    assert endProb <= 1;
                    exp += endProb * probTransfer * (step + 1);
                }
                dp[to] += (1 - endProb) * probTransfer;
            }
        }
        out.println(exp);
    }
}
