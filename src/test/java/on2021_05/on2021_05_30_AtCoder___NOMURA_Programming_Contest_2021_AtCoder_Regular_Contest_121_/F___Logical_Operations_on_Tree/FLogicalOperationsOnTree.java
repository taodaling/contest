package on2021_05.on2021_05_30_AtCoder___NOMURA_Programming_Contest_2021_AtCoder_Regular_Contest_121_.F___Logical_Operations_on_Tree;



import org.omg.PortableServer.POA;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Power;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FLogicalOperationsOnTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for(int i = 0; i < n; i++){
            nodes[i] = new Node();

        }
        for(int i = 0; i < n - 1; i++){
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        dfs(nodes[0], null);
        long[] dp = nodes[0].dp;
        long total = power.pow(2, 2 * n - 1);
        long ans = total - dp[0];
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
    int mod = 998244353;
    Power power = new Power(mod);
    public void dfs(Node root, Node p){
        long[] prev = new long[2];
        long[] next = new long[2];
        prev[0] = 1;
        prev[1] = 1;
        for(Node node : root.adj){
            if(node == p){
                continue;
            }
            dfs(node, root);
            Arrays.fill(next, 0);
            //or edge
            for(int i = 0; i < 2; i++){
                next[i] += prev[i] * node.dp[0] % mod;
            }
            //and edge
            for(int i = 0; i < 2; i++){
                for(int j = 0; j < 2; j++){
                    next[i & j] += prev[i] * node.dp[j] % mod;
                }
            }
            for(int i = 0; i < 2; i++){
                next[i] %= mod;
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }

        root.dp = prev;
    }

}

class Node {
    List<Node> adj = new ArrayList<>();
    long[] dp;
}
