package template.graph;

/**
 * <p>
 * Find max matching in general graph with vertex 0, 1, ..., n - 1 with time complexity O(V^3)
 * </p>
 */
public class GeneralGraphMatch {
    int n;
    int[] pre;
    boolean[][] edges;
    int[] mate;
    int[] link;
    int[] vis;
    int[] fa;
    int[] que;
    int hd;
    int tl;
    int[] ss;
    int tim;

    /**
     * -1 represent no mate
     */
    public int mateOf(int i) {
        return mate[i + 1] - 1;
    }

    public void addEdge(int x, int y) {
        edges[x + 1][y + 1] = edges[y + 1][x + 1] = true;
    }

    private int find(int x) {
        return fa[x] == x ? x : (fa[x] = find(fa[x]));
    }

    private int lca(int x, int y) {
        ++tim;
        while (ss[x] != tim) {
            if (x != 0) {
                ss[x] = tim;
                x = find(link[mate[x]]);
            }
            int tmp = x;
            x = y;
            y = tmp;
        }
        return x;
    }

    private void flower(int x, int y, int p) {
        while (find(x) != p) {
            link[x] = y;
            fa[y = mate[x]] = fa[x] = p;
            if (vis[y] == 1) vis[que[tl++] = y] = 2;
            x = link[y];
        }
    }

    private boolean match(int x) {
        hd = tl = 0;
        for (int i = 1; i <= n; ++i) vis[fa[i] = i] = 0;
        vis[que[tl++] = x] = 2;
        while (hd < tl) {
            x = que[hd++];
            for (int u = 1; u <= n; u++) {
                if (!edges[x][u]) {
                    continue;
                }
                if (0 == vis[u]) {
                    vis[u] = 1;
                    link[u] = x;
                    if (0 == mate[u]) {
                        while (0 != x) {
                            x = mate[link[u]];
                            mate[mate[u] = link[u]] = u;
                            u = x;
                        }
                        return true;
                    } else
                        vis[que[tl++] = mate[u]] = 2;
                } else if (vis[u] == 2 && find(u) != find(x)) {
                    int p = lca(x, u);
                    flower(x, u, p);
                    flower(u, x, p);
                }
            }
        }
        return false;
    }

    public int maxMatch() {
        int total = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (edges[i][j] && mate[i] == 0 && mate[j] == 0) {
                    mate[i] = j;
                    mate[j] = i;
                    total++;
                }
            }
        }

        for (int i = 1; i <= n; i++) {
            if (mate[i] == 0
                    && match(i)) {
                total++;
            }
        }
        return total;
    }

    public GeneralGraphMatch(int n) {
        this.n = n;
        int len = n + 1;
        pre = new int[len];
        edges = new boolean[len][len];
        mate = new int[len];
        link = new int[len];
        vis = new int[len];
        fa = new int[len];
        que = new int[len];
        ss = new int[len];
    }
}
