package on2021_03.on2021_03_28_AtCoder___AtCoder_Regular_Contest_116.E___Spread_of_Information;



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
    boolean[] covered;
    int[] depth;
    int[] dist;

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

    public void dfsForRange(int root, int d, int p) {
        if (d <= dist[root]) {
            return;
        }
        dist[root] = d;
        covered[root] = true;
        for (int node : g[root]) {
            if (node == p) {
                continue;
            }
            dfsForRange(node, d - 1, root);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] u = new int[n - 1];
        int[] v = new int[n - 1];
        dist = new int[n];
        depth = new int[n];
        covered = new boolean[n];
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
            int req = 0;
            Arrays.fill(covered, false);
            Arrays.fill(dist, -1);
            for (int node : sortedIndices) {
                if (covered[node]) {
                    continue;
                }
                req++;
                if (req > k) {
                    return false;
                }
                for (int i = 0; i < mid && fa[node] != -1; i++) {
                    node = fa[node];
                }
                dfsForRange(node, mid, -1);
            }
            return req <= k;
        };
        int ans = BinarySearch.firstTrue(predicate, 0, DigitUtils.ceilDiv(n, k) + 1);
        out.println(ans);
    }
}
