package on2021_06.on2021_06_25_Codeforces___Codeforces_Round__728__Div__1_.B__Tree_Array;



import template.graph.*;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.InverseNumber;
import template.math.ModPrimeInverseNumber;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class BTreeArray {
    int mod = (int) 1e9 + 7;
    InverseNumber inv = new ModPrimeInverseNumber((int) 2000, mod);

    public long prob(int a, int b) {
        return (long) b * inv.inverse(a + b) % mod;
    }

    long[][] f;

    public long f(int a, int b) {
        if (f[a][b] == -1) {
            if (a == 0) {
                return f[a][b] = 1;
            } else if (b == 0) {
                return f[a][b] = 0;
            }
            f[a][b] = (long) inv.inverse(2) * (f(a - 1, b) + f(a, b - 1)) % mod;
        }
        return f[a][b];
    }

    int[][] g;
    int[] p;
    int[] depth;
    long[][] probs;
    IntegerArrayList collector;

    public void consider(int a, int b, int lca) {
        if (a < b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        probs[a][b] += f(depth[a] - depth[lca], depth[b] - depth[lca]);
    }

    public void dfsForContrib(int root, int p) {
        collector.add(root);
        for (int node : g[root]) {
            if (node == p) {
                continue;
            }
            calc(node, root, root);
            collect(node, root);
        }
        collector.clear();
        for (int node : g[root]) {
            if (node == p) {
                continue;
            }
            dfsForContrib(node, root);
        }
    }

    public void collect(int root, int p) {
        for (int node : g[root]) {
            if (node == p) {
                continue;
            }
            collect(node, root);
        }
        collector.add(root);
    }

    public void calc(int root, int p, int lca) {
        for (int node : g[root]) {
            if (node == p) {
                continue;
            }
            calc(node, root, lca);
        }
        for (int i = 0; i < collector.size(); i++) {
            int node = collector.get(i);
            consider(node, root, lca);
        }
    }

    public void dfsForP(int root, int p) {
        this.p[root] = p;
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        for (int node : g[root]) {
            if (node == p) {
                continue;
            }
            dfsForP(node, root);
        }
    }

    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerArrayList us = new IntegerArrayList(n - 1);
        IntegerArrayList vs = new IntegerArrayList(n - 1);
        for (int i = 0; i < n - 1; i++) {
            us.add(in.ri() - 1);
            vs.add(in.ri() - 1);
        }
        f = new long[n + 1][n + 1];
        SequenceUtils.deepFill(f, -1L);
        g = Graph.createUndirectedGraph(n, n - 1, us.toArray(), vs.toArray());
        probs = new long[n][n];
        depth = new int[n];
        collector = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            p = new int[n];
            dfsForP(i, -1);
            dfsForContrib(i, -1);
        }
        long exp = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                exp += probs[i][j] % mod;
            }
        }
        debug.debug("2/5", (long) 2 * inv.inverse(5) % mod);
        debug.debug("2/5", (long) 1500000013L % mod);
        debug.debug("probs", probs);
        exp = exp % mod * inv.inverse(n);
        exp = DigitUtils.mod(exp, mod);
        out.println(exp);
    }
}
