package contest;

import template.algo.BinarySearch;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.DoublePreSum;
import template.primitve.generated.graph.*;

import java.util.List;
import java.util.function.DoublePredicate;

public class DDeliveryBears {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int x = in.ri();
        int[][] edges = new int[m][3];
        for (int i = 0; i < m; i++) {
            edges[i][0] = in.ri() - 1;
            edges[i][1] = in.ri() - 1;
            edges[i][2] = in.ri();
        }
        LongISAP isap = new LongISAP();
        DoublePredicate pred = mid -> {
            List<LongFlowEdge>[] g = Graph.createGraph(n);
            for (int[] e : edges) {
                LongFlow.addFlowEdge(g, e[0], e[1], (long) (e[2] / mid));
            }
            long flow = isap.apply(g, 0, n - 1, x);
            return flow < x;
        };
        double ans = BinarySearch.firstTrue(pred, 1e-12, 1e12, 1e-12, 1e-12);
        out.println(ans * x);
    }
}
