package on2021_08.on2021_08_25_AtCoder___AtCoder_Beginner_Contest_214.F___Substrings;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongDijkstraMinimumCostFlow;
import template.utils.Debug;

import java.util.Arrays;

public class FSubstrings {
    int mod = (int) 1e9 + 7;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.rs().toCharArray();
        int n = s.length;
        int charset = 'z' - 'a' + 1;
        int[][] next = new int[charset][n];
        for (int i = 0; i < charset; i++) {
            next[i][n - 1] = n;
        }
        int[] reg = new int[charset];
        Arrays.fill(reg, n);
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < charset; j++) {
                next[j][i] = reg[j];
            }
            if (i + 1 < n) {
                reg[s[i + 1] - 'a'] = i + 1;
            }
        }
        reg[s[0] - 'a'] = 0;
        long[] dp = new long[n + 1];
        for (int x : reg) {
            dp[x]++;
        }
        for (int i = 0; i < n; i++) {
            dp[i] %= mod;
            for (int j = 0; j < charset; j++) {
                int to = next[j][i];
                dp[to] += dp[i];
            }
        }
        debug.debug("dp", dp);
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += dp[i];
        }
        sum %= mod;
        out.println(sum);
    }

    Debug debug = new Debug(false);
}

