package on2020_09.on2020_09_04_Codeforces___Codeforces_Round__364__Div__1_.D__Huffman_Coding_on_Segment;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Buffer;

import java.util.Deque;
import java.util.TreeSet;

public class DHuffmanCodingOnSegment {
    public void solve(int testNumber, FastInput in, FastOutput out) {
    }
}

class Automaton {
    Node[] nodes;
    Node root;
    TreeSet<Node> set = new TreeSet<>((a, b) -> Integer.compare(a.size, b.size));
    long sum = 0;

    Buffer<Node> buf = new Buffer<>(Node::new, x -> {
        x.l = x.r = null;
        x.p = null;
    });

    public Automaton(int n) {
        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
    }

    private Node merge(Node a, Node b) {
        Node fa = buf.alloc();
        fa.l = a;
        fa.r = b;
        a.p = fa;
        b.p = fa;
        fa.pushUp();
        return fa;
    }

    public void afterInc(Node root) {
        if (root == null) {
            return;
        }
        root.pushUp();
        afterInc(root.p);
        if (root.p == null || root.p.p == null) {
            return;
        }
        Node other = root.p.p.other(root.p);
        if (root.size <= other.size) {
            return;
        }
        Node brother = root.p.other(root);
    }

    public void add(int x) {
        Node update = nodes[x];
        update.size++;
        if (nodes[x].size == 1) {
            if (root == null) {
                root = nodes[x];
                sum = 1;
                set.add(root);
                return;
            }
            Node first = set.first();
            Node p = first.p;
            Node fa = merge(nodes[x], first);
            if (p != null) {
                p.replace(first, fa);
            } else {
                root = fa;
            }
            update = fa;
        }

    }
}

class Node {
    Node l;
    Node r;
    Node p;
    int size;

    Node other(Node x) {
        return l == x ? r : l;
    }

    public void replace(Node x, Node y) {
        if (l == x) {
            l = y;
        } else {
            r = y;
        }
    }


    public void pushUp() {
        if (l == null) {
            return;
        }
        size = l.size + r.size;
    }
}