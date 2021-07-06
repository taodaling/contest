package on2021_06.on2021_06_25_Library_Checker.Lowest_Common_Ancestor;



import template.graph.*;
import template.io.FastInput;
import template.io.FastOutput;

public class LowestCommonAncestor {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] p = new int[n];
        for (int i = 1; i < n; i++) {
            p[i] = in.ri();
        }
        p[0] = -1;
        ParentOnTree pot = new ParentOnTreeByFunction(n, i -> p[i]);
        DepthOnTree dot = new DepthOnTreeByParent(n, pot);
        LcaOnTree lca = new CompressedBinaryLift(n, dot, pot);
        for (int i = 0; i < q; i++) {
            int u = in.ri();
            int v = in.ri();
            out.println(lca.lca(u, v));
        }
    }
}
