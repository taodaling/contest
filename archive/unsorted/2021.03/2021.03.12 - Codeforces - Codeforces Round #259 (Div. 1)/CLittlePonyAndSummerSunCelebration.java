package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CLittlePonyAndSummerSunCelebration {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        DSU dsu = new DSU(n);
        dsu.init();
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
            dsu.merge(a.id, b.id);
        }
        Node root = null;
        for (int i = 0; i < n; i++) {
            nodes[i].odd = in.ri() == 1;
            if (nodes[i].odd) {
                root = nodes[i];
            }
        }
        if (root == null) {
            out.println(0);
            return;
        }
        for (Node node : nodes) {
            if (node.odd) {
                if (dsu.find(root.id) != dsu.find(node.id)) {
                    out.println(-1);
                    return;
                }
            }
        }

        dfs(root, null);

        eulerPath(root);
        SequenceUtils.reverse(seq);
        out.println(seq.size());
        for (Node node : seq) {
            out.append(node.id + 1).append(' ');
        }
    }

    List<Node> seq = new ArrayList<>((int) 4e5);

    public void eulerPath(Node root) {
        while (!root.dq.isEmpty()) {
            eulerPath(root.dq.removeFirst());
        }
        seq.add(root);
    }

    public void dfs(Node root, Node p) {
        root.visited = true;
        for (Node node : root.adj) {
            if (node.visited) {
                continue;
            }
            dfs(node, root);
            for (int i = 0; i < node.deg; i++) {
                node.dq.addLast(root);
            }
            root.deg += node.deg;
        }
        if (root.odd) {
            root.deg++;
        }
        root.deg %= 2;
        if (root.deg == 0) {
            root.deg = 2;
        }
        if (p != null) {
            for (int i = 0; i < root.deg; i++) {
                p.dq.add(root);
            }
        } else {
            if (root.deg == 2) {
                root.dq.peekLast().dq.removeLast();
            }
        }
    }
}

class Node {
    int id;
    boolean visited;
    List<Node> adj = new ArrayList<>();
    Deque<Node> dq = new ArrayDeque<>();
    boolean odd;
    int deg;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
