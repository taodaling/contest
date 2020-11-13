package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Factorial;
import template.math.Power;

import java.util.ArrayList;
import java.util.List;

public class FDistributingIntegers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        dfsForSize(nodes[0], null);
        dfs(nodes[0], null, 1, n);
        for(Node node : nodes){
            out.println(node.ans);
        }
    }

    int mod = (int) 1e9 + 7;
    Factorial fact = new Factorial((int) 1e6, mod);
    Combination comb = new Combination(fact);
    Power power = new Power(mod);

    public void dfs(Node root, Node p, long topWay, int total) {
        long common = 1;
        for (Node node : root.adj) {
            if (node == p) {
                common = common * topWay % mod * fact.invFact(total - root.size) % mod;
                continue;
            }
            common = common * node.way % mod * fact.invFact(node.size) % mod;
        }
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            long local = common * fact.fact(total - 1 - node.size) % mod * fact.fact(node.size) % mod
                    * power.inverse((int) node.way) % mod;
            dfs(node, root, local, total);
        }

        root.ans = fact.fact(total - 1);
        for (Node node : root.adj) {
            if (node == p) {
                root.ans = root.ans * topWay % mod * fact.invFact(total - root.size) % mod;
                continue;
            }
            root.ans = root.ans * node.way % mod * fact.invFact(node.size) % mod;
        }
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
        root.way = fact.fact(root.size - 1);
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            root.way = root.way * node.way % mod * fact.invFact(node.size) % mod;
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    long ans;
    int size;
    long way;
}
