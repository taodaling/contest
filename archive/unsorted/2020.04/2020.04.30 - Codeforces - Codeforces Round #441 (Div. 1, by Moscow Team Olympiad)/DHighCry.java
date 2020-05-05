package contest;

import template.binary.Bits;
import template.datastructure.IntervalBooleanMap;
import template.graph.DescartesTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerSparseTable;
import template.rand.Randomized;
import template.utils.Debug;

import java.util.Arrays;

public class DHighCry {
    int[] a;

    Debug debug = new Debug(true);
    IntegerSparseTable maxSt;
    IntegerSparseTable orSt;


    int[][] next;
    int[][] prev;
    int n;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }


        next = new int[n][30];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < 30; j++) {
                if (Bits.bitAt(a[i], j) == 1) {
                    next[i][j] = i;
                } else {
                    next[i][j] = i == n - 1 ? n : next[i + 1][j];
                }
            }
        }

        prev = new int[n][30];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 30; j++) {
                if (Bits.bitAt(a[i], j) == 1) {
                    prev[i][j] = i;
                } else {
                    prev[i][j] = i == 0 ? -1 : prev[i - 1][j];
                }
            }
        }

        DescartesTree tree = new DescartesTree(a, 0, n - 1, (x, y) -> -Integer.compare(x, y));
        long ans = dfs(tree.getRoot(), 0, n - 1);
        out.println(ans);
    }

    public long dfs(DescartesTree.Node root, int l, int r) {
        if (root == null) {
            return 0;
        }
        int index = root.index;
        long ans = dfs(root.left, l, index - 1) + dfs(root.right, index + 1, r);
        int lIndex = l - 1;
        int rIndex = r + 1;
        for (int i = 0; i < 30; i++) {
            if (Bits.bitAt(a[index], i) == 0) {
                lIndex = Math.max(prev[index][i], lIndex);
                rIndex = Math.min(next[index][i], rIndex);
            }
        }
        int leftBlock = lIndex - l + 1;
        int rightBlock = r - rIndex + 1;
        long local = (long) leftBlock * (r - index + 1) + (long) rightBlock * (index - l + 1) -
                (long) leftBlock * rightBlock;
        return ans + local;
    }
}
