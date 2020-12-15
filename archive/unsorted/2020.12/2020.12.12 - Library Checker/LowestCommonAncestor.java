package contest;

import template.graph.*;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class LowestCommonAncestor {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] p = new int[n];
        int[] depth = new int[n];
        p[0] = -1;
        depth[0] = 0;
        for (int i = 1; i < n; i++) {
            p[i] = in.ri();
            depth[i] = depth[p[i]] + 1;
        }
        KthAncestorOnTreeByBinaryLift bl = new KthAncestorOnTreeByBinaryLift(n);
        bl.init(i -> p[i], n);
        for (int i = 0; i < q; i++) {
            int a = in.ri();
            int b = in.ri();
            out.println(bl.lca(a, depth[a], b, depth[b]));
        }
    }
}
