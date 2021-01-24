package on2021_01.on2021_01_19_Codeforces___Educational_Codeforces_Round_102__Rated_for_Div__2_.F__Strange_Set;



import template.graph.DirectedEdge;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.IntegerDinic;
import template.primitve.generated.graph.IntegerMaximumCloseSubGraph;
import template.primitve.generated.graph.IntegerMaximumCloseSubGraphAdapter;

import java.util.List;

public class FStrangeSet {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        int[] b = new int[n];
        in.populate(a);
        in.populate(b);
        List<DirectedEdge>[] g = Graph.createGraph(n);
        IntegerMaximumCloseSubGraph graph = new IntegerMaximumCloseSubGraphAdapter(new IntegerDinic());
        for (int i = 0; i < n; i++) {
            boolean[] added = new boolean[101];
            for (int j = i - 1; j >= 0; j--) {
                if (a[i] % a[j] == 0 && !added[a[j]]) {
                    added[a[j]] = true;
                    Graph.addEdge(g, i, j);
                }
            }
        }
        boolean[] ans = new boolean[n];
        long w = graph.maximumCloseSubGraph(g, b, ans);
        out.println(w);
    }
}
