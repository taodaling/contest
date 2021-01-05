package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.*;
import template.utils.Debug;

import java.util.List;

public class BPerspective {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int[] w = new int[n];
        in.populate(w);
        int[] r = new int[n];
        in.populate(r);
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.ri();
                r[i] -= mat[i][j];
            }
        }
        w[0] += r[0];
        int cap = w[0];
        for (int x : mat[0]) {
            cap += x;
        }
        for (int i = 1; i < n; i++) {
            if (w[i] > cap) {
                out.println("NO");
                return;
            }
        }
        int total = 0;
        int inf = (int) 1e9;
        List<IntegerFlowEdge>[] g = Graph.createGraph(idOfDst() + 1);
        for (int i = 1; i < n; i++) {
            IntegerFlow.addFlowEdge(g, idOfNode(i), idOfDst(), cap - w[i]);
            for (int j = 1; j < i; j++) {
                total += mat[i][j];
                IntegerFlow.addFlowEdge(g, idOfEdge(i, j), idOfNode(i), inf);
                IntegerFlow.addFlowEdge(g, idOfEdge(i, j), idOfNode(j), inf);
                IntegerFlow.addFlowEdge(g, idOfSrc(), idOfEdge(i, j), mat[i][j]);
            }
        }
        debug.debug("total", total);
        debug.debugMatrix("mat", mat);

        IntegerMaximumFlow mf = new IntegerDinic();
        int ans = mf.apply(g, idOfSrc(), idOfDst(), total);
        out.println(ans == total ? "YES" : "NO");
    }

    int n;

    public int idOfNode(int i) {
        return i;
    }

    public int idOfEdge(int i, int j) {
        return n + i * n + j;
    }

    public int idOfSrc() {
        return n + n * n;
    }

    public int idOfDst() {
        return idOfSrc() + 1;
    }

}
