package template;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TwoSat {
    public static class Node {
        List<Node> outEdge = new ArrayList();
        List<Node> inEdge = new ArrayList();
        int id;
        Node inverse;
        Node head;
        Node next;
        int dfn;
        int low;
        boolean instack;
        int value;
        int relyOn;

        @Override
        public String toString() {
            return "" + id;
        }
    }

    Node[][] nodes;
    Deque<Node> deque;
    int n;

    public TwoSat(int n) {
        this.n = n;
        deque = new ArrayDeque(2 * n);
        nodes = new Node[2][n + 1];
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= n; j++) {
                nodes[i][j] = new Node();
                nodes[i][j].id = i == 0 ? -j : j;
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= n; j++) {
                nodes[i][j].inverse = nodes[1 - i][j];
            }
        }
        reset(n);
    }

    void reset(int n) {
        this.n = n;
        order = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= n; j++) {
                nodes[i][j].dfn = -1;
                nodes[i][j].outEdge.clear();
                nodes[i][j].inEdge.clear();
                nodes[i][j].head = null;
                nodes[i][j].value = -1;
                nodes[i][j].next = null;
                nodes[i][j].relyOn = 0;
            }
        }
    }

    public Node getElement(int i) {
        return nodes[1][i];
    }

    public Node getNotElement(int i) {
        return nodes[0][i];
    }

    private void addEdge(Node a, Node b) {
        a.outEdge.add(b);
        b.inEdge.add(a);
    }

    public void alwaysTrue(Node node) {
        addEdge(node.inverse, node);
    }

    public void alwaysFalse(Node node) {
        addEdge(node, node.inverse);
    }

    /**
     * a && b
     */
    public void and(Node a, Node b) {
        alwaysTrue(a);
        alwaysTrue(b);
    }

    /**
     * a || b
     */
    public void or(Node a, Node b) {
        addEdge(a.inverse, b);
        addEdge(b.inverse, a);
    }

    /**
     * a -> b
     */
    public void deduce(Node a, Node b) {
        or(a.inverse, b);
    }

    /**
     * a == false || b == false
     */
    public void atLeastOneIsFalse(Node a, Node b) {
        or(a.inverse, b.inverse);
    }

    /**
     * a ^ b
     */
    public void xor(Node a, Node b) {
        notEqual(a, b);
    }

    /**
     * a != b
     */
    public void notEqual(Node a, Node b) {
        same(a, b.inverse);
    }

    /**
     * a == b
     */
    public void same(Node a, Node b) {
        addEdge(a, b);
        addEdge(b, a);
        addEdge(a.inverse, b.inverse);
        addEdge(b.inverse, a.inverse);
    }

    public boolean valueOf(int i) {
        return nodes[1][i].value == 1;
    }

    public boolean solve(boolean fetchValue) {
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= n; j++) {
                tarjan(nodes[i][j]);
            }
        }
        for (int i = 1; i <= n; i++) {
            if (nodes[0][i].head == nodes[1][i].head) {
                return false;
            }
        }

        if (!fetchValue) {
            return true;
        }

        // Topological sort
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= n; j++) {
                for (Node node : nodes[i][j].outEdge) {
                    if (node.head != nodes[i][j].head) {
                        nodes[i][j].head.relyOn++;
                    }
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= n; j++) {
                if (nodes[i][j].head == nodes[i][j] && nodes[i][j].relyOn == 0) {
                    deque.addLast(nodes[i][j]);
                }
            }
        }

        while (!deque.isEmpty()) {
            Node head = deque.removeFirst();
            if (head.inverse.value != -1) {
                head.value = 0;
            } else {
                head.value = 1;
            }
            for (Node trace = head; trace != null; trace = trace.next) {
                trace.value = head.value;
                for (Node node : trace.inEdge) {
                    if (node.head == head) {
                        continue;
                    }
                    node.head.relyOn--;
                    if (node.head.relyOn == 0) {
                        deque.addLast(node.head);
                    }
                }
            }
        }

        return true;
    }

    int order;

    private void tarjan(Node root) {
        if (root.dfn >= 0) {
            return;
        }
        root.low = root.dfn = order++;
        deque.addLast(root);
        root.instack = true;
        for (Node node : root.outEdge) {
            tarjan(node);
            if (node.instack) {
                root.low = Math.min(root.low, node.low);
            }
        }
        if (root.dfn == root.low) {
            while (true) {
                Node head = deque.removeLast();
                head.instack = false;
                head.head = root;
                if (head == root) {
                    break;
                }
                head.next = root.next;
                root.next = head;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            builder.append(valueOf(i)).append(' ');
        }
        return builder.toString();
    }
}
