package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.*;

import java.util.List;

public class GridPuzzleII {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        List<LongFlowEdge>[] g = Graph.createGraph(idOfDst() + 1);
        int asum = 0;
        for (int i = 0; i < n; i++) {
            int x = in.ri();
            asum += x;
            LongFlow.addFlowEdge(g, idOfSrc(), idOfLeft(i), x);
        }
        LongFlowEdge[][] edges = new LongFlowEdge[n][n];
        int bsum = 0;
        for (int i = 0; i < n; i++) {
            int x = in.ri();
            bsum += x;
            LongFlow.addFlowEdge(g, idOfRight(i), idOfDst(), x);
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                edges[i][j] = LongFlow.addFlowEdge(g, idOfLeft(i), idOfRight(j), 1);
            }
        }

        LongMaximumFlow mcf = new LongDinic();
        long res = mcf.apply(g, idOfSrc(), idOfDst(), n * n);
        if(asum != res || bsum != res){
            out.println(-1);
            return;
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(edges[i][j].flow == 1){
                    out.append('X');
                }else{
                    out.append('.');
                }
            }
            out.println();
        }
    }

    int n;

    public int idOfLeft(int i) {
        return i;
    }

    public int idOfRight(int i) {
        return n + i;
    }

    public int idOfSrc() {
        return n * 2;
    }

    public int idOfDst() {
        return idOfSrc() + 1;
    }
}
