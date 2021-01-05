package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.IntegerDinic;
import template.primitve.generated.graph.IntegerFlow;
import template.primitve.generated.graph.IntegerFlowEdge;

import java.util.List;

public class TheGloriousKarlutkaRiver {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int m = in.ri();
        int d = in.ri();
        int w = in.ri();
        if (w <= d) {
            out.println(1);
            return;
        }
        int[][] pts = new int[n][3];
        List<IntegerFlowEdge>[] g = Graph.createGraph(dst() + 1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                pts[i][j] = in.ri();
            }
        }
        int inf = (int) 1e8;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < time; j++) {
                IntegerFlow.addFlowEdge(g, rubbishInput(i, j), rubbishOutput(i, j), pts[i][2]);
                if (pts[i][1] <= d && j >= 1) {
                    IntegerFlow.addFlowEdge(g, src(), rubbishInput(i, j), inf);
                }
//                if (w - pts[i][1] <= d) {
//                    IntegerFlow.addFlowEdge(g, rubbishOutput(i, j), dst(), inf);
//                }
            }
            for (int t = 0; t + 1 < time; t++) {
                IntegerFlow.addFlowEdge(g, rubbishOutput(i, t), rubbishInput(i, t + 1), inf);
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int dx = pts[i][0] - pts[j][0];
                int dy = pts[i][1] - pts[j][1];
                if (dx * dx + dy * dy <= d * d) {
                    for (int k = 0; k + 1 < time; k++) {
                        IntegerFlow.addFlowEdge(g, rubbishOutput(i, k), rubbishInput(j, k + 1), inf);
                    }
                }
            }
        }
        IntegerDinic dinic = new IntegerDinic();
        int now = 0;
        int remain = m;
        while (now + 1 < time && remain > 0) {
            now++;
            for (int i = 0; i < n; i++) {
                if (w - pts[i][1] <= d) {
                    IntegerFlow.addFlowEdge(g, rubbishOutput(i, now), dst(), inf);
                }
            }
            remain -= dinic.apply(g, src(), dst(), remain);
        }
        if(remain == 0){
            out.println(now + 1);
        }else{
            out.println("IMPOSSIBLE");
        }
    }

    int time = 100;

    public int rubbishInput(int i, int t) {
        return (i * time + t) * 2;
    }

    public int rubbishOutput(int i, int t) {
        return (i * time + t) * 2 + 1;
    }

    int n;

    public int src() {
        return (n * time) * 2;
    }

    public int dst() {
        return src() + 1;
    }

}
