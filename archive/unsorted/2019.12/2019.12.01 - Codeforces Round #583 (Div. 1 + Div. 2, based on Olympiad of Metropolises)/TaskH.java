package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskH {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }
        findDiameter(nodes[1], null, 0);
        List<Node> trace = new ArrayList<>(n);
        dfsForTrace(nodes[1].a, null, 0, nodes[1].diameter, trace);

        int half = trace.size() / 2;
        Node a = trace.get(half - 1);
        Node b = trace.get(half);
        a.next.remove(b);
        b.next.remove(a);
        paint(a, b, 0, k, -1);
        paint(b, a, 1, k, 1);
        verify(a, b, 0, k);
        verify(b, a, 0, k);
        if (!valid) {
            out.println("No");
            return;
        }
        out.println("Yes");
        for (int i = 1; i <= n; i++) {
            out.append(nodes[i].color + 1).append(' ');
        }
    }

    boolean valid = true;

    public void verify(Node root, Node p, int depth, int k) {
        root.depth = depth;
        root.depthest = root;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            verify(node, root, depth + 1, k);
            if (root.depthest != root && node.depthest.depth + root.depthest.depth - depth * 2 + 1 >= k && k > 2) {
                valid = false;
            }
            if (node.depthest.depth > root.depthest.depth) {
                root.depthest = node.depthest;
            }
        }
    }

    public void paint(Node root, Node p, int color, int k, int step) {
        root.color = DigitUtils.mod(color, k);
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            paint(node, root, color + step, k, step);
        }
    }

    public boolean dfsForTrace(Node root, Node p, int depth, int diameter, List<Node> trace) {
        trace.add(root);
        if (depth == diameter) {
            return true;
        }
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            if (dfsForTrace(node, root, depth + 1, diameter, trace)) {
                return true;
            }
        }
        trace.remove(trace.size() - 1);
        return false;
    }

    public void findDiameter(Node root, Node p, int depth) {
        root.depth = depth;
        root.depthest = root;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            findDiameter(node, root, depth + 1);
            if (root.diameter < node.diameter) {
                root.diameter = node.diameter;
                root.a = node.a;
            }
            if (root.depthest.depth + node.depthest.depth - root.depth * 2 > root.diameter) {
                root.diameter = root.depthest.depth + node.depthest.depth - 2 * root.depth;
                root.a = root.depthest;
            }
            if (root.depthest.depth < node.depthest.depth) {
                root.depthest = node.depthest;
            }
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int color;
    Node depthest;
    int depth;
    int diameter;
    Node a;
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}
