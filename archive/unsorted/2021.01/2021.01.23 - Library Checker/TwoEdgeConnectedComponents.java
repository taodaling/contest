package contest;

import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.graph.UndirectedTarjanSCC;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class TwoEdgeConnectedComponents {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        for(int i = 0; i < m; i++){
            int a = in.ri();
            int b = in.ri();
            Graph.addUndirectedEdge(g, a, b);
        }
        UndirectedTarjanSCC scc = new UndirectedTarjanSCC(n);
        scc.init(g);
        List<Integer>[] ans = Graph.createGraph(n);
        int sets = 0;
        for(int i = 0; i < n; i++){
            if(i == scc.set[i]){
                sets++;
            }
            ans[scc.set[i]].add(i);
        }
        out.println(sets);
        for(List<Integer> list : ans){
            if(list.isEmpty()){
                continue;
            }
            out.append(list.size()).append(' ');
            for(int x : list){
                out.append(x).append(' ');
            }
            out.println();
        }
    }
}
