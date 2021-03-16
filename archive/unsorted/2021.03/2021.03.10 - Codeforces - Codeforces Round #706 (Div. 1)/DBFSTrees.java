package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;


public class DBFSTrees {
    int mod = 998244353;
    int inf = (int) 1e8;

    int intersect(Node[] a, Node[] b) {
        int as = a.length - 1;
        int bs = b.length - 1;
        int ans = 0;
        while (as >= 0 && bs >= 0) {
            if (a[as].id == b[bs].id) {
                ans++;
                as--;
                bs--;
                continue;
            }
            if(a[as].id < b[bs].id){
                bs--;
            }else{
                as--;
            }
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        Node[][][] bs = new Node[n][n][];
        for (int i = 0; i < n; i++) {
            bfs(nodes[i], nodes);
            for (int j = 0; j < n; j++) {
                bs[i][j] = nodes[j].prev.toArray(new Node[0]);
                Arrays.sort(bs[i][j], Comparator.comparingInt(x -> x.id));
            }
        }

        long[][] ans = new long[n][n];
        for (int i = 0; i < n; i++) {
            bfs(nodes[i], nodes);
            for (int j = i; j < n; j++) {
                if (nodes[j].dup) {
                    continue;
                }
                now++;
                dfs(nodes[j]);
                long prod = 1;
                for (Node node : nodes) {
                    if (node.version == now) {
                        continue;
                    }
                    prod = prod * intersect(bs[i][node.id], bs[j][node.id]) % mod;
                    if (prod == 0) {
                        break;
                    }
                }
                ans[i][j] = ans[j][i] = prod;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.append(ans[i][j]).append(' ');
            }
            out.println();
        }
    }

    int now = 0;

    public void dfs(Node root) {
        root.version = now;
        if (!root.prev.isEmpty()) {
            dfs(root.prev.get(0));
        }
    }

    Deque<Node> dq = new ArrayDeque<>();

    public void bfs(Node root, Node[] nodes) {
        for (Node node : nodes) {
            node.dist = inf;
            node.dup = false;
            node.prev.clear();
        }
        root.dist = 0;
        dq.clear();
        dq.addLast(root);
        while (!dq.isEmpty()) {
            Node head = dq.removeFirst();
            if (head.prev.size() > 1) {
                head.dup = true;
            }
            for (Node node : head.adj) {
                if (node.dist > head.dist + 1) {
                    node.dist = head.dist + 1;
                    dq.addLast(node);
                }
                if (node.dist == head.dist + 1) {
                    node.prev.add(head);
                    if (head.dup) {
                        node.dup = true;
                    }
                }
            }
        }
    }
}

class Node {
    int id;
    int dist;
    List<Node> prev = new ArrayList<>();
    boolean dup;
    List<Node> adj = new ArrayList<>();
    int version;
}
