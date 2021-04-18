package on2021_03.on2021_03_25_Codeforces___Codeforces_Round__236__Div__1_.C__Strictly_Positive_Matrix;



import template.graph.DirectedEdge;
import template.graph.DirectedTarjanSCC;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class CStrictlyPositiveMatrix {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] mat = new int[n][n];
        List<DirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (in.ri() > 0) {
                    Graph.addEdge(g, j, i);
                }
            }
        }
        DirectedTarjanSCC scc = new DirectedTarjanSCC(n);
        scc.init(g);
        for(int i = 0; i < n; i++){
            if(scc.set[i] != scc.set[0]){
                out.println("NO");
                return;
            }
        }
        out.println("YES");
    }
}
