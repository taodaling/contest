package on2020_11.on2020_11_28_AtCoder___AtCoder_Regular_Contest_109.A___Hands;



import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.graph.LongWeightGraph;
import template.primitve.generated.graph.LongWeightUndirectedEdge;

import java.util.List;

public class AHands {
    int threshold = 100;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt() - 1;
        int b = in.readInt() - 1;
        int x = in.readInt();
        int y = in.readInt();

        List<LongWeightUndirectedEdge>[] g = Graph.createGraph(threshold + threshold);
        for (int i = 0; i < threshold; i++) {
            LongWeightGraph.addUndirectedEdge(g, left(i), right(i), x);
            if (i + 1 < threshold) {
                LongWeightGraph.addUndirectedEdge(g, left(i + 1), right(i), x);
                LongWeightGraph.addUndirectedEdge(g, left(i), left(i + 1), y);
                LongWeightGraph.addUndirectedEdge(g, right(i), right(i + 1), y);
            }
        }

        IntegerArrayList source = new IntegerArrayList();
        source.add(left(a));
        long[] dists = new long[threshold + threshold];
        LongWeightGraph.dijkstraElogV(g, source, dists, (long) 2e18);
        long ans = dists[right(b)];
        out.println(ans);
    }

    int left(int i) {
        return i;
    }

    int right(int i) {
        return i + threshold;
    }
}
