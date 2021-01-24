package contest;

import template.graph.*;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class NecessaryCities {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            Graph.addUndirectedEdge(g, in.ri() - 1, in.ri() - 1);
        }
        DfsTree dt = new DfsTree(n);
        dt.init(g);
        CutVertexes cv = new CutVertexes(n);
        cv.init(dt);
        int cnt = 0;
        for(int i = 0; i < n; i++){
            if(cv.cut[i]){
                cnt++;
            }
        }
        out.println(cnt);
        for(int i = 0; i < n; i++){
            if(cv.cut[i]){
                out.append(i + 1).append(' ');
            }
        }
    }
}
