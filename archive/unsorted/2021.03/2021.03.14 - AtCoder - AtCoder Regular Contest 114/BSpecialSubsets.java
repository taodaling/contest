package contest;

import template.graph.DirectedEdge;
import template.graph.DirectedTarjanSCC;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Power;

import java.util.Arrays;
import java.util.List;

public class BSpecialSubsets {
    int mod = 998244353;
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] f = new int[n];
        for (int i = 0; i < n; i++) {
            f[i] = in.ri() - 1;
        }
        List<DirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < n; i++) {
            Graph.addEdge(g, i, f[i]);
        }
        DirectedTarjanSCC scc = new DirectedTarjanSCC(n);
        scc.init(g);
        int[] set = scc.set;
        boolean[] isLeaf = new boolean[n];
        Arrays.fill(isLeaf, true);
        for (int i = 0; i < n; i++) {
            for (DirectedEdge e : g[i]) {
                if (set[e.to] == set[i]) {
                    continue;
                }
                isLeaf[set[i]] = false;
            }
        }
        int leafCnt = 0;
        for (int i = 0; i < n; i++) {
            if (set[i] == i && isLeaf[i]) {
                leafCnt++;
            }
        }
        int ans = pow.pow(2, leafCnt) - 1;
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
