package contest;

import template.datastructure.DSU;
import template.datastructure.XorDeltaDSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class DHamiltonianSpanningTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long x = in.rl();
        long y = in.rl();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        XorDeltaDSU dsu = new XorDeltaDSU(n);
        dsu.init();
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
            dsu.merge(a.id, b.id, 1);
        }
        if (x < y) {
            dfs(nodes[0], null);
            int path = nodes[0].open + nodes[0].close;
            long ans = x * (n - path) + (path - 1) * y;
            out.println(ans);
        } else {
            int[] cnts = new int[2];
            for (int i = 0; i < n; i++) {
                cnts[dsu.delta(i, 0)]++;
            }
            long ans = y * (n - 2);
            if (Math.min(cnts[0], cnts[1]) == 1) {
                ans += x;
            } else {
                ans += y;
            }
            out.println(ans);
        }
    }


    void dfs(Node root, Node p) {
        int openCnt = 0;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            root.close += node.close;
            openCnt += node.open;
        }
        if (openCnt >= 2) {
            root.close += openCnt - 2;
            openCnt = 2;
        }
        if (openCnt == 2) {
            root.close++;
        } else if (openCnt == 1) {
            root.open = 1;
        } else {
            root.open = 1;
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int close;
    int open;
    int id;
}
