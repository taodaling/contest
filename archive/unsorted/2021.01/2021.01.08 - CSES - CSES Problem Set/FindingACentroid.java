package contest;

import template.graph.Centroid;
import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class FindingACentroid {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            Graph.addUndirectedEdge(g, a, b);
        }
        Centroid centroid = new Centroid(n);
        List<Integer> ans = new ArrayList<>(2);
        centroid.findCentroid(g, ans::add);
        out.println(ans.get(0) + 1);
    }
}
