package on2020_07.on2020_07_17_AtCoder___NIKKEI_Programming_Contest_2019.E___Weights_on_Vertices_and_Edges;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class EWeightsOnVerticesAndEdges {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] vertex = new int[n];
        in.populate(vertex);
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = in.readInt() - 1;
            edges[i].b = in.readInt() - 1;
            edges[i].w = in.readInt();
        }

        DSU dsu = new DSU(n);
        for (int i = 0; i < n; i++) {
            dsu.sum[i] = vertex[i];
        }

        Arrays.sort(edges, (a, b) -> Integer.compare(a.w, b.w));
        for (int i = 0; i < m; i++) {
            int r = i;
            while (r + 1 < m && edges[r + 1].w == edges[i].w) {
                r++;
            }
            for (int j = i; j <= r; j++) {
                Edge e = edges[j];
                if (dsu.prev[dsu.find(e.a)] != null) {
                    dsu.prev[dsu.find(e.a)].next = e;
                    dsu.prev[dsu.find(e.a)] = null;
                }
                if (dsu.prev[dsu.find(e.b)] != null) {
                    dsu.prev[dsu.find(e.b)].next = e;
                    dsu.prev[dsu.find(e.b)] = null;
                }
                dsu.merge(e.a, e.b);
                dsu.prev[dsu.find(e.b)] = e;
            }

            for (int j = i; j <= r; j++) {
                Edge e = edges[j];
                e.sum = dsu.sum[dsu.find(e.a)];
            }

            i = r;
        }

        for (int i = m - 1; i >= 0; i--) {
            Edge e = edges[i];
            if ((e.next == null || e.next.deleted) && e.sum < e.w) {
                e.deleted = true;
            }
        }

        int ans = 0;
        for(Edge e : edges){
            if(e.deleted){
                ans++;
            }
        }

        out.println(ans);
    }
}

class Edge {
    int a;
    int b;
    int w;
    long sum;
    boolean deleted;

    Edge next;
}

class DSU {
    protected int[] p;
    protected int[] rank;
    protected Edge[] prev;
    protected long[] sum;

    public DSU(int n) {
        p = new int[n];
        rank = new int[n];
        prev = new Edge[n];
        sum = new long[n];
        reset();
    }

    public final void reset() {
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
            rank[i] = 0;
        }
    }

    public final int find(int a) {
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        return p[a] = find(p[a]);
    }


    public final void merge(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (rank[a] == rank[b]) {
            rank[a]++;
        }

        if (rank[a] < rank[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }

        p[b] = a;
        sum[a] += sum[b];
    }
}
