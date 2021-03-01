package template.problem;

import template.primitve.generated.graph.LongKM;
import template.utils.SequenceUtils;

public class DirectedGraphMaximumWeightedHamiltonCirclePartition {
    LongKM km;
    long[][] table;
    boolean[][] edge;
    int n;
    long inf;

    public DirectedGraphMaximumWeightedHamiltonCirclePartition(int n) {
        this.n = n;
        table = new long[n][n];
        edge = new boolean[n][n];
        inf = Long.MAX_VALUE / 2 / n;
        SequenceUtils.deepFill(table, -inf);
    }

    public void addEdge(int i, int j, long weight) {
        edge[i][j] = true;
        table[i][j] = Math.max(table[i][j], weight);
    }

    long matchWeight;

    public boolean solve() {
        km = new LongKM(table);
        matchWeight = km.solve();
        for (int i = 0; i < n; i++) {
            if (!edge[i][km.getLeftPartner(i)]) {
                return false;
            }
        }
        return true;
    }

    public long sumOfEdgeWeight() {
        return matchWeight;
    }

    public int next(int i) {
        return km.getLeftPartner(i);
    }
}
