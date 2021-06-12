package on2021_06.on2021_06_06_Luogu.P5633_________;



import template.graph.Graph;
import template.graph.KDegreeMinimumSpanningTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongWeightGraph;
import template.primitve.generated.graph.LongWeightUndirectedEdge;

import java.util.List;

public class P5633 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int s = in.ri() - 1;
        int k = in.ri();

        List<LongWeightUndirectedEdge>[] g = Graph.createGraph(n);
        for(int i = 0; i < m; i++){
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            long w = in.rl();
            LongWeightGraph.addUndirectedEdge(g, a, b, w);
        }

        KDegreeMinimumSpanningTree st = new KDegreeMinimumSpanningTree(g, s);
        while(st.next() && st.getTree()[s].size() < k){
        }

        if(st.getTree() == null || st.getTree()[s].size() != k){
            out.println("Impossible");
            return;
        }
        out.println(st.getTotalWeight());
    }
}
