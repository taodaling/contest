package on2019_11.on2019_11_16_Codeforces_Round__600__Div__2_.E___Antenna_Coverage;



import template.FastInput;
import template.FastOutput;
import template.SequenceUtils;

import java.util.Arrays;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Node[] nodes = new Node[1 + n];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            int x = in.readInt();
            int s = in.readInt();
            nodes[i].l = x - s;
            nodes[i].r = x + s;
        }

        Arrays.sort(nodes, 1, n + 1, (a, b) -> a.l - b.l);

        int[][] dp = new int[n + 1][m + 1];
        SequenceUtils.deepFill(dp, (int) 1e8);
        dp[0][0] = 0;
        int[] minUntil = new int[m + 1];
        for (int i = 0; i <= m; i++) {
            minUntil[i] = i;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = m; j >= 0; j--) {
                int ext = Math.max(0, j - nodes[i].r);
                int l = Math.max(0, nodes[i].l - ext - 1);
                dp[i][j] = minUntil[l] + ext;
                if (j < m) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][j + 1]);
                }
            }

            for (int j = 0; j <= m; j++) {
                minUntil[j] = Math.min(minUntil[j], dp[i][j]);
            }
        }

        out.println(minUntil[m]);
    }

    static class Node {
        int l;
        int r;
    }
}

