package contest;

import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskG {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }
        Edge[] es = new Edge[n];
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            Edge e = new Edge();
            es[i] = e;
            e.a = a;
            e.b = b;
            a.next.add(e);
            b.next.add(e);
        }
        IntBinarySearch bs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                dfs(nodes[1], null, mid);
                return nodes[1].cnt <= k;
            }
        };
        int ans = bs.binarySearch(1, n - 1);
        bs.check(ans);
        out.println(ans);
        paint(nodes[1], null, ans);
        for (int i = 1; i < n; i++) {
            out.append(es[i].color + 1).append(' ');
        }
    }

    public void paint(Node root, Edge p, int c) {
        int color = 0;
        if (p != null) {
            color = p.color + 1;
        }
        for (Edge e : root.next) {
            Node node = e.other(root);
            if (e == p) {
                continue;
            }
            e.color = color % c;
            color++;
            paint(node, e, c);
        }
    }

    public void dfs(Node root, Edge p, int c) {
        root.cnt = 0;
        if (root.next.size() > c) {
            root.cnt++;
        }
        for (Edge e : root.next) {
            Node node = e.other(root);
            if (e == p) {
                continue;
            }
            dfs(node, e, c);
            root.cnt += node.cnt;
        }
    }
}

class Edge {
    Node a;
    Node b;

    Node other(Node x) {
        return a == x ? b : a;
    }

    int color;
}

class Node {
    List<Edge> next = new ArrayList<>();
    int cnt;
}
