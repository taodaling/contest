package contest;

import dp.Lis;
import graphs.lca.Lca;
import template.datastructure.PermutationNode;
import template.graph.DirectedEdge;
import template.graph.KthAncestorOnTree;
import template.graph.LcaOnTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Permutation;
import template.problem.ContinuousIntervalProblem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class EGoodSubsegments {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt() - 1;
        }

        ContinuousIntervalProblem problem = new ContinuousIntervalProblem(p);
        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;
            long ans = problem.countContinuousIntervalBetween(l, r);
            out.println(ans);
        }
    }

}

