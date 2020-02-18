package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerIterator;
import template.primitve.generated.IntegerMultiWayStack;

import java.util.Arrays;

public class FUpAndDownTheTree {
    int[][] dp;
    IntegerMultiWayStack edges;
    int[] depths;
    int[] nearestDepths;
    int[] degrees;
    boolean[] isLeaves;
    int k;
    int inf = (int) 1e8;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        k = in.readInt();
        dp = new int[2][n];
        edges = new IntegerMultiWayStack(n, 2 * n);
        depths = new int[n];
        nearestDepths = new int[n];
        degrees = new int[n];
        isLeaves = new boolean[n];

        for (int i = 1; i < n; i++) {
            int p = in.readInt() - 1;
            edges.addLast(i, p);
            edges.addLast(p, i);
            degrees[i]++;
            degrees[p]++;
        }
        for (int i = 1; i < n; i++) {
            if (degrees[i] == 1) {
                isLeaves[i] = true;
            }
        }

        dfsForDepth(0, -1, 0);
        dfsForDp(0, -1);

        int ans = dp[0][0];
        out.println(ans);
    }

    public void dfsForDepth(int root, int p, int depth) {
        depths[root] = depth;
        nearestDepths[root] = inf;
        if (isLeaves[root]) {
            nearestDepths[root] = depth;
        }
        for (IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            int node = iterator.next();
            if (node == p) {
                continue;
            }
            dfsForDepth(node, root, depth + 1);
            nearestDepths[root] = Math.min(nearestDepths[root], nearestDepths[node]);
        }
    }

    public void dfsForDp(int root, int p) {
        int maxDiff = 0;
        if (isLeaves[root]) {
            dp[1][root] = 1;
        }
        for (IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            int node = iterator.next();
            if (node == p) {
                continue;
            }
            dfsForDp(node, root);
            maxDiff = Math.max(dp[0][node] - dp[1][node], maxDiff);
            dp[1][root] += dp[1][node];
        }
        dp[0][root] = dp[1][root] + maxDiff;
        if (nearestDepths[root] - (depths[root] - 1) > k) {
            dp[1][root] = 0;
        }
    }
}
