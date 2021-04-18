package on2021_03.on2021_03_28_AtCoder___AtCoder_Regular_Contest_116.E___Spread_of_Information0;




import template.algo.BinarySearch;
import template.algo.IntBinarySearch;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SortUtils;

import javax.swing.*;
import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public class ESpreadOfInformation {
    int[][] g;
    int[] fa;
    int[] depth;
    int[] A;
    int[] B;
    int[] C;

    public void dfs(int root, int p) {
        fa[root] = p;
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        for (int node : g[root]) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
        }
    }

    int inf = (int) 1e8;

    public void dp(int root, int p, int D) {
        A[root] = 0;
        B[root] = 0;
        C[root] = inf;
        for (int node : g[root]) {
            if (node == p) {
                continue;
            }
            dp(node, root, D);
            A[root] += A[node];
            B[root] = Math.max(B[root], B[node] + 1);
            C[root] = Math.min(C[root], C[node] + 1);
        }
        if (B[root] + C[root] <= D) {
            B[root] = -1;
        }
        if (B[root] == D || B[root] >= 0 && p == -1) {
            A[root]++;
            B[root] = -1;
            C[root] = 0;
        }
    }


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] u = new int[n - 1];
        int[] v = new int[n - 1];
        A = new int[n];
        B = new int[n];
        C = new int[n];
        depth = new int[n];
        fa = new int[n];
        for (int i = 0; i < n - 1; i++) {
            u[i] = in.ri() - 1;
            v[i] = in.ri() - 1;
        }
        g = Graph.createUndirectedGraph(n, n - 1, u, v);
        dfs(0, -1);
        int[] sortedIndices = IntStream.range(0, n).toArray();
        SortUtils.quickSort(sortedIndices, (i, j) -> -Integer.compare(depth[i], depth[j]), 0, n);
        IntPredicate predicate = mid -> {
            dp(0, -1, mid);
            return A[0] <= k;
        };
        int ans = BinarySearch.firstTrue(predicate, 0, DigitUtils.ceilDiv(n, k) + 1);
        out.println(ans);
    }
}
