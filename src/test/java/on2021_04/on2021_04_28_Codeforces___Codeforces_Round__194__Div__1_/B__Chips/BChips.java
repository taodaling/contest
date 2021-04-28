package on2021_04.on2021_04_28_Codeforces___Codeforces_Round__194__Div__1_.B__Chips;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BChips {
    public int dist(int x0, int y0, int x1, int y1) {
        return Math.abs(x0 - x1) + Math.abs(y0 + y1);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[][] tb = new Node[2][n];
        Node[][] lr = new Node[2][n];
        for (int j = 0; j < 2; j++) {
            for (int i = 1; i < n - 1; i++) {
                tb[j][i] = new Node();
            }
            for (int i = 1; i < n - 1; i++) {
                lr[j][i] = new Node();
            }
        }
        for (int i = 0; i < m; i++) {
            int x = in.ri() - 1;
            int y = in.ri() - 1;
            tb[0][y] = tb[1][y] = lr[0][x] = lr[1][x] = null;
        }
        for (int j = 0; j < n; j++) {
            if (tb[0][j] != null && tb[1][j] != null) {
                tb[0][j].adj.add(tb[1][j]);
                tb[1][j].adj.add(tb[0][j]);
            }
            if (lr[0][j] != null && lr[1][j] != null) {
                lr[0][j].adj.add(lr[1][j]);
                lr[1][j].adj.add(lr[0][j]);
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < n; k++) {
                    for (int t = 0; t < n; t++) {
                        if (tb[i][k] == null || lr[j][t] == null) {
                            continue;
                        }
                        int y = k;
                        int x = t;
                        if (dist(i * (n - 1), k, x, y) == dist(t, j * (n - 1), x, y)) {
                            tb[i][k].adj.add(lr[j][t]);
                            lr[j][t].adj.add(tb[i][k]);
                        }
                    }
                }
            }
        }

        int ans = 0;
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < n; i++) {
                if (n % 2 == 1 && i == n / 2) {
                    continue;
                }
                if (tb[j][i] != null && tb[j][i].color == -1) {
                    Arrays.fill(cnt, 0);
                    dfs(tb[j][i], 0);
                    ans += Math.max(cnt[0], cnt[1]);
                }
                if (lr[j][i] != null && lr[j][i].color == -1) {
                    Arrays.fill(cnt, 0);
                    dfs(lr[j][i], 0);
                    ans += Math.max(cnt[0], cnt[1]);
                }
            }
        }

        if (n % 2 == 1) {
            if (lr[0][n / 2] != null || lr[1][n / 2] != null ||
                    tb[0][n / 2] != null || tb[1][n / 2] != null) {
                ans++;
            }
        }

        out.println(ans);
    }

    int[] cnt = new int[2];

    void dfs(Node root, int c) {
        if (root.color != -1) {
            return;
        }
        root.color = c;
        cnt[root.color]++;
        for (Node node : root.adj) {
            dfs(node, c ^ 1);
        }
    }
}

class Node {
    int color = -1;
    List<Node> adj = new ArrayList<>(8);
}
