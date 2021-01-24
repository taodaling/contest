package contest;

import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.graph.UndirectedTarjanSCC;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class NecessaryRoads {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        List<UndirectedEdge> edges = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            edges.add(Graph.addUndirectedEdge(g, in.ri() - 1, in.ri() - 1));
        }
        UndirectedTarjanSCC scc = new UndirectedTarjanSCC(n);
        scc.init(g);
        List<UndirectedEdge> ans = new ArrayList<>(m);
        for(UndirectedEdge e : edges){
            if(scc.set[e.to] == scc.set[e.rev.to]){
                continue;
            }
            ans.add(e);
        }
        out.println(ans.size());
        for(UndirectedEdge e : ans){
            out.append(e.rev.to + 1).append(' ').append(e.to + 1).println();
        }
    }
}
