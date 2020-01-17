package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.ACAutomaton;

public class FWrapAround {
    ACAutomaton.Node end;
    ACAutomaton.Node begin;
    int n;
    char[] s;
    ACAutomaton.Node[] nodes;
    ACAutomaton at;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        s = in.readString().toCharArray();
        at = new ACAutomaton('0', '1');
        at.beginBuilding();
        for (char c : s) {
            at.build(c);
        }
        at.endBuilding();
        end = at.getBuildLast();
        nodes = at.getAllNodes().toArray(new ACAutomaton.Node[0]);
        begin = nodes[0];

        long ans = 0;
        for(int i = 0; i < n; i++){
            ans += way(i);
        }

        out.println(ans);
    }

    public long pow(long x, long n){
        if(n == 0){
            return 1;
        }
        long ans = pow(x, n / 2);
        ans *= ans;
        if(n % 2 == 1){
            ans *= x;
        }
        return ans;
    }

    public long way(int offset) {
        char[] data = new char[1 + 2 * n];
        for (int i = 0; i < s.length; i++) {
            data[1 + i + offset] = s[i];
            data[1 + (i + offset) % n] = s[i];
        }
        int dpLen = offset + s.length;
        long[][] dp = new long[dpLen + 1][nodes.length];
        dp[0][begin.getId()] = 1;
        for (int i = 0; i < dpLen; i++) {
            for (int j = 0; j < nodes.length; j++) {
                if (dp[i][j] == 0 || j == end.getId()) {
                    continue;
                }
                if (data[i + 1] == 0) {
                    dp[i + 1][nodes[j].next[0].getId()] += dp[i][j];
                    dp[i + 1][nodes[j].next[1].getId()] += dp[i][j];
                } else {
                    dp[i + 1][nodes[j].next[data[i + 1] - '0'].getId()] += dp[i][j];
                }
            }
        }
        long ans = dp[dpLen][end.getId()];
        if(offset + s.length < n){
            ans *= pow(2, n - offset - s.length);
        }
        return ans;
    }
}
