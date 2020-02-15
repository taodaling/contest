package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.BitOperator;
import template.math.Log2;

public class TaskU {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.readInt();
            }
        }
        long[] profits = new long[1 << n];
        Log2 log2 = new Log2();
        BitOperator bo = new BitOperator();
        for (int i = 0; i < (1 << n); i++) {
            if (Integer.lowestOneBit(i) == i) {
                profits[i] = 0;
                continue;
            }
            int bit = log2.floorLog(i);
            int j = i - (1 << bit);
            profits[i] = profits[j];
            for (int k = 0; k < n; k++) {
                if (bo.bitAt(j, k) == 0) {
                    continue;
                }
                profits[i] += mat[k][bit];
            }
        }

        SubsetGenerator sg = new SubsetGenerator();
        long[] dp = new long[1 << n];
        for (int i = 1; i < (1 << n); i++) {
            dp[i] = profits[i];
            sg.setSet(i);
            while (sg.hasNext()) {
                int next = sg.next();
                if (next == 0 || next == i) {
                    continue;
                }
                dp[i] = Math.max(dp[i], dp[i - next] + dp[next]);
            }
        }

        out.println(dp[(1 << n) - 1]);
    }
}
