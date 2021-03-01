package contest;

import template.binary.Bits;
import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.LongEntryIterator;
import template.primitve.generated.datastructure.LongHashMap;
import template.primitve.generated.datastructure.LongIterator;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class RiolkusMockCCCS2KeenKeenerSequence {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] data = in.ri(n);
        LongHashMap a = new LongHashMap(n, false);
        for (int i = 0; i < n; i++) {
            a.modify(data[i], 1);
        }
        LongHashMap ab = new LongHashMap(n * n, false);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ab.modify((long) data[i] * data[j], 1);
            }
        }
        LongHashMap square = new LongHashMap(n, false);
        for (int i = 0; i < n; i++) {
            square.modify((long) data[i] * data[i], 1);
        }
        List<Edge> edgeList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                edgeList.add(new Edge(i, j));
            }
        }
        Edge[] edges = edgeList.toArray(new Edge[0]);
        int m = edges.length;
        DSU dsu = new DSU(4);
        long ans = 0;
        for (int i = 0; i < 1 << m; i++) {
            debug.debug("i", i);
            dsu.init();
            for (int j = 0; j < m; j++) {
                if (Bits.get(i, j) == 1) {
                    debug.debug("e", edges[j]);
                    dsu.merge(edges[j].a, edges[j].b);
                }
            }
            int bc = Integer.bitCount(i);
            int cc = 0;
            for (int j = 0; j < 4; j++) {
                if (dsu.find(j) == j) {
                    cc++;
                }
            }
            long contrib = 0;
            if (bc > 1) {
                if (cc == 2) {
                    if (dsu.find(0) == dsu.find(2) && dsu.find(1) == dsu.find(3) ||
                            dsu.find(1) == dsu.find(2) && dsu.find(0) == dsu.find(3)) {
                        contrib += n * n;
                    } else {
                        for (LongEntryIterator iterator = a.iterator(); iterator.hasNext(); ) {
                            iterator.next();
                            long k = iterator.getEntryValue();
                            contrib += k * k;
                        }
                    }
                } else if (cc == 1) {
                    contrib += n;
                } else {
                    assert false;
                }
            } else if (bc == 1) {
                if (dsu.find(0) == dsu.find(1) || dsu.find(2) == dsu.find(3)) {
                    //one side
                    for (int x : data) {
                        contrib += ab.getOrDefault((long) x * x, 0);
                    }
                } else {
                    //kick any
                    for (LongEntryIterator iterator = a.iterator(); iterator.hasNext(); ) {
                        iterator.next();
                        long k = iterator.getEntryValue();
                        contrib += k * k;
                    }
                    contrib *= n;
                }
            } else {
                for (LongEntryIterator iterator = ab.iterator(); iterator.hasNext(); ) {
                    iterator.next();
                    long k = iterator.getEntryValue();
                    contrib += k * k;
                }
            }
            if (bc % 2 == 1) {
                contrib = -contrib;
            }
            debug.debug("contrib", contrib);
            ans += contrib;
        }
        out.println(ans);
    }
}

class Edge {
    int a;
    int b;

    public Edge(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public String toString() {
        return "(" + a + "," + b + ")";
    }
}
