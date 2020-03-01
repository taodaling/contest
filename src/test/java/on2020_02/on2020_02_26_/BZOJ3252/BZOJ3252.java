package on2020_02.on2020_02_26_.BZOJ3252;



import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.problem.TreePickSpecialPointProblem;

import java.util.Arrays;
import java.util.List;

public class BZOJ3252 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        boolean[] choice = new boolean[n];
        Arrays.fill(choice, true);
        List<UndirectedEdge>[] g = Graph.createUndirectedGraph(n);
        long[] w = new long[n];
        for (int i = 0; i < n; i++) {
            w[i] = in.readInt();
        }
        for (int i = 1; i < n; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            Graph.addUndirectedEdge(g, a, b);
            choice[a] = false;
        }
        TreePickSpecialPointProblem problem = new TreePickSpecialPointProblem(n);
        boolean[] ans = new boolean[n];
        long best = problem.applyNotMoreThan(g, w, 0, choice, ans, k);
        out.println(best);
    }
}
