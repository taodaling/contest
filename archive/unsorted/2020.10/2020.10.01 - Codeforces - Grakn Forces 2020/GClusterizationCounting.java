package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.math.DigitUtils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GClusterizationCounting {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[][] w = new int[n][n];
        for (int i = 0; i < n; i++) {
            in.populate(w[i]);
        }

        DSUExt dsu = new DSUExt(n);
        dsu.reset();

        List<Edge> edges = new ArrayList<>(n * (n - 1) / 2);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                edges.add(new Edge(i, j, w[i][j]));
            }
        }

        edges.sort((a, b) -> Integer.compare(a.w, b.w));
        for (int i = 0; i < edges.size(); i++) {
            int r = i;
            while (r + 1 < edges.size() && edges.get(r + 1).w == edges.get(r).w) {
                r++;
            }
            for (int j = i; j <= r; j++) {
                Edge e = edges.get(j);
                dsu.merge(e.a, e.b);
                dsu.edge[dsu.find(e.a)]++;
            }
            for (int j = i; j <= r; j++) {
                Edge e = edges.get(j);
                int p = dsu.find(e.a);
                if (dsu.edge[p] == choose2(dsu.size[p])) {
                    dsu.dp[p][1] = DigitUtils.modplus((int) dsu.dp[p][1], 1, mod);
                }
            }
            i = r;
        }

        int root = dsu.find(0);
        for (int i = 1; i <= n; i++) {
            out.println(dsu.dp[root][i]);
        }
    }

    public int choose2(int n) {
        return n * (n - 1) / 2;
    }
}

class Edge {
    int a;
    int b;
    int w;

    public Edge(int a, int b, int w) {
        this.a = a;
        this.b = b;
        this.w = w;
    }
}

class DSUExt extends DSU {
    int[] size;
    int[] edge;
    long[][] dp;
    static int mod = 998244353;

    public DSUExt(int n) {
        super(n);
        size = new int[n];
        edge = new int[n];
        dp = new long[n][2];
    }

    @Override
    public void reset() {
        super.reset();
        for (int i = 0; i < size.length; i++) {
            size[i] = 1;
            edge[i] = 0;
            dp[i][1] = 1;
        }
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);

        int n = size[a];
        int m = size[b];
        long[] next = new long[n + m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                next[i + j] += dp[a][i] * dp[b][j] % mod;
            }
        }
        for (int i = 1; i <= n + m; i++) {
            next[i] %= mod;
        }

        size[a] += size[b];
        edge[a] += edge[b];
        dp[a] = next;
    }
}

