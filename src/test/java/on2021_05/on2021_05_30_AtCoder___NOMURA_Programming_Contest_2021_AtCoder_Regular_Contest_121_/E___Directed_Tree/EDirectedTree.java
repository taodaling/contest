package on2021_05.on2021_05_30_AtCoder___NOMURA_Programming_Contest_2021_AtCoder_Regular_Contest_121_.E___Directed_Tree;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Factorial;
import template.math.Factorization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EDirectedTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i < n; i++) {
            Node p = nodes[in.ri() - 1];
            p.adj.add(nodes[i]);
        }
        dfsForSize(nodes[0], null);
        dp(nodes[0], null);
        long[] dp = nodes[0].dp;
        long ans = 0;
        for(int i = 0; i <= n; i++){
            long contrib = dp[i] * fact.fact(n - i) % mod;
            if(i % 2 == 1){
                contrib = -contrib;
            }
            ans += contrib;
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }

    public void dfsForSize(Node root, Node p) {
        root.size = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForSize(node, root);
            root.size += node.size;
        }
    }

    int mod = 998244353;
    Factorial fact = new Factorial((int)1e5, mod);
    public void dp(Node root, Node p) {
        long[] prev = new long[root.size + 1];
        long[] next = new long[root.size + 1];
        int known = 0;
        prev[0] = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dp(node, root);
            for(int i = 0; i <= known; i++){
                next[i] = 0;
            }
            for(int i = 0; i <= known; i++){
                for(int j = 0; j <= node.size; j++){
                    next[i + j] += prev[i] * node.dp[j] % mod;
                }
            }
            known += node.size;
            for(int i = 0; i <= known; i++){
                next[i] %= mod;
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }
        Arrays.fill(next, 0);
        int descent = root.size - 1;
        for(int i = 0; i < root.size; i++){
            next[i + 1] += prev[i] * (descent - i) % mod;
            next[i] += prev[i];
        }
        for(int i = 0; i <= root.size; i++){
            next[i] %= mod;
        }
        root.dp = next;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    long[] dp;
    int size;
}
