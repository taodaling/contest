package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.ArrayList;
import java.util.List;

public class CJohnnySolving {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.next.add(b);
            b.next.add(a);
        }

        dfs(nodes[0], null, 0);
        Node maxDepth = null;
        for (Node node : nodes) {
            if (maxDepth == null || maxDepth.depth < node.depth) {
                maxDepth = node;
            }
        }

        List<Node> trace = new ArrayList<>(n);
        if (maxDepth.depth >= DigitUtils.ceilDiv(n, k)) {
            up(maxDepth, nodes[0], trace);
            out.println("PATH");
            out.println(trace.size());
            for (Node node : trace) {
                out.append(node.id + 1).append(' ');
            }
            return;
        }

        out.println("CYCLES");
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            if (!nodes[i].leaf) {
                continue;
            }
            cnt++;
            if (cnt > k) {
                continue;
            }
            Node a = nodes[i].next.get(0);
            Node b = nodes[i].next.get(1);
            trace.clear();
            if ((nodes[i].depth - a.depth + 1) % 3 != 0) {
                up(nodes[i], a, trace);
            } else if ((nodes[i].depth - b.depth + 1) % 3 != 0) {
                up(nodes[i], b, trace);
            } else {
                trace.add(nodes[i]);
                if (a.depth < b.depth) {
                    Node tmp = a;
                    a = b;
                    b = tmp;
                }
                up(a, b, trace);
            }
            out.println(trace.size());
            for (Node node : trace) {
                out.append(node.id + 1).append(' ');
            }
            out.println();
        }
    }

    public void up(Node root, Node target, List<Node> trace) {
        trace.add(root);
        if (target != root) {
            up(root.p, target, trace);
        }
    }

    public void dfs(Node root, Node p, int d) {
        root.depth = d;
        root.p = p;
        root.next.remove(p);

        for (Node node : root.next) {
            if (node.depth == -1) {
                dfs(node, root, d + 1);
                root.leaf = false;
            }
        }
    }
}

class Node {
    Node p;
    List<Node> next = new ArrayList<>();
    boolean leaf = true;
    int depth = -1;
    int id;
}