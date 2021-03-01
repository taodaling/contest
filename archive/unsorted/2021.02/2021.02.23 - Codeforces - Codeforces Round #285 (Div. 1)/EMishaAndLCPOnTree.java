package contest;

import template.graph.Graph;
import template.graph.KthAncestorOnTreeByLongLink;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.rand.HashData;

import java.util.List;

public class EMishaAndLCPOnTree {
    List<UndirectedEdge>[] g;
    int[] depth;
    long[][] tb;
    long[][] bt;
    int[] fa;
    char[] s;
    int n;
    HashData[] hds = HashData.doubleHashData((int) 5e5);
    int mod = hds[0].mod;
    long tbh0;
    long tbh1;
    long bth0;
    long bth1;

    private void add(long v, int d) {
        tbh0 = (tbh0 + hds[0].pow[d] * v) % mod;
        tbh1 = (tbh1 + hds[1].pow[d] * v) % mod;

        bth0 = (bth0 + hds[0].pow[n - 1 - d] * v) % mod;
        bth1 = (bth1 + hds[1].pow[n - 1 - d] * v) % mod;
    }

    public void dfs(int root, int p) {
        fa[root] = p;
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        add(s[root], depth[root]);
        tb[0][root] = tbh0;
        tb[1][root] = tbh1;
        bt[0][root] = bth0;
        bt[1][root] = bth1;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root);
        }
        add(mod - s[root], depth[root]);
    }

    public int dist(int a, int b, int c) {
        return depth[a] + depth[b] - depth[c] * 2 + 1;
    }

    public long hash(int a, int b, int c, int len) {
        //a to c
        int part1 = depth[a] - depth[c];
        int part2 = depth[b] - depth[c] + 1;
        part1 = Math.min(part1, len);
        len -= part1;
        part2 = Math.min(part2, len);
        long h1 = 0;
        long h2 = 0;
        long h3 = 0;
        long h4 = 0;
        if (part1 > 0) {
            int climb = kthAncestor.kthAncestor(a, part1);
            h1 = bt[0][a];
            h2 = bt[1][a];
            if (climb != -1) {
                h1 -= bt[0][climb];
                h2 -= bt[1][climb];
            }
            h1 = h1 * hds[0].inv[n - 1 - depth[a]];
            h2 = h2 * hds[1].inv[n - 1 - depth[a]];
        }
        if (part2 > 0) {
            int climb = kthAncestor.kthAncestor(b, depth[b] - depth[c] - (part2 - 1));
            h3 = tb[0][climb];
            h4 = tb[1][climb];
            if (fa[c] != -1) {
                h3 -= tb[0][fa[c]];
                h4 -= tb[1][fa[c]];
            }
            h3 = h3 * hds[0].inv[depth[c]];
            h4 = h4 * hds[1].inv[depth[c]];
        }
        h1 = DigitUtils.mod(h1, mod);
        h2 = DigitUtils.mod(h2, mod);
        h3 = DigitUtils.mod(h3, mod);
        h4 = DigitUtils.mod(h4, mod);

        long x = (h1 + h3 * hds[0].pow[part1]) % mod;
        long y = (h2 + h4 * hds[1].pow[part1]) % mod;
        return DigitUtils.asLong(x, y);
    }

    LcaOnTree lca;
    KthAncestorOnTreeByLongLink kthAncestor;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        g = Graph.createGraph(n);
        depth = new int[n];
        tb = new long[2][n];
        bt = new long[2][n];
        fa = new int[n];
        s = new char[n];
        in.rs(s);
        for (int i = 0; i < n; i++) {
            s[i] -= 'a';
        }
        for (int i = 0; i < n - 1; i++) {
            Graph.addUndirectedEdge(g, in.ri() - 1, in.ri() - 1);
        }
        dfs(0, -1);
        kthAncestor = new KthAncestorOnTreeByLongLink(g, 0);
        lca = new LcaOnTree(n);
        lca.init(g, i -> i == 0);
        int q = in.ri();
        for (int i = 0; i < q; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int c = in.ri() - 1;
            int d = in.ri() - 1;
            int lab = lca.lca(a, b);
            int lcd = lca.lca(c, d);
            int len = Math.min(dist(a, b, lab), dist(c, d, lcd));
            int l = 0;
            int r = len;
            while (l < r) {
                int m = (l + r + 1) / 2;
                if (hash(a, b, lab, m) == hash(c, d, lcd, m)) {
                    l = m;
                } else {
                    r = m - 1;
                }
            }
            out.println(l);
        }
    }
}
