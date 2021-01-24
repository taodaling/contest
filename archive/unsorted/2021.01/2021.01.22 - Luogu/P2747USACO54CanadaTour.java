package contest;

import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P2747USACO54CanadaTour {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Map<String, Integer> map = new HashMap<>(n);
        for (int i = 0; i < n; i++) {
            map.put(in.rs(), i);
        }
        List<Integer>[] g = Graph.createGraph(n);
        boolean[] toT = new boolean[n];
        for (int i = 0; i < m; i++) {
            int a = map.get(in.rs());
            int b = map.get(in.rs());
            if (a > b) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            if (a == b) {
                continue;
            }
            g[a].add(b);
            if (b == n - 1) {
                toT[a] = true;
            }
        }
        if (n == 1) {
            out.println(1);
            return;
        }
        int[][] dp = new int[n][n];
        int inf = (int) 1e9;
        SequenceUtils.deepFill(dp, -inf);
        dp[0][0] = 1;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                for (int x : g[i]) {
                    if (x == j) {
                        continue;
                    }
                    if (x < j) {
                        dp[x][j] = Math.max(dp[x][j], dp[i][j] + 1);
                    } else {
                        dp[j][x] = Math.max(dp[j][x], dp[i][j] + 1);
                    }
                }
                if (toT[i] && toT[j]) {
                    dp[n - 1][n - 1] = Math.max(dp[n - 1][n - 1], dp[i][j] + 1);
                }
            }
        }

        if (dp[n - 1][n - 1] < 0) {
            out.println(1);
            return;
        }
        out.println(dp[n - 1][n - 1]);
    }
}
