package contest;



import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class TaskE {
    MultiWayIntegerStack edges;
    int[] a;
    long[][] negDp;
    long[] posDp;
    int[] sizes;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }
        edges = new MultiWayIntegerStack(n + 1, n * 2);
        for (int i = 1; i < n; i++) {
            int a = in.readInt();
            int b = in.readInt();
            edges.addLast(a, b);
            edges.addLast(b, a);
        }
        negDp = new long[n + 1][];
        posDp = new long[n + 1];
        sizes = new int[n + 1];

        dfsForSize(1, 0);
        dfs(1, 0);
        long ans = posDp[1];
        for (int i = 0; i < n; i++) {
            if (negDp[1][i] < 0) {
                ans = Math.min(ans, i);
                break;
            }
        }
        out.println(ans);
    }

    public void dfsForSize(int root, int p) {
        sizes[root] = 1;
        for (IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            int node = iterator.next();
            if (node == p) {
                continue;
            }
            dfsForSize(node, root);
            sizes[root] += sizes[node];
        }
    }

    public void dfs(int root, int p) {
        posDp[root] = a[root] > 0 ? 0 : (long) 1e18;
        int total = 1;

        long[] last = new long[sizes[root]];
        long[] next = new long[sizes[root]];
        last[0] = a[root];
        for (IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            int node = iterator.next();
            if (node == p) {
                continue;
            }
            dfs(node, root);
            long atLeastNeed = posDp[node];
            long cutAtLeast = posDp[node] + 1;
            for (int i = 0; i < sizes[node]; i++) {
                if (negDp[node][i] < 0) {
                    atLeastNeed = Math.min(atLeastNeed, i + 1);
                    cutAtLeast = Math.min(cutAtLeast, i + 1);
                    break;
                }
            }
            posDp[root] += atLeastNeed;
            for (int i = 0; i < total + sizes[node]; i++) {
                next[i] = (long) 1e18;
                if (i >= cutAtLeast && i - cutAtLeast < total) {
                    next[i] = Math.min(next[i], last[(int) (i - cutAtLeast)]);
                }
            }
            for (int i = 0; i < total; i++) {
                for (int j = 0; j < sizes[node]; j++) {
                    next[i + j] = Math.min(next[i + j], last[i] + negDp[node][j]);
                }
            }

            total += sizes[node];
            {
                long[] tmp = next;
                next = last;
                last = tmp;
            }
        }
        negDp[root] = last;
    }
}