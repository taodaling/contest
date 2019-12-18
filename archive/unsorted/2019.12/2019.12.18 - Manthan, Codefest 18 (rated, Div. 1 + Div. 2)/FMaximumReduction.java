package contest;

import template.graph.DescartesTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Modular;

public class FMaximumReduction {
    int[] as;
    int k;
    int n;
    Modular mod = new Modular(1e9 + 7);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        k = in.readInt() - 1;

        as = new int[n];
        for (int i = 0; i < n; i++) {
            as[i] = in.readInt();
        }

        DescartesTree tree = new DescartesTree(as, 0, n - 1, (a, b) -> -Integer.compare(a, b));
        dfs(tree.getRoot(), 0, n - 1);

        out.println(ans);
    }

    int ans = 0;

    public void dfs(DescartesTree.Node root, int l, int r) {
        if (root == null) {
            return;
        }

        if (l + k > r) {
            return;
        }

        int rightLen = r - (root.index + 1) + 1 + 1;
        int leftLen = root.index - l;
        int l0 = DigitUtils.ceilDiv(rightLen, k) * k - rightLen;
        int l1 = k - l0;
        if (l0 == 0) {
            l0 = l1;
            l1 = 0;
        }
        int d = rightLen / k;

        int blockContri = mod.mul(leftLen / k, mod.plus(mod.mul(l0, d), mod.mul(l1, d + 1)));

        int extraContri = 0;
        if (leftLen % k <= l0) {
            extraContri = mod.mul(leftLen % k, d);
        } else {
            extraContri = mod.plus(mod.mul(l0, d), mod.mul(leftLen % k - l0, d + 1));
        }

        //index + kt <= r => t <= (r - index) / k
        if (root.index + k <= r) {
            extraContri = mod.plus(extraContri, (r - root.index) / k);
        }

        int localContri = mod.plus(blockContri, extraContri);
        //System.err.println("f(" + as[root.index] + ") = " + localContri);
        localContri = mod.mul(localContri, as[root.index]);
        ans = mod.plus(ans, localContri);

        dfs(root.left, l, root.index - 1);
        dfs(root.right, root.index + 1, r);
    }
}
