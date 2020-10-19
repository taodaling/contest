package contest;

import template.datastructure.DSU;
import template.io.FastInput;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpySatellites {

    List<Edge> edges = new ArrayList<>((int) 4e5);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();

        if (n == 0) {
            throw new UnknownError();
        }

        int[][] pts = new int[2][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                pts[j][i] = in.readInt();
            }
        }

        edges.clear();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int dx = pts[0][i] - pts[0][j];
                int dy = pts[1][i] - pts[1][j];
                edges.add(new Edge(i, j, dx * dx + dy * dy));
            }
        }

        edges.sort((a, b) -> Integer.compare(a.w, b.w));
        DSUExt dsu = new DSUExt(n);
        dsu.reset();
        for (int i = 0; i < edges.size(); i++) {
            int r = i;
            while (r + 1 < edges.size() && edges.get(r + 1).w == edges.get(r).w) {
                r++;
            }

            for (int j = i; j <= r; j++) {
                Edge e = edges.get(j);
                if (dsu.find(e.a) != dsu.find(e.b)) {
                    dsu.merge(e.a, e.b);
                }
                int p = dsu.find(e.a);
                dsu.edges[p]++;
            }

            for(int j = i; j <= r; j++){
                Edge e = edges.get(j);
                int p = dsu.find(e.a);
                assert choose2(dsu.sizes[p]) >= dsu.edges[p];
                if (choose2(dsu.sizes[p]) == dsu.edges[p]) {
                    dsu.dp[p][1] = true;
                }
            }

            i = r;
        }

        int p = dsu.find(0);
        boolean[] dp = dsu.dp[p];
        for (int i = 1; i <= n; i++) {
            out.print(dp[i] ? 1 : 0);
        }
        out.println();
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
    int[] edges;
    boolean[][] dp;
    int[] sizes;

    public DSUExt(int n) {
        super(n);
        edges = new int[n];
        sizes = new int[n];
        dp = new boolean[n][2];
    }

    @Override
    public void reset() {
        super.reset();
        Arrays.fill(edges, 0);
        Arrays.fill(sizes, 1);
        for (int i = 0; i < dp.length; i++) {
            dp[i][1] = true;
        }
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);

        int n = sizes[a];
        int m = sizes[b];
        boolean[] next = new boolean[n + m + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                if (dp[a][i] && dp[b][j]) {
                    next[i + j] = true;
                }
            }
        }

        edges[a] += edges[b];
        sizes[a] += sizes[b];
        dp[a] = next;
    }
}