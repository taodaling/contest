package on2021_10.on2021_10_11_DMOJ___DMOPC__21_Contest_2.P5___Permutations0;





import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Factorial;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class P5Permutations {
    int mod = 998244352;
    Debug debug = new Debug(false);
    int C = 30;
    void dfs(Node root, Node p) {

        debug.debug("enter root", root);
        long[][] prev = new long[C][root.size];
        long[][] next = new long[C][root.size];
        prev[0][0] = 1;
        int size = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            for (int j = 0; j < C; j++) {
                for (int k = 0; k < size; k++) {
                    next[j][k] = 0;
                }
            }
            dfs(node, root);
            long[][] nodeDp = node.dp;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j <= node.size; j++) {
                    if(nodeDp[i][j] == 0){
                        continue;
                    }
                    for (int t = 0; t < C; t++) {
                        for (int k = 0; k < size; k++) {
                            if(prev[t][k] == 0){
                                continue;
                            }
                            int nt = t + i;
                            int nk = k + j;
                            if(nt < C) {
                                next[nt][nk] += prev[t][k] * nodeDp[i][j] % mod;
                            }
                        }
                    }
                }
            }
            size += node.size;
            for (int t = 0; t < C; t++) {
                for (int k = 0; k < size; k++) {
                    next[t][k] %= mod;
                }
            }
            long[][] tmp = prev;
            prev = next;
            next = tmp;
        }

        long[][] dp = new long[2][size + 1];
        for (int i = 0; i < 2; i++) {
            for (int t = 0; t < C; t++) {
                for (int k = 0; k < size; k++) {
                    dp[i][k + i] += prev[t][k] * fact[i + t] % mod;
                }
            }
        }
        for(int i = 0; i < 2; i++){
            for(int j = 0; j <= size; j++){
                dp[i][j] %= mod;
            }
        }
        root.dp = dp;

        debug.debug("leave root", root);
    }

    long[] fact = new long[(int)1e5];
    {
        fact[0] = 1;
        for(int i = 1; i < fact.length; i++){
            fact[i] = fact[i - 1] * i % mod;
        }
        assert fact[C] == 0;
    }

    void dfsForSize(Node root, Node p) {
        root.size = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForSize(node, root);
            root.size += node.size;
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        Node[] nodes = new Node[n];
        for(int i = 0; i < n; i++){
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        dfsForSize(nodes[0], null);
        dfs(nodes[0], null);
        long[][] dp = nodes[0].dp;
        long ans = dp[0][k];
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}

class Node {
    int id;
    long[][] dp;
    List<Node> adj = new ArrayList<>();
    int size;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
