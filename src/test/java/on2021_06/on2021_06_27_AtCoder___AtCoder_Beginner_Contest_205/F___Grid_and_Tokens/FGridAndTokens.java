package on2021_06.on2021_06_27_AtCoder___AtCoder_Beginner_Contest_205.F___Grid_and_Tokens;



import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.*;

import java.util.List;

public class FGridAndTokens {
    int h;
    int w;
    int n;

    public int row(int i) {
        return i;
    }

    public int col(int i) {
        return h + i;
    }

    public int lnode(int i) {
        return h + w + i;
    }

    public int rnode(int i) {
        return lnode(i) + n;
    }

    public int src() {
        return h + w + n * 2;
    }

    public int dst() {
        return src() + 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        h = in.ri();
        w = in.ri();
        n = in.ri();
        int[] a = new int[n];
        int[] b = new int[n];
        int[] c = new int[n];
        int[] d = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.ri() - 1;
            c[i] = in.ri() - 1;
            b[i] = in.ri() - 1;
            d[i] = in.ri() - 1;
        }
        List<IntegerFlowEdge>[] g = Graph.createGraph(dst() + 1);
        for (int i = 0; i < h; i++) {
            IntegerFlow.addFlowEdge(g, src(), row(i), 1);
        }
        for (int i = 0; i < w; i++) {
            IntegerFlow.addFlowEdge(g, col(i), dst(), 1);
        }
        for (int i = 0; i < n; i++) {
            for (int j = a[i]; j <= b[i]; j++) {
                IntegerFlow.addFlowEdge(g, row(j), lnode(i), 1);
            }
            for (int j = c[i]; j <= d[i]; j++) {
                IntegerFlow.addFlowEdge(g, rnode(i), col(j), 1);
            }
            IntegerFlow.addFlowEdge(g, lnode(i), rnode(i), 1);
        }
        IntegerMaximumFlow mf = new IntegerHLPP();
        int f = mf.apply(g, src(), dst(), n);
        out.println(f);
    }
}
