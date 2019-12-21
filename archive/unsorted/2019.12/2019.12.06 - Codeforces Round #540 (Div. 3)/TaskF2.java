package contest;

import template.graph.LcaOnTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

public class TaskF2 {
    MultiWayIntegerStack edges;
    int[][] dp;
    int[] color;
    int[] parent;
    boolean[] visited;
    Modular mod = new Modular(998244353);


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        edges = new MultiWayIntegerStack(n + 1, n * 2);
        dp = new int[n + 1][2];
        color = new int[n + 1];
        visited = new boolean[n + 1];
        parent = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            color[i] = in.readInt();
        }

        for (int i = 1; i <= n; i++) {
            int a = in.readInt();
            int b = in.readInt();
            edges.addLast(a, b);
            edges.addLast(b, a);
        }
        LcaOnTree lcaOnTree = new LcaOnTree(edges, 1);
        int[] lca = new int[k + 1];
        dfsForParent(1, 0);
        for (int i = 1; i <= n; i++) {
            if (color[i] == 0) {
                continue;
            }
            if (lca[color[i]] == 0) {
                lca[color[i]] = i;
            } else {
                lca[color[i]] = lcaOnTree.lca(lca[color[i]], i);
            }
        }
        for (int i = 1; i <= n; i++) {
            if (color[i] == 0) {
                continue;
            }
            if (i != lca[color[i]]) {
                paint(parent[i], lca[color[i]], color[i]);
            }
        }


        if (!valid) {
            out.println(0);
            return;
        }

        int ans = 1;
        for(int i = 1; i <= n; i++){
            if(visited[i] || color[i] != 0){
                continue;
            }
            dfs(i, 0);
            ans = mod.mul(ans, dp[i][1]);
        }

        out.println(ans);
    }

    boolean valid = true;

    public void dfsForParent(int root, int p) {
        parent[root] = p;
        for (IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            int node = iterator.next();
            if (node == p) {
                continue;
            }
            dfsForParent(node, root);
        }
    }

    public void paint(int root, int ancestor, int c) {
        if (color[root] != 0) {
            if (color[root] == c) {
                return;
            }
            valid = false;
            return;
        }
        color[root] = c;
        if (root != ancestor) {
            paint(parent[root], ancestor, c);
        }
    }

    public void dfs(int root, int p) {
        if(visited[root]){
            return;
        }
        visited[root] = true;
        if(color[root] != 0){
            dp[root][0] = 0;
            dp[root][1] = 1;
            return;
        }
        dp[root][0] = 1;
        dp[root][1] = 0;
        for(IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ){
            int node = iterator.next();
            if(node == p){
                continue;
            }
            dfs(node, root);
            int total = mod.plus(dp[node][0], dp[node][1]);
            dp[root][1] = mod.plus(mod.mul(dp[root][1], total),mod.mul(dp[root][0], dp[node][1]));
            dp[root][0] = mod.mul(dp[root][0], total);
        }
    }
}

