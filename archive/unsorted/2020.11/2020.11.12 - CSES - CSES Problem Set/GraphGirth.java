package contest;

import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.graph.UndirectedOneWeightMinCircle;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.List;

public class GraphGirth {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        for(int i = 0; i < m; i++){
            Graph.addUndirectedEdge(g, in.readInt() - 1, in.readInt() - 1);
        }
        IntegerArrayList list = new IntegerArrayList(n);
        UndirectedOneWeightMinCircle circle = new UndirectedOneWeightMinCircle(g);
        int inf = (int)1e9;
        int ans = inf;
        for(int i = 0; i < n; i++) {
            circle.findCircle(i, list, ans);
            if(list.isEmpty()){
                continue;
            }
            ans = Math.min(list.size(), ans);
        }
        out.println(ans == inf ? -1 : ans);
    }
}
