package contest;

import template.graph.*;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongHashSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StronglyConnectedEdges {
    public long edgeId(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        return DigitUtils.asLong(a, b);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        UndirectedEdge[] es = new UndirectedEdge[m];
        LongHashSet set = new LongHashSet(m, false);
        for (int i = 0; i < m; i++) {
            es[i] = Graph.addUndirectedEdge(g, in.ri() - 1, in.ri() - 1);
        }
        DfsTree dt = new DfsTree(n);
        dt.setConsumer((root, e) -> {
            set.add(edgeId(e.to, ((UndirectedEdge) e).rev.to));
        });
        dt.init(g);

        UndirectedTarjanSCC ut = new UndirectedTarjanSCC(n);
        ut.init(g);
        for (int i = 0; i < n; i++) {
            if (ut.set[i] != ut.set[0]) {
                out.println("IMPOSSIBLE");
                return;
            }
        }

        for (UndirectedEdge e : es) {
            int a = e.to;
            int b = e.rev.to;
            if (set.contain(edgeId(e.to, e.rev.to))) {
                if (dt.depth[b] < dt.depth[a]) {
                    int tmp = a;
                    a = b;
                    b = tmp;
                }
            } else {
                if (dt.depth[b] > dt.depth[a]) {
                    int tmp = a;
                    a = b;
                    b = tmp;
                }
            }
            out.append(a + 1).append(' ').append(b + 1).println();
        }
    }
}
