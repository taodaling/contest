package contest;

import template.graph.DirectedEdge;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.graph.IntegerWeightDirectedEdge;
import template.primitve.generated.graph.IntegerWeightGraph;
import template.rand.RandomWrapper;

import java.util.Arrays;
import java.util.List;

public class AAtCoderJumper {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        for (int i = 0; i < n; i++) {
            out.append((i * 2) % n + 1).append(' ')
                    .append((i * 2 + 1) % n + 1).println();
        }
    }

}
