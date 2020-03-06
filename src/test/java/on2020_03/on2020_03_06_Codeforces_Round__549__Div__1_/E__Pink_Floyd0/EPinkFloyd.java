package on2020_03.on2020_03_06_Codeforces_Round__549__Div__1_.E__Pink_Floyd0;



import template.datastructure.CircularLinkedNode;
import template.datastructure.LinkedListBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class EPinkFloyd {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        int n = in.readInt();
        int m = in.readInt();

        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            addEdge(a, b);
        }

        for (int i = 1; i <= n; i++) {
            tarjan(nodes[i]);
        }

        Deque<Node> dq = new ArrayDeque<>(n);
        for (int i = 1; i <= n; i++) {
            for (Node node : nodes[i].out) {
                if (node.set == nodes[i].set) {
                    continue;
                }
                node.set.indeg++;
            }
        }

        for (int i = 1; i <= n; i++) {
            if (nodes[i] == nodes[i].set && nodes[i].indeg == 0) {
                dq.addLast(nodes[i]);
            }
        }

        while (dq.size() > 1) {
            debug.debug("nodes", nodes);
            Node a = dq.removeFirst();
            Node b = dq.removeFirst();

            Node x = a.dq.removeFirst();
            Node y = b.dq.removeFirst();

            if (ask(x, y) == x) {
                a.dq.addFirst(x);
            } else {
                b.dq.addFirst(y);
            }

            addBack(a, dq);
            addBack(b, dq);
        }

        Node node = dq.peekFirst().dq.peekFirst();
        out.append("! ").append(node.id).println().flush();
    }

    public void addBack(Node x, Deque<Node> dq) {
        if (!x.dq.isEmpty()) {
            dq.addFirst(x);
            return;
        }
        for (Node root : x.all) {
            for (Node node : root.out) {
                if (node.set == x) {
                    continue;
                }
                node.set.indeg--;
                if (node.set.indeg == 0) {
                    dq.addLast(node.set);
                }
            }
        }
    }

    Deque<Node> stk = new ArrayDeque<>(100000);
    int order = 0;

    public void tarjan(Node root) {
        if (root.dfn != 0) {
            return;
        }
        root.dfn = root.low = ++order;
        root.instk = true;
        stk.addLast(root);
        for (Node node : root.out) {
            tarjan(node);
            if (node.instk && node.low < root.low) {
                root.low = node.low;
            }
        }

        if (root.low == root.dfn) {
            while (true) {
                Node last = stk.removeLast();
                last.set = root;
                last.instk = false;
                root.all.add(last);
                if (last == root) {
                    break;
                }
            }
            root.dq.addAll(root.all);
        }
    }

    public void addEdge(Node a, Node b) {
        a.out.add(b);
    }

    FastOutput out;
    FastInput in;

    public Node ask(Node a, Node b) {
        out.append("? ").append(a.id).append(' ').append(b.id).println().flush();
        return in.readInt() == 1 ? a : b;
    }
}

class Node extends CircularLinkedNode<Node> {
    int id;
    int indeg;
    List<Node> out = new ArrayList<>();
    List<Node> all = new ArrayList<>();
    Deque<Node> dq = new ArrayDeque<>();
    Node set;
    int dfn;
    int low;
    boolean instk;

    @Override
    public String toString() {
        return "" + id + ":" + indeg;
    }
}
