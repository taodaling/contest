package template.graph;

import template.binary.Bits;
import template.datastructure.DSU;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntConsumer;

/**
 * Given undirected graph, calculate the mst of specified subgraph
 */
public class SubsetMST {
    static long inf = (long) 2e18;
    static Edge dummy = new Edge();

    static {
        dummy.index = -1;
        dummy.a = 0;
        dummy.b = 0;
        dummy.w = inf;
    }

    int n;
    int sqrt;
    Edge[][] mst;
    Edge[] cand;
    Edge[] buf;
    DSU dsu;
    int X;
    int Y;

    /**
     * <pre>
     * time complexity: O(n2^n)
     * space complexity: O(2^n)
     * </pre>
     */
    public SubsetMST(int n, int[] a, int[] b, long[] w) {
        this.n = n;
        buf = new Edge[n];
        dsu = new DSU(n);
        Edge[][] reg = new Edge[n][n];
        mst = new Edge[1 << n][];
        List<Edge> edgeList = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            int u = a[i];
            int v = b[i];
            if (u > v) {
                int tmp = u;
                u = v;
                v = tmp;
            }
            if (reg[u][v] == null) {
                reg[u][v] = new Edge();
                reg[u][v].a = u;
                reg[u][v].b = v;
                reg[u][v].w = inf;
                edgeList.add(reg[u][v]);
            }
            if (reg[u][v].w > w[i]) {
                reg[u][v].w = w[i];
                reg[u][v].index = i;
            }
        }
        Edge[] edges = edgeList.toArray(new Edge[0]);
        Arrays.sort(edges, (x, y) -> Long.compare(x.w, y.w));
        sqrt = (int) Math.floor(Math.sqrt(n));
        for (int i = 0; i < n; i++) {
            if (i + sqrt < n) {
                X |= 1 << i;
            }
            if (i >= sqrt) {
                Y |= 1 << i;
            }
        }
        calcSubset(X, edges);
        calcSubset(Y, edges);
        cand = Arrays.stream(edges).filter(e -> e.a < sqrt && e.b >= n - sqrt).toArray(i -> new Edge[i]);
        cand = Arrays.copyOf(cand, cand.length + 1);
        cand[cand.length - 1] = dummy;
    }

    /**
     * <pre>
     * time complexity: O(n)
     * </pre>
     */
    public void mst(IntConsumer consumer, int set) {
        Edge[] a = mst[set & X];
        Edge[] b = mst[set & Y];
        Edge[] c = cand;

        int ai = 0;
        int bi = 0;
        int ci = 0;
        Edge ha = a[ai++];
        Edge hb = b[bi++];
        Edge hc = c[ci++];

        dsu.init();
        while (ha != dummy || hb != dummy || hc != dummy) {
            Edge e;
            if (ha.w <= hb.w && ha.w <= hc.w) {
                e = ha;
                ha = a[ai++];
            } else if (hb.w <= hc.w) {
                e = hb;
                hb = b[bi++];
            } else {
                e = hc;
                hc = c[ci++];
            }
            if (Bits.get(set, e.a) + Bits.get(set, e.b) == 2 && dsu.find(e.a) != dsu.find(e.b)) {
                dsu.merge(e.a, e.b);
                consumer.accept(e.index);
            }
        }
    }

    private void calcSubset(int x, Edge[] edges) {
        edges = Arrays.stream(edges).filter(e -> Bits.get(x, e.a) + Bits.get(x, e.b) == 2).toArray(i -> new Edge[i]);
        for (int subset = x + 1; subset != 0; ) {
            subset = (subset - 1) & x;
            if (mst[subset] != null) {
                continue;
            }
            dsu.init();
            int wpos = 0;
            for (Edge e : edges) {
                if (Bits.get(subset, e.a) + Bits.get(subset, e.b) == 2 && dsu.find(e.a) != dsu.find(e.b)) {
                    buf[wpos++] = e;
                    dsu.merge(e.a, e.b);
                }
            }
            buf[wpos++] = dummy;
            mst[subset] = Arrays.copyOf(buf, wpos);
        }
    }

    static class Edge {
        int a;
        int b;
        long w;
        int index;
    }
}
