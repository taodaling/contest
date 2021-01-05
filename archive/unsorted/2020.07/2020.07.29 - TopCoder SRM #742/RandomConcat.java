package contest;

import template.binary.Bits;
import template.utils.SequenceUtils;

public class RandomConcat {
    public double expectation(String[] pieces, String needle) {
        ACAutomaton ac = new ACAutomaton('a', 'z');
        ac.beginBuilding();
        for (char c : needle.toCharArray()) {
            ac.build(c);
        }
        int success = ac.getBuildLast().id;
        ac.endBuilding();
        ACAutomaton.Node[] nodes = ac.getAllNodes().toArray(new ACAutomaton.Node[0]);
        int m = nodes.length;
        int n = pieces.length;
        int[][] cast = new int[n][m];
        int[][] occur = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int cnt = 0;
                ACAutomaton.Node cur = nodes[j];
                for (int k = 0; k < pieces[i].length(); k++) {
                    cur = cur.next[pieces[i].charAt(k) - 'a'];
                    if (cur.id == success) {
                        cnt++;
                    }
                }
                cast[i][j] = cur.id;
                occur[i][j] = cnt;
            }
        }
        double[] fact = new double[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = fact[i - 1] * i;
        }
        double[][] dp = new double[m][1 << n];
        double[][] way = new double[m][1 << n];
        way[0][0] = 1;
        for (int i = 0; i < 1 << n; i++) {
            for (int j = 0; j < m; j++) {
                double contrib = dp[j][i];
                double w = way[j][i];
                for (int k = 0; k < n; k++) {
                    if (Bits.get(i, k) == 1) {
                        continue;
                    }
                    int next = Bits.set(i, k);
                    dp[cast[k][j]][next] += contrib + w * occur[k][j];
                    way[cast[k][j]][next] += w;
                }
            }
        }

        double ans = 0;
        for(int i = 0; i < m; i++){
            ans += dp[i][(1 << n) - 1];
        }
        ans /= fact[n];

        return ans;
    }
}
