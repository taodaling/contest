package on2020_04.on2020_04_24_AtCoder_Regular_Contest_099.E___Independence;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskE {
    boolean[][] next;
    int n;
    int[] colors;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int m = in.readInt();
        next = new boolean[n][n];
        colors = new int[n];
        SequenceUtils.deepFill(next, true);
        SequenceUtils.deepFill(colors, -1);
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            next[a][b] = next[b][a] = false;
        }

        List<int[]> cnts = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            if (colors[i] != -1) {
                continue;
            }
            int[] c = new int[2];
            dfs(i, 0, c);
            cnts.add(c);
        }

        if (!valid) {
            out.println(-1);
            return;
        }

        int k = cnts.size();
        boolean[][] dp = new boolean[k + 1][n / 2 + 1];
        dp[0][0] = true;
        for (int i = 1; i <= k; i++) {
            int[] c = cnts.get(i - 1);
            for (int j = 0; j <= n / 2; j++) {
                if (j >= c[0]) {
                    dp[i][j] = dp[i][j] || dp[i - 1][j - c[0]];
                }
                if (j >= c[1]) {
                    dp[i][j] = dp[i][j] || dp[i - 1][j - c[1]];
                }
            }
        }

        int mx = 0;
        for (int i = 0; i <= n / 2; i++) {
            if (dp[k][i]) {
                mx = i;
            }
        }

        out.println(pick2(mx) + pick2(n - mx));
    }

    public int pick2(int n){
        return n * (n - 1) / 2;
    }

    boolean valid = true;

    public void dfs(int root, int color, int[] cnts) {
        if (colors[root] != -1) {
            if (color != colors[root]) {
                valid = false;
            }
            return;
        }
        colors[root] = color;
        cnts[color]++;
        for (int i = 0; i < n; i++) {
            if (!next[root][i] || i == root) {
                continue;
            }
            dfs(i, color ^ 1, cnts);
        }
    }
}
