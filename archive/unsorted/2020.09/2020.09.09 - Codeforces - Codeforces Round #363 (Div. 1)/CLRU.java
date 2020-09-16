package contest;

import template.binary.Bits;
import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;

public class CLRU {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();

        double[] p = new double[n];
        in.populate(p);
        int nonZeroCnt = 0;
        for (int i = 0; i < n; i++) {
            if (p[i] != 0) {
                nonZeroCnt++;
            }
        }
        if (nonZeroCnt <= k) {
            for (int i = 0; i < n; i++) {
                out.println(p[i] == 0 ? 0 : 1);
            }
            return;
        }


        double[] subset = new double[1 << n];
        for (int i = 1; i < 1 << n; i++) {
            int log = Log2.floorLog(i);
            subset[i] = subset[i - (1 << log)] + p[log];
        }

        double[] dp = new double[1 << n];
        dp[0] = 1;
        for (int i = 1; i < 1 << n; i++) {
            double sum = 0;
            boolean possible = true;
            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 1 && p[j] == 0) {
                    possible = false;
                }
            }
            if (!possible) {
                dp[i] = 0;
                continue;
            }
            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 0) {
                    continue;
                }
                sum += p[j] / (1 - subset[i - (1 << j)]) * dp[i - (1 << j)];
            }
            dp[i] = sum;
        }

        double[] ans = new double[n];
        for (int i = 0; i < 1 << n; i++) {
            if (Integer.bitCount(i) != k) {
                continue;
            }
            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 0) {
                    continue;
                }
                ans[j] += dp[i];
            }
        }

        for (double x : ans) {
            out.println(x);
        }
    }
}
