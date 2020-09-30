package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class ETOF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
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
            for (Node next : node.adj) {
                if (next.set == node.set) {
                    continue;
                }
                node.set.leaf = false;
            }
            node.filteredAdj = node.adj.stream()
                    .filter(x -> x.set == node.set).collect(Collectors.toList());
        }

        Deque<Node> dq = new ArrayDeque<>(n);
        int ans = n;
        for (Node node : nodes) {
            if (node.set != node || !node.leaf) {
                continue;
            }
            if (node.adj.isEmpty()) {
                continue;
            }

            int circle = (int)1e8;
            for (Node src : node.elements) {
                now++;
                dq.clear();
                visit(src);
                src.dist = 0;
                dq.addLast(src);
                outer:
                while (!dq.isEmpty()) {
                    Node head = dq.removeFirst();
                    if (head.dist + 1 >= circle) {
                        break;
                    }
                    for (Node next : head.filteredAdj) {
                        int cand = head.dist + 1;
                        visit(next);
                        if (next == src) {
                            circle = Math.min(circle, cand);
                            dq.clear();
                            continue outer;
                        }
                        if (cand < next.dist) {
                            next.dist = cand;
                            dq.addLast(next);
                        }
                    }
                }
            }

            ans += circle * 998 + 1;
        }

        out.println(ans);
    }

    int now;

    public void visit(Node node) {
        if (node.version != now) {
            node.version = now;
            node.dist = (int) 1e8;
        }
    }

    int order = 0;
    Deque<Node> stack = new ArrayDeque<>(5000);

    public void tarjan(Node root) {
        if (root.dfn != 0) {
            return;
        }
        root.dfn = root.low = ++order;
        root.instk = true;
        stack.addLast(root);

        for (Node node : root.adj) {
            tarjan(node);
            if (node.instk) {
                root.low = Math.min(root.low, node.low);
            }
        }

        if (root.low == root.dfn) {
            while (true) {
                Node tail = stack.removeLast();
                tail.instk = false;
                tail.set = root;
                root.elements.add(tail);
                if (tail == root) {
                    break;
                }
            }
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    Node set;
    int dfn;
    int low;
    boolean instk;
    boolean leaf = true;
    int dist;
    int version;
    List<Node> elements = new ArrayList<>();
    List<Node> filteredAdj = new ArrayList<>();
}
