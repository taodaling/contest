package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Buffer;
import template.utils.Debug;

import java.util.Arrays;

public class CPetersonPolyglot {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < n - 1; i++) {
            Node u = nodes[in.readInt() - 1];
            Node v = nodes[in.readInt() - 1];
            int e = in.readChar() - 'a';
            u.next[e] = v;
        }

        dfs(nodes[0]);
        reduce = new int[n];

        mergeOnTree(nodes[0], 0);
        int index = 0;
        for (int i = 0; i < n; i++) {
            if (reduce[i] > reduce[index]) {
                index = i;
            }
        }

        debug.debug("reduce", reduce);
        out.println(n - reduce[index]);
        out.println(index + 1);
    }

    int[] reduce;
    int charset = 'z' - 'a' + 1;

    public void dfs(Node root) {
        if (root == null) {
            return;
        }
        for (Node node : root.next) {
            dfs(node);
        }
        root.pushUp();
    }

    public void mergeOnTree(Node root, int d) {
        if (root == null) {
            return;
        }
        for (Node node : root.next) {
            mergeOnTree(node, d + 1);
        }
        Node virtual = buf.alloc();
        virtual.virtual = true;
        for (Node node1 : root.next) {
            if (node1 == null) {
                continue;
            }
            for (int j = 0; j < charset; j++) {
                Node node2 = node1.next[j];
                if (node2 == null) {
                    continue;
                }
                virtual.next[j] = merge(virtual.next[j], node2);
            }
        }

        virtual.pushUp();
        reduce[d] += root.size - virtual.size;
        release(virtual);
    }

    public int sizeOf(Node root) {
        if (root == null) {
            return 0;
        }
        return root.size;
    }

    public Node merge(Node a, Node b) {
        if (sizeOf(a) > sizeOf(b)) {
            return merge0(b, a);
        } else {
            return merge0(a, b);
        }
    }

    public Node merge0(Node a, Node b) {
        if (b == null) {
            return a;
        }
        if (a == null) {
            return b;
        }
        Node clone = buf.alloc();
        clone.virtual = true;
        for (int i = 0; i < charset; i++) {
            clone.next[i] = merge0(a.next[i], b.next[i]);
        }
        clone.pushUp();
        return clone;
    }

    public void release(Node root) {
        if (root == null || !root.virtual) {
            return;
        }
        for (Node node : root.next) {
            release(node);
        }
        buf.release(root);
    }

    Buffer<Node> buf = new Buffer<>(Node::new, x -> {
        Arrays.fill(x.next, null);
    }, (int) 3e5);
}

class Node {
    Node[] next = new Node['z' - 'a' + 1];
    int size;
    boolean virtual;


    public void pushUp() {
        size = 1;
        for (Node node : next) {
            if (node != null) {
                size += node.size;
            }
        }
    }
}