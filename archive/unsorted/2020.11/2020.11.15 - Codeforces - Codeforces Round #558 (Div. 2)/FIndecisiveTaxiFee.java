package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.UndirectedModifiableMinDistProblem;

public class FIndecisiveTaxiFee {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int q = in.readInt();
        UndirectedModifiableMinDistProblem problem = new UndirectedModifiableMinDistProblem(n, m, 0, n - 1);
        for (int i = 0; i < m; i++) {
            problem.addEdge(in.readInt() - 1, in.readInt() - 1, in.readInt());
        }
        for (int i = 0; i < q; i++) {
            int t = in.readInt() - 1;
            int x = in.readInt();
            long ans = problem.queryOnModifyEdge(t, x);
            out.println(ans);
        }
    }
}
