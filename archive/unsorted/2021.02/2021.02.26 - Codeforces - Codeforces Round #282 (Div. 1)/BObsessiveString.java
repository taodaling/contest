package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.IntRangeHash;
import template.string.KMPAutomaton;

public class BObsessiveString {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int mod = (int) 1e9 + 7;
        char[] s = new char[(int) 1e5];
        char[] t = new char[(int) 1e5];
        int n = in.rs(s);
        int m = in.rs(t);
        KMPAutomaton kmp = new KMPAutomaton(m);
        for (int i = 0; i < m; i++) {
            kmp.build(t[i]);
        }
        boolean[] match = new boolean[n];
        kmp.beginMatch();
        for (int i = 0; i < n; i++) {
            kmp.match(s[i]);
            match[i] = kmp.matchLast == m;
        }
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                prev[i] = -1;
            } else {
                prev[i] = prev[i - 1];
            }
            if (match[i]) {
                prev[i] = i;
            }
        }
        long[] ps = new long[n];
        long[] dp = new long[n];
        for (int i = 0; i < n; i++) {
            dp[i] = i == 0 ? 1 : dp[i - 1];
            int last = prev[i] - m;
            if (last + 1 >= 0) {
                dp[i] += 1;
                if (last >= 0) {
                    dp[i] += ps[last];
                }
            }
            dp[i] %= mod;
            ps[i] = dp[i];
            if (i != 0) {
                ps[i] += ps[i - 1];
            }
            ps[i] %= mod;
        }
        long ans = dp[n - 1];
        ans--;
        if(ans < 0){
            ans += mod;
        }
        out.println(ans);
    }
}
