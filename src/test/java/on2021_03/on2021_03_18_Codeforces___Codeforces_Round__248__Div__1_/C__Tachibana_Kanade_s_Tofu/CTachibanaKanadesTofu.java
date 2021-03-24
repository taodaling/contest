package on2021_03.on2021_03_18_Codeforces___Codeforces_Round__248__Div__1_.C__Tachibana_Kanade_s_Tofu;



import template.io.FastInput;
import template.io.FastOutput;
import template.string.AhoCorasick;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class CTachibanaKanadesTofu {

    public int[] readString(FastInput in) {
        int n = in.ri();
        int[] a = in.ri(n);
        SequenceUtils.reverse(a);
        return a;
    }

    int[][] next;
    int[] stateVal;
    int[] l;
    int[] r;

    int[][][][][][] dp;
    int mod = (int) 1e9 + 7;
    int m;

    public int dp(int floor, int ceil, int begin, int remain, int state, int digit) {
        if (digit < 0) {
            return 1;
        }
        if (dp[floor][ceil][begin][remain][state][digit] == -1) {
            long sum = 0;
            for (int j = 0; j < m; j++) {
                if (ceil == 1 && j > r[digit]) {
                    continue;
                }
                if (floor == 1 && j < l[digit]) {
                    continue;
                }
                int to = j == 0 && begin == 0 ? 0 : next[j][state];
                if (stateVal[to] > remain) {
                    continue;
                }
                sum += dp(floor == 1 && j == l[digit] ? 1 : 0,
                        ceil == 1 && j == r[digit] ? 1 : 0,
                        begin == 0 && j == 0 ? 0 : 1,
                        remain - stateVal[to],
                        to,
                        digit - 1);
            }
            sum %= mod;
            dp[floor][ceil][begin][remain][state][digit] = (int) sum;
        }
        return dp[floor][ceil][begin][remain][state][digit];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        m = in.ri();
        int k = in.ri();
        l = readString(in);
        r = readString(in);
        if (l.length < r.length) {
            l = Arrays.copyOf(l, r.length);
        }
        AhoCorasick ac = new AhoCorasick(0, m - 1, 2000);
        int[] stringVal = new int[n];
        int[] endNode = new int[n];
        for (int i = 0; i < n; i++) {
            int len = in.ri();
            ac.prepareBuild();
            for (int j = 0; j < len; j++) {
                ac.build(in.ri());
            }
            endNode[i] = ac.buildLast;
            stringVal[i] = in.ri();
        }
        int[] topo = ac.endBuild();
        next = ac.next;
        stateVal = new int[topo.length];
        for (int i = 0; i < n; i++) {
            stateVal[endNode[i]] += stringVal[i];
        }
        for (int i = 1; i < topo.length; i++) {
            int node = topo[i];
            stateVal[node] += stateVal[ac.fails[node]];
        }

        dp = new int[2][2][2][k + 1][topo.length][r.length];
        SequenceUtils.deepFill(dp, -1);

        int ans = dp(1, 1, 0, k, 0, r.length - 1);
        out.println(ans);
    }
}
