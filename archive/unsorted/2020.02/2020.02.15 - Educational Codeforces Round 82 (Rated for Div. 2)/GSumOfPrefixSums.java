package contest;

import template.geometry.LongConvexHullTrick;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class GSumOfPrefixSums {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.next.add(b);
            b.next.add(a);
        }
        for (int i = 0; i < n; i++) {
            nodes[i].w = in.readInt();
        }

        dac(nodes[0], n * 2);
        out.println(ans);
    }

    long ans = 0;

    private void prepareForAdd(Node root) {
        root.a = root.w;
        root.b = root.w;
        root.c = 1;
    }

    private void prepareForTest(Node root) {
        root.a = 0;
        root.b = 0;
        root.c = 0;
    }

    LongConvexHullTrick cht = new LongConvexHullTrick();

    private void dac(Node root, int psize) {
        ans = Math.max(ans, root.w);
        dfsForSize(root, null);
        if (root.size == 1) {
            return;
        }
        int n = root.size;
        if (n * 2 > psize) {
            throw new RuntimeException();
        }
        root = dfsForCentroid(root, null, n);
        cht.clear();
        prepareForAdd(root);
        cht.insert(root.b, root.a);
        for (Node node : root.next) {
            prepareForTest(root);
            dfsForTest(node, root);
            prepareForAdd(root);
            dfsForAdd(node, root);
        }
        cht.clear();
        prepareForAdd(root);
        cht.insert(root.b, root.a);
        SequenceUtils.reverse(root.next);
        for (Node node : root.next) {
            prepareForTest(root);
            dfsForTest(node, root);
            prepareForAdd(root);
            dfsForAdd(node, root);
        }

        for (Node node : root.next) {
            node.next.remove(root);
            dac(node, n);
        }
    }

    private void dfsForTest(Node root, Node p) {
        root.a = p.a + p.b + root.w;
        root.b = p.b + root.w;
        root.c = p.c + 1;
        int size = 0;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfsForTest(node, root);
            size++;
        }
        if (size == 0) {
            ans = Math.max(ans, root.a);
            LongConvexHullTrick.Line line = cht.queryLine(root.c);
            if (line != null) {
                ans = Math.max(ans, line.y(root.c) + root.a);
            }
        }
    }

    private void dfsForAdd(Node root, Node p) {
        root.a = p.a + root.w * (1 + p.c);
        root.b = p.b + root.w;
        root.c = p.c + 1;
        int size = 0;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfsForAdd(node, root);
            size++;
        }
        if (size == 0) {
            cht.insert(root.b, root.a);
            ans = Math.max(ans, root.a);
        }
    }

    private Node dfsForCentroid(Node root, Node p, int total) {
        int size = 0;
        for (Node node : root.next) {
            if (node == p) {
                size = Math.max(size, total - root.size);
            } else {
                size = Math.max(size, node.size);
            }
        }
        if (size <= total / 2) {
            return root;
        }
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            Node ans = dfsForCentroid(node, root, total);
            if (ans != null) {
                return ans;
            }
        }
        return null;
    }

    private void dfsForSize(Node root, Node p) {
        root.size = 1;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfsForSize(node, root);
            root.size += node.size;
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    long w;
    int size;

    long a;
    long b;
    long c;

    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
