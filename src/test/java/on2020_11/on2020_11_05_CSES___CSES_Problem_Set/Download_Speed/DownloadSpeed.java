package on2020_11.on2020_11_05_CSES___CSES_Problem_Set.Download_Speed;



import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongDinic;
import template.primitve.generated.graph.LongFlow;
import template.primitve.generated.graph.LongFlowEdge;

import java.util.List;

public class DownloadSpeed {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        List<LongFlowEdge>[] g = Graph.createGraph(n);
        for(int i = 0; i < m; i++){
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            int c = in.readInt();
            LongFlow.addFlowEdge(g, a, b, c);
        }
        LongDinic dinic = new LongDinic();
        long ans = dinic.apply(g, 0, n - 1, (long)1e18);
        out.println(ans);
    }
}
