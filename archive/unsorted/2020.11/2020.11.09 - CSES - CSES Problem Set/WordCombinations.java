package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class WordCombinations {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        ACAutomaton ac = new ACAutomaton('a', 'z');
        char[] s = new char[(int) 1e4];
        int n = in.readString(s, 0);

        char[] buf = new char[(int) 1e6];
        int m = in.readInt();

        for (int i = 0; i < m; i++) {
            int len = in.readString(buf, 0);
            ac.beginBuilding();
            for (int j = 0; j < len; j++) {
                ac.build(buf[j]);
            }
            ac.getBuildLast().increaseCnt();
        }
        long[] dp = new long[n];
        int mod = (int) 1e9 + 7;
        for (int i = 0; i < n; i++) {
            long way = i == 0 ? 1 : dp[i - 1];
            ACAutomaton.Node node = ac.getRoot();
            for (int j = i; j < n; j++) {
                node = node.next[s[j] - 'a'];
                if (node == null) {
                    break;
                }
                if (node.cnt > 0) {
                    dp[j] += way;
                }
            }
            dp[i] %= mod;
        }

        long ans = dp[n - 1];
        out.println(ans);
    }
}
