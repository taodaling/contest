package on2020_09.on2020_09_01_Library_Checker.Matching_on_Bipartite_Graph;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.IntegerDinic;
import template.primitve.generated.graph.IntegerFlow;
import template.primitve.generated.graph.IntegerFlowEdge;

import java.util.List;

public class MatchingOnBipartiteGraph {
    int l;
    int r;

    public int getL(int i) {
        return i;
    }

    public int getR(int i) {
        return l + i;
    }

    public int getSrc() {
        return l + r;
    }

    public int getDst() {
        return getSrc() + 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        l = in.readInt();
        r = in.readInt();
        int m = in.readInt();

        IntegerFlowEdge[] edges = new IntegerFlowEdge[m];
        List<IntegerFlowEdge>[] g = IntegerFlow.createFlow(getDst() + 1);
        for (int i = 0; i < m; i++) {
            int a = in.readInt();
            int b = in.readInt();
            edges[i] = IntegerFlow.addEdge(g, getL(a), getR(b), 1);
        }

        for (int i = 0; i < l; i++) {
            IntegerFlow.addEdge(g, getSrc(), getL(i), 1);
        }

        for (int i = 0; i < r; i++) {
            IntegerFlow.addEdge(g, getR(i), getDst(), 1);
        }

        int inf = (int) 1e9;
        IntegerDinic dinic = new IntegerDinic();
        int match = dinic.apply(g, getSrc(), getDst(), inf);
        out.println(match);

        for(IntegerFlowEdge e : edges){
            if(e.flow == 1){
                out.append(e.rev.to).append(' ').append(e.to - l).println();
            }
        }
    }
}
