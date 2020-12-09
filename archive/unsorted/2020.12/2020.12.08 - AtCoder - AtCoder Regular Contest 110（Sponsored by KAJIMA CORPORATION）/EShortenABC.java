package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;

public class EShortenABC {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int mod = (int) 1e9 + 7;
        int n = in.ri();
        char[] s = new char[n + 1];
        in.rs(s, 1);
        for (int i = 1; i <= n; i++) {
            s[i] -= 'A';
        }

        boolean same = true;
        for (int i = 2; i <= n; i++) {
            if (s[i] != s[i - 1]) {
                same = false;
            }
        }
        if (same) {
            out.println(1);
            return;
        }
        int[][] next = new int[1 << 3][n + 1];
        int[] ps = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            ps[i] = 1 << s[i];
            ps[i] ^= ps[i - 1];
        }
        for (int i = 0; i < 1 << 3; i++) {
            next[i][n] = n + 1;
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < 1 << 3; j++) {
                next[j][i] = next[j][i + 1];
            }
            next[ps[i + 1]][i] = i + 1;
        }
        long[] dp = new long[n + 1];
        dp[0] = 1;
        int[] near = new int[3];
        int mask = (1 << 3) - 1;
        for (int i = 0; i <= n; i++) {
            dp[i] %= mod;
            Arrays.fill(near, n + 1);
            for (int j = 0; j < 8; j++) {
                int v = j ^ ps[i];
                if (Integer.bitCount(v) == 2) {
                    v = mask ^ v;
                }
                if (Integer.bitCount(v) == 1) {
                    int log = Log2.floorLog(v);
                    near[log] = Math.min(near[log], next[j][i]);
                }
            }
            for (int j = 0; j < 3; j++) {
                if (near[j] <= n) {
                    dp[near[j]] += dp[i];
                }
            }
        }
        debug.debug("dp", dp);


        int post = 0;
        long sum = 0;
        for (int i = n; i >= 1; i--) {
            if (post == 0 || post == mask) {
                sum += dp[i];
            }
            post ^= 1 << s[i];
        }
        sum %= mod;
        out.println(sum);
    }
}
