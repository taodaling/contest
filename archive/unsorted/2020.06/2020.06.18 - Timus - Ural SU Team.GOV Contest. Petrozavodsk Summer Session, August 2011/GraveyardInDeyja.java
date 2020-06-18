package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.ACAutomaton;
import template.utils.SequenceUtils;

public class GraveyardInDeyja {
    String charset;

    {
        StringBuilder builder = new StringBuilder();
        for (char i = 'a'; i <= 'z'; i++) {
            builder.append(i);
        }
        for (char i = 'A'; i <= 'Z'; i++) {
            builder.append(i);
        }
        charset = builder.toString();
    }


    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.readString().toCharArray();
        char[] t = in.readString().toCharArray();
        int m = charset.length();
        ACAutomaton ac = new ACAutomaton(0, m - 1);
        ac.beginBuilding();
        for (char c : t) {
            ac.build((char) charset.indexOf(c));
        }
        ac.getBuildLast().increaseCnt();
        ac.endBuilding();
        ACAutomaton.Node[] nodes = ac.getAllNodes().toArray(new ACAutomaton.Node[0]);
        int[][] dp = new int[s.length + 1][nodes.length];
        int inf = (int) 1e8;
        SequenceUtils.deepFill(dp, -inf);
        dp[0][0] = 0;
        for (int i = 0; i < s.length; i++) {
            int l = charset.indexOf(s[i]);
            int r = l;
            if (l == -1) {
                l = 0;
                r = charset.length() - 1;
            }
            for (int k = l; k <= r; k++) {
                for (int j = 0; j < nodes.length; j++) {
                    ACAutomaton.Node next = nodes[j].next[k];
                    dp[i + 1][next.id] = Math.max(dp[i + 1][next.id], dp[i][j] + next.getCnt());
                }
            }
        }

        int ans = 0;
        for(int i = 0; i < nodes.length; i++){
            ans = Math.max(ans, dp[s.length][i]);
        }

        out.println(ans);
    }
}
