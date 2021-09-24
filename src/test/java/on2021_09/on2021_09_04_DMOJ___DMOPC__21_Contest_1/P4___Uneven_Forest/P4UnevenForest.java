package on2021_09.on2021_09_04_DMOJ___DMOPC__21_Contest_1.P4___Uneven_Forest;



import template.algo.BinarySearch;
import template.datastructure.DSU;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

import java.util.Arrays;
import java.util.List;
import java.util.function.LongPredicate;

public class P4UnevenForest {
    Edge[][] adj;
    long[] depth;
    long diameter;
    long[][] prev;
    long[][] next;
    int[][] subtree;
    int n;

    public void prepare(int root, int p, long d) {
        depth[root] = d;
        IntegerArrayList st = new IntegerArrayList(n);
        st.add(root);
        for (Edge e : adj[root]) {
            int node = e.other(root);
            if (node == p) {
                continue;
            }
            prepare(node, root, d + e.len);
            st.addAll(subtree[node]);
        }
        subtree[root] = st.toArray();
    }

    long inf = (long) 1e18;

    public void dfs(int root, int p) {
        int curSize = 1;
        Arrays.fill(prev[root], inf);
        Arrays.fill(next[root], inf);
        prev[root][0] = 0;
        for (Edge e : adj[root]) {
            int node = e.other(root);
            if (node == p) {
                continue;
            }
            dfs(node, root);
            for (int i = 0; i < curSize; i++) {
                next[root][i] = inf;
            }
            for (int i = 0; i < curSize; i++) {
                for (int j = 0; j < subtree[node].length; j++) {
                    //del
                    next[root][i] = Math.min(next[root][i], prev[root][i] + prev[node][j] + e.cost);
                    //merge
                    long d1 = depth[subtree[root][i]];
                    long d2 = depth[subtree[node][j]];
                    if (d1 + d2 - 2 * depth[root] <= diameter) {
                        int to = d1 >= d2 ? i : curSize + j;
                        next[root][to] = Math.min(next[root][to], prev[root][i] + prev[node][j]);
                    }
                }
            }
            long[] tmp = prev[root];
            prev[root] = next[root];
            next[root] = tmp;
            curSize += subtree[node].length;
        }

        debug.debug("root", root);
        debug.debugArray("dp", prev[root]);
    }

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int m = in.ri();
        int k = in.ri();
        depth = new long[n];
        prev = new long[n][n];
        next = new long[n][n];
        subtree = new int[n][];
        List<Edge>[] edgeAdj = Graph.createGraph(n);
        DSU dsu = new DSU(n);
        dsu.init();
        for (int i = 0; i < m; i++) {
            int u = in.ri() - 1;
            int v = in.ri() - 1;
            int l = in.ri();
            int b = in.ri();
            Edge e = new Edge();
            e.a = u;
            e.b = v;
            e.len = l;
            e.cost = b;
            edgeAdj[u].add(e);
            edgeAdj[v].add(e);
            dsu.merge(u, v);
        }
        adj = new Edge[n][];
        for (int i = 0; i < n; i++) {
            adj[i] = edgeAdj[i].toArray(new Edge[0]);
        }
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == i) {
                prepare(i, -1, 0);
            }
        }

        for(int i = 0; i < n; i++){
            debug.debug("i", i);
            debug.debugArray("subset", subtree[i]);
        }

        LongPredicate predicate = mid -> {
            diameter = mid;
            long total = 0;
            for (int i = 0; i < n; i++) {
                if (dsu.find(i) != i) {
                    continue;
                }
                dfs(i, -1);
                long minCost = Arrays.stream(prev[i]).min().orElse(-1);
                total += minCost;
            }
            return total <= k;
        };
//        predicate.test(3);
        long minDiameter = BinarySearch.firstTrue(predicate, 0, (long) 1e12);
        out.println(minDiameter);
    }
}

class Edge {
    int a;
    int b;
    int len;
    int cost;

    public int other(int x) {
        return a ^ b ^ x;
    }
}
