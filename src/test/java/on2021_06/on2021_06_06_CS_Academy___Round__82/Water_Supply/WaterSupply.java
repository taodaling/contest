package on2021_06.on2021_06_06_CS_Academy___Round__82.Water_Supply;



import template.graph.KDegreeMinimumSpanningTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongWeightGraph;
import template.primitve.generated.graph.LongWeightUndirectedEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class WaterSupply {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri() + 1;
        int k = in.ri();
        List<LongWeightUndirectedEdge>[] g = IntStream.range(0, n).mapToObj(i -> new ArrayList<>(n)).toArray(i -> new List[i]);
        for (int i = 1; i < n; i++) {
            LongWeightGraph.addUndirectedEdge(g, 0, i, in.ri());
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {
                int w = in.ri();
                if (i < j) {
                    LongWeightGraph.addUndirectedEdge(g, i, j, w);
                }
            }
        }
        KDegreeMinimumSpanningTree st = new KDegreeMinimumSpanningTree(g, 0);
        long best = Long.MAX_VALUE;
        while (st.next() && st.getTree()[0].size() <= k) {
            best = Math.min(best, st.getTotalWeight());
        }
        out.println(best);
    }
}
