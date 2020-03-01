package contest;

import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.List;

public class FShrinkingTree {
    List<UndirectedEdge>[] g;
    double[][] dp;
    double[][] f;
    double[][] comp;
    int[] size;

    public double comp(int n, int m) {
        if (n < 0 || m < 0) {
            return 0;
        }
        if (comp[n][m] == -1) {
            if (n < m) {
                return comp[n][m] = 0;
            }
            if (m == 0) {
                return comp[n][m] = 1;
            }
            comp[n][m] = comp(n - 1, m - 1) * n / m;
        }
        return comp[n][m];
    }

    double[] fact;
    public double fact(int i){
        if(i < 0){
            return 0;
        }
        if(fact[i] == -1){
            if(i == 0){
                return fact[i] = 1;
            }
            fact[i] = i * fact(i - 1);
        }
        return fact[i];
    }

    int n;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        g = Graph.createUndirectedGraph(n);
        for (int i = 1; i < n; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            Graph.addUndirectedEdge(g, a, b);
        }
        dp = new double[n][n];
        f = new double[n][n];
        comp = new double[n][n];
        size = new int[n];
        fact = new double[n];
        SequenceUtils.deepFill(fact, -1D);
        SequenceUtils.deepFill(comp, -1D);
        for (int i = 0; i < n; i++) {
            SequenceUtils.deepFill(dp, 0D);
            SequenceUtils.deepFill(f, 0D);
            Arrays.fill(size, 0);
            dfs(i, -1);
            double ans = dp[i][0] / fact(n - 1) / Math.pow(2, n - 1);
            out.println(ans);
        }

    }


    public void dfs(int root, int p) {
        size[root] = 1;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root);
            size[root] += size[e.to];
        }

        int sum = 0;
        dp[root][0] = 1;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            double[] former = dp[root];
            dp[root] = new double[n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; i + j < n; j++) {
                    dp[root][i + j] += comp(i + j, i) * comp(sum - i + size[e.to] - j, sum - i) * former[i] * f[e.to][j];
                }
            }
            sum += size[e.to];
        }

        double[] suffix = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            suffix[i] = dp[root][i];
            if (i + 1 < n) {
                suffix[i] += suffix[i + 1];
            }
        }
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                f[root][i] += 2 * dp[root][i - 1] * i;
            }
            f[root][i] += suffix[i];
        }
    }

}
