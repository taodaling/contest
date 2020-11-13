package contest;

import template.graph.DirectedEdge;
import template.graph.DominatorTree;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.List;

public class CriticalCities {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        List<DirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            Graph.addEdge(g, in.readInt() - 1, in.readInt() - 1);
        }
        DominatorTree dt = new DominatorTree(g, 0);
        int cur = n - 1;
        IntegerArrayList list = new IntegerArrayList(n);
        while(true) {
            list.add(cur);
            if(cur == 0){
                break;
            }
            cur = dt.parent(cur);
        }
        list.sort();
        out.println(list.size());
        for(int x : list.toArray()){
            out.println(x + 1);
        }
    }
}
