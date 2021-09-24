package on2021_08.on2021_08_11_CS_Academy___Virtual_Beta_Round__6.Xor_Cycle;



import template.datastructure.LinearBasis;
import template.datastructure.LongXorDeltaDSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XorCycle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Edge[] edges = new Edge[m];
        LongXorDeltaDSU dsu = new LongXorDeltaDSU(n);
        dsu.init();
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge(in.ri() - 1, in.ri() - 1, in.rl());
        }
        for (Edge e : edges) {
            dsu.merge(e.a, e.b, 0);
        }
        Map<Integer, List<Edge>> group = Arrays.stream(edges).collect(Collectors.groupingBy(x -> dsu.find(x.a)));
        dsu.init();
        long best = 0;
        LinearBasis lb = new LinearBasis();
        for (List<Edge> list : group.values()) {
            for (Edge e : list) {
                if (dsu.find(e.a) == dsu.find(e.b)) {
                    lb.add(dsu.delta(e.a, e.b) ^ e.w);
                } else {
                    dsu.merge(e.a, e.b, e.w);
                }
            }
            best = Math.max(best, lb.theMaximumNumberXor(0));
        }

        out.println(best);
    }
}

class Edge {
    int a;
    int b;
    long w;

    public Edge(int a, int b, long w) {
        this.a = a;
        this.b = b;
        this.w = w;
    }
}