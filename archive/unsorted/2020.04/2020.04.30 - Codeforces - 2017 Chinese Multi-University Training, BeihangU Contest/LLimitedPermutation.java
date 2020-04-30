package contest;

import template.graph.DescartesTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Factorial;
import template.math.Modular;

public class LLimitedPermutation {
    Interval[] intervals;
    Modular mod = new Modular(1e9 + 7);
    Factorial fact = new Factorial(1000000, mod);
    Combination comb = new Combination(fact);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        if (!in.hasMore()) {
            throw new UnknownError();
        }
        out.printf("Case #%d: ", testNumber);
        int n = in.readInt();
        intervals = new Interval[n];
        valid = true;
        for (int i = 0; i < n; i++) {
            intervals[i] = new Interval();
            intervals[i].l = in.readInt() - 1;
        }
        for (int i = 0; i < n; i++) {
            intervals[i].r = in.readInt() - 1;
        }

        DescartesTree tree = new DescartesTree(intervals, 0, n - 1, (a, b) ->
                -Integer.compare((a.r - a.l), (b.r - b.l)));
        check(tree.getRoot(), 0, n - 1);
        if (!valid) {
            out.println(0);
            return;
        }

        int ans = dfs(tree.getRoot());
        out.println(ans);
    }

    boolean valid = true;

    public void check(DescartesTree.Node root, int l, int r) {
        if (l > r) {
            valid = valid && root == null;
            return;
        }
        valid = valid && intervals[root.index].l == l && intervals[root.index].r == r;
        check(root.left, l, root.index - 1);
        check(root.right, root.index + 1, r);
    }

    public int dfs(DescartesTree.Node root) {
        if (root == null) {
            return 1;
        }
        int lWay = dfs(root.left);
        int rWay = dfs(root.right);
        int ans = mod.mul(lWay, rWay);
        int range = intervals[root.index].r - intervals[root.index].l;
        int lRange = root.index - intervals[root.index].l;
        ans = mod.mul(ans, comb.combination(range, lRange));
        return ans;
    }
}

class Interval {
    int l;
    int r;

    @Override
    public String toString() {
        return String.format("[%d, %d]", l, r);
    }
}