package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class P2210Haywire {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] edges = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                int to = in.ri() - 1;
                edges[i] |= 1 << to;
            }
        }
        int[] dp = new int[1 << n];
        Arrays.fill(dp, (int) 1e9);
        dp[0] = 0;
        for (int i = 0; i < 1 << n; i++) {
            int size = Integer.bitCount(i);
            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 1) {
                    continue;
                }
                int to = i | (1 << j);
                int left = Integer.bitCount(i & edges[j]);
                int right = 3 - left;
                dp[to] = Math.min(dp[to], dp[i] + (left - right) * size);
            }
        }

        out.println(dp[dp.length - 1]);
    }
}
