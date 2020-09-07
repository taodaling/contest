package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CBreakUp {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int s = in.readInt() - 1;
        int t = in.readInt() - 1;
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        List<Edge> cand = new ArrayList<>();
        DSU dsu = new DSU(n);
        dsu.reset();
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            Edge e = new Edge();
            e.w = in.readInt();
            e.id = i;
            e.a = nodes[a];
            e.b = nodes[b];
            e.a.adj.add(e);
            e.b.adj.add(e);
            if (dsu.find(a) == dsu.find(b)) {
                continue;
            }
            dsu.merge(a, b);
            cand.add(e);
        }

        if (dsu.find(s) != dsu.find(t)) {
            out.println(0).println(0);
            return;
        }

        long cost = (long) 1e18;
        List<Edge> ans = new ArrayList<>();
        for (Edge e : cand) {
            e.deleted = true;
            order = 0;
            for (Node node : nodes) {
                node.dfn = node.low = 0;
                node.instk = false;
                node.set = null;
                node.w = null;
                node.visited = false;
            }
            tarjan(nodes[s], null);
            if (nodes[s].set == nodes[t].set) {
            } else if (nodes[t].set == null) {
                if (cost > e.w) {
                    cost = e.w;
                    ans.clear();
                    ans.add(e);
                }
            } else {
                findPath(nodes[s], null, null);
                if (cost > nodes[t].w.w + e.w) {
                    cost = nodes[t].w.w + e.w;
                    ans.clear();
                    ans.add(e);
                    ans.add(nodes[t].w);
                }
            }
            e.deleted = false;
        }

        if (ans.isEmpty()) {
            out.println(-1);
            return;
        }
        out.println(cost).println(ans.size());
        ans.sort((a, b) -> Integer.compare(a.id, b.id));
        for (Edge e : ans) {
            out.append(e.id + 1).append(' ');
        }
    }

    Deque<Node> dq = new ArrayDeque<>();

    int order = 0;

    public Edge min(Edge a, Edge b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return a.w < b.w ? a : b;
    }

    public void findPath(Node root, Edge p, Edge cost) {
        if (root.visited) {
            return;
        }
        root.visited = true;
        root.w = cost;
        for (Edge e : root.adj) {
            if (e == p || e.deleted) {
                continue;
            }
            Node node = e.other(root);
            if (node.set == root.set) {
                findPath(node, e, cost);
            } else {
                findPath(node, e, min(e, cost));
            }
        }
    }

    public void tarjan(Node root, Edge p) {
        if (root.dfn != 0) {
            return;
        }
        root.dfn = root.low = ++order;
        root.instk = true;
        dq.addLast(root);

        for (Edge e : root.adj) {
            if (e == p || e.deleted) {
                continue;
            }
            Node node = e.other(root);
            tarjan(node, e);
            if (node.instk) {
                root.low = Math.min(root.low, node.low);
            }
        }
        if (root.low == root.dfn) {
            while (true) {
                Node tail = dq.removeLast();
                tail.set = root;
                tail.instk = false;
                if (tail == root) {
                    break;
                }
            }
        }
    }
}

class Edge {
    Node a;
    Node b;
    boolean deleted;
    int w;
    int id;

    Node other(Node x) {
        return a == x ? b : a;
    }

    @Override
    public String toString() {
        return "(" + a + ", " + b + ")";
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    Node set;
    boolean visited;
    Edge w;
    int dfn;
    int low;
    boolean instk;
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}