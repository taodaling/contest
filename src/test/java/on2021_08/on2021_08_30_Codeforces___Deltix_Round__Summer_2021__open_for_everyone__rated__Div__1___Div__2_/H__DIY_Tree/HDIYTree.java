package on2021_08.on2021_08_30_Codeforces___Deltix_Round__Summer_2021__open_for_everyone__rated__Div__1___Div__2_.H__DIY_Tree;



import template.algo.MatroidIndependentSet;
import template.algo.MaximumWeightMatroidIntersect;
import template.binary.Bits;
import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongArrayList;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HDIYTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] d = in.ri(k);
        int n2 = n * (n - 1) / 2;
        int ck = k * (k - 1) / 2;
        IntegerArrayList usList = new IntegerArrayList(n2);
        IntegerArrayList vsList = new IntegerArrayList(n2);
        LongArrayList wsList = new LongArrayList(n2);
        IntegerArrayList edgeList = new IntegerArrayList(ck);
        List<Edge> outside = new ArrayList<>(n2);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i >= k && j >= k) {
                    Edge e = new Edge(i, j, in.ri());
                    outside.add(e);
                } else {
                    usList.add(i);
                    vsList.add(j);
                    wsList.add(in.ri());
                }
                if (i < k && j < k) {
                    edgeList.add(usList.size() - 1);
                }
            }
        }
        DSU dsu = new DSU(n);
        dsu.init();
        outside.sort(Comparator.comparingInt(x -> x.w));

        debug.debug("size", usList.size());
        for (Edge e : outside) {
            if (dsu.find(e.a) == dsu.find(e.b)) {
                continue;
            }
            dsu.merge(e.a, e.b);
            usList.add(e.a);
            vsList.add(e.b);
            wsList.add(e.w);
        }
        int[] us = usList.toArray();
        int[] vs = vsList.toArray();
        long[] ws = wsList.toArray();
        int[] edges = edgeList.toArray();
        int m = edges.length;
        long inf = (long) 1e9;
        long[] wsSnapshot = ws.clone();
        int[] type = new int[us.length];
        for (int i = 0; i < us.length; i++) {
            if (us[i] < k && vs[i] >= k) {
                type[i] = us[i];
            } else if (vs[i] < k && us[i] >= k) {
                type[i] = vs[i];
            } else {
                type[i] = k;
            }
        }
        debug.debug("size", us.length);
        long best = inf;
        for (int i = 0; i < 1 << m; i++) {
            boolean ok = true;
            dsu.init();
            System.arraycopy(wsSnapshot, 0, ws, 0, ws.length);
            int[] cap = new int[k + 1];
            System.arraycopy(d, 0, cap, 0, k);
            cap[k] = n;
            for (int j = 0; j < m; j++) {
                int e = edges[j];
                if (Bits.get(i, j) == 0) {
                    ws[e] += inf;
                } else {
                    int u = us[e];
                    int v = vs[e];
                    ws[e] -= inf;
                    cap[u]--;
                    cap[v]--;
                    if (dsu.find(u) == dsu.find(v)) {
                        ok = false;
                    }
                    dsu.merge(u, v);
                }
            }
            for (int j = 0; j < ws.length; j++) {
                ws[j] = -ws[j];
            }
            for (int j = 0; j <= k; j++) {
                if (cap[j] < 0) {
                    ok = false;
                }
            }
            if (!ok) {
                continue;
            }
            MatroidIndependentSet container = MatroidIndependentSet.ofColorContainers(type, cap);
            MatroidIndependentSet tree = MatroidIndependentSet.ofSpanningTree(n, new int[][]{us, vs});
            MaximumWeightMatroidIntersect mi = new MaximumWeightMatroidIntersect(us.length, ws);
            boolean[] sol = mi.intersect(container, tree);
            long sum = 0;
            int occur = 0;
            for (int j = 0; j < sol.length; j++) {
                if (sol[j]) {
                    occur++;
                    sum += ws[j];
                }
            }
            sum = -sum;
            sum += Integer.bitCount(i) * inf;
            if (sum >= inf || occur < n - 1) {
                continue;
            }
            best = Math.min(best, sum);
        }
        out.println(best);
    }

    Debug debug = new Debug(true);
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