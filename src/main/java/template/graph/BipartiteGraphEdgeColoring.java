package template.graph;

/**
 * Paint all edges in bipartite graph with least colors used.
 * <br>
 * O(VE) algorithm.
 */
public class BipartiteGraphEdgeColoring {
    int[][] left;
    int[][] right;
    int[][] colors;

    public BipartiteGraphEdgeColoring(int n, int m) {
        left = new int[n][Math.max(n, m)];
        right = new int[m][Math.max(n, m)];
    }

    public void paint(int a, int b, int c) {
        int old = colors[a][b];
        if (old != -1) {
            if (left[a][old] == b) {
                left[a][old] = -1;
            }
            if (right[b][old] == a) {
                right[b][old] = -1;
            }
        }
        colors[a][b] = c;
        left[a][c] = b;
        right[b][c] = a;
    }

    /**
     * a - c1 -> b => a - c2 -> b
     *
     * @param b
     * @param c1
     * @param c2
     */
    public void dfs(int b, int c1, int c2) {
        if (right[b][c2] == -1) {
            return;
        }
        int na = right[b][c2];
        int nb = left[na][c1];
        if (nb != -1) {
            dfs(nb, c1, c2);
            paint(na, nb, c2);
        }
        paint(na, b, c1);
    }

    /**
     * Find a bipartite edge painting with least colors used. The color index start with 0.
     */
    public int solve(boolean[][] g, int[][] colors) {
        if (g.length == 0 || g[0].length == 0) {
            return 0;
        }
        int n = g.length;
        int m = g[0].length;
        this.colors = colors;
        int deg = 0;
        for (int i = 0; i < n; i++) {
            int local = 0;
            for (int j = 0; j < m; j++) {
                local += g[i][j] ? 1 : 0;
            }
            deg = Math.max(deg, local);
        }
        for (int j = 0; j < m; j++) {
            int local = 0;
            for (int i = 0; i < n; i++) {
                local += g[i][j] ? 1 : 0;
            }
            deg = Math.max(deg, local);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                colors[i][j] = -1;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < deg; j++) {
                left[i][j] = -1;
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < deg; j++) {
                right[i][j] = -1;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!g[i][j]) {
                    continue;
                }
                int c = -1;
                int ca = -1;
                int cb = -1;
                for (int k = 0; k < deg; k++) {
                    if (left[i][k] == -1 && right[j][k] == -1) {
                        c = k;
                        break;
                    }
                    if (left[i][k] == -1) {
                        cb = k;
                    }
                    if (right[j][k] == -1) {
                        ca = k;
                    }
                }
                if (c != -1) {
                    paint(i, j, c);
                    continue;
                }
                dfs(j, ca, cb);
                paint(i, j, cb);
            }
        }

        return deg;
    }
}
