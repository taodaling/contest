package contest;

import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.InverseNumber;
import template.math.ModPrimeInverseNumber;
import template.primitve.generated.datastructure.LongBIT;
import template.utils.Debug;

import java.util.List;

public class RandomWalkQueries {
    int mod = (int) 1e9 + 7;
    InverseNumber inv = new ModPrimeInverseNumber((int) 2e5, mod);
    LongBIT[] toP;
    LongBIT[] state;
    List<UndirectedEdge>[] g;
    long[] prefixProd;
    long[] invPrefixProd;
    int[] deg;
    int[] shrinkDeg;
    int[] depth;
    int[] size;
    int[] fa;
    LcaOnTree lca;

    public void dfsForPrefix(int root, int p, long pp, long invPp) {
        prefixProd[root] = pp;
        invPrefixProd[root] = invPp;
        pp = pp * shrinkDeg[root] % mod;
        invPp = invPp * inv.inverse(shrinkDeg[root]) % mod;
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForPrefix(e.to, root, pp, invPp);
        }
    }

    public long pathProb(int v, int u, int l) {
        if (u == v) {
            return 1;
        }
        long prob = invPrefixProd[v] * invPrefixProd[u] % mod * prefixProd[l] % mod * prefixProd[l] % mod * shrinkDeg[l] % mod * inv.inverse(deg[v]) % mod;
        return prob;
    }

    public long pathProbWithoutStart(int v, int u, int l) {
        if (u == v) {
            return 1;
        }
        long prob = invPrefixProd[v] * invPrefixProd[u] % mod * prefixProd[l] % mod * prefixProd[l] % mod * shrinkDeg[l] % mod * inv.inverse(shrinkDeg[v]) % mod;
        return prob;
    }

    public int dist(int u, int v, int l) {
        return depth[u] + depth[v] - 2 * depth[l];
    }

    private void dfsForSize(int root, int p) {
        size[root] = 1;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForSize(e.to, root);
            size[root] += size[e.to];
        }
    }

    private int dfsForCentroid(int root, int p, int total) {
        int maxChild = total - size[root];
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            int ans = dfsForCentroid(e.to, root, total);
            if (ans != -1) {
                return ans;
            }
            maxChild = Math.max(maxChild, size[e.to]);
        }
        return maxChild * 2 <= total ? root : -1;
    }

    public void dac(int root, int p) {
        dfsForSize(root, -1);
        int total = size[root];
        root = dfsForCentroid(root, -1, total);
        fa[root] = p;
        toP[root] = new LongBIT(total + 1);
        state[root] = new LongBIT(total);
        for (UndirectedEdge e : g[root]) {
            g[e.to].remove(e.rev);
            dac(e.to, root);
        }
    }

    public void addRange(LongBIT bit, int l, int r, long x) {
        if (l > r) {
            return;
        }
        bit.update(l + 1, x);
        bit.update(r + 2, -x);
    }

    public long queryRange(LongBIT bit, int i) {
        return bit.query(i + 1);
    }

    int[][] dists;
    long[][] pathProb;
    long[][] pathProbWithoutStart;

    public void modify(int u, int d) {
        //handle root case
        addRange(state[u], 0, 0, 1);
        addRange(state[u], 1, d, (long) shrinkDeg[u] * inv.inverse(deg[u]) % mod);

        int lastTop = u;
        for (int top = fa[u], step = 0; top != -1; lastTop = top, top = fa[top], step++) {
            int dist = dists[u][step];
            if (dist <= d) {
                long prob = pathProb[u][step];
                addRange(state[top], 0, d - dist, prob);
                addRange(toP[lastTop], 0, d - dist, -prob);
            }
        }
    }

    public long query(int u) {
        int lastTop = u;
        long ans = queryRange(state[u], 0);
        for (int top = fa[u], step = 0; top != -1; lastTop = top, top = fa[top], step++) {
            int d = dists[u][step];
            long prob = pathProbWithoutStart[u][step];
            long localContrib = queryRange(state[top], d) + queryRange(toP[lastTop], d);
            localContrib = localContrib % mod * prob % mod;
            ans += localContrib;
        }
        ans = DigitUtils.mod(ans, mod);
        return ans;
    }

    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();

        if (n == 1) {
            int type1 = 0;
            for (int i = 0; i < q; i++) {
                int t = in.ri();
                if (t == 1) {
                    in.ri();
                    in.ri();
                    type1++;
                } else {
                    in.ri();
                    out.println(type1);
                }
            }
            return;
        }

        toP = new LongBIT[n];
        state = new LongBIT[n];
        prefixProd = new long[n];
        invPrefixProd = new long[n];
        deg = new int[n];
        shrinkDeg = new int[n];
        depth = new int[n];
        size = new int[n];
        fa = new int[n];
        g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            deg[a]++;
            deg[b]++;
            Graph.addUndirectedEdge(g, a, b);
        }
        for (int i = 0; i < n; i++) {
            shrinkDeg[i] = Math.max(1, deg[i] - 1);
        }
        lca = new LcaOnTree(g, 0);
        dfsForPrefix(0, -1, 1, 1);
        dac(0, -1);

        dists = new int[n][20];
        pathProb = new long[n][20];
        pathProbWithoutStart = new long[n][20];
        for (int i = 0; i < n; i++) {
            for (int step = 0, top = fa[i]; top != -1; top = fa[top], step++) {
                int l = lca.lca(i, top);
                pathProb[i][step] = pathProb(i, top, l);
                pathProbWithoutStart[i][step] = pathProbWithoutStart(i, top, l);
                dists[i][step] = dist(i, top, l);
            }
        }

        int lastans = 0;
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            if (t == 1) {
                int a = in.ri();
                int b = in.ri();
                if (debug.enable()) {
                    a--;
                } else {
                    a = (lastans + a) % n;
                    b = (lastans + b) % n + 1;
                }
                modify(a, b);
            } else {
                int u = in.ri();
                if (debug.enable()) {
                    u--;
                } else {
                    u = (lastans + u) % n;
                }
                long ans = query(u);
                out.println(ans);

                lastans = (int) ans;
            }
        }
    }
}
