package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class FlightRoutesCheck {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
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
            for (Node x : node.adj) {
                if (x.set != node.set) {
                    out.println("NO");
                    out.append(x.id + 1).append(' ').append(node.id + 1);
                    return;
                }
            }
        }
        for (int i = 1; i < n; i++) {
            if (nodes[i].set != nodes[0].set) {
                out.println("NO");
                out.append(i + 1).append(' ').append(1);
                return;
            }
        }

        out.println("YES");
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
}
