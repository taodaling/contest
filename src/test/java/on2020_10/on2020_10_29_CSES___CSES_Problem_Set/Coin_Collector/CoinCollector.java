package on2020_10.on2020_10_29_CSES___CSES_Problem_Set.Coin_Collector;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;

public class CoinCollector {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].coins = in.readInt();
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
        }

        for (Node node : nodes) {
            tarjan(node);
        }
        for (Node node : nodes) {
            node.set.totalCoins += node.coins;
            for (Node x : node.adj) {
                if (x.set != node.set) {
                    node.set.virtualEdges.add(x.set);
                }
            }
        }

        long ans = 0;
        for(Node node : nodes){
            ans = Math.max(ans, find(node));
        }
        out.println(ans);
    }

    public long find(Node root) {
        if (root.dp == -1) {
            root.dp = 0;
            root.dp += root.totalCoins;
            long max = 0;
            for (Node node : root.virtualEdges) {
                max = Math.max(max, find(node));
            }
            root.dp += max;
        }
        return root.dp;
    }

    int order = 0;
    Deque<Node> dq = new ArrayDeque<>();

    public void tarjan(Node root) {
        if (root.dfn != 0) {
            return;
        }
        root.dfn = root.low = ++order;
        root.instk = true;
        dq.addLast(root);
        for (Node node : root.adj) {
            tarjan(node);
            if (node.instk) {
                root.low = Math.min(root.low, node.low);
            }
        }
        if (root.low == root.dfn) {
            while (true) {
                Node tail = dq.removeLast();
                tail.instk = false;
                tail.set = root;
                if (tail == root) {
                    break;
                }
            }
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int dfn;
    int low;
    boolean instk;
    int id;
    Node set;

    List<Node> virtualEdges = new ArrayList<>();
    long coins;
    long totalCoins;

    long dp = -1;
}

