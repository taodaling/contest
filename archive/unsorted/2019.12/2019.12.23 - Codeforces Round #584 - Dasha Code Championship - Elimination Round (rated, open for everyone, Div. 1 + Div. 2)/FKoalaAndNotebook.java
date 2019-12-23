package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedLog2;
import template.math.DigitBase;
import template.math.Modular;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class FKoalaAndNotebook {


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        DigitBase base = new DigitBase(10);
        for (int i = 1; i <= m; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.len = i;
            a.next.add(e);
            b.next.add(e);
        }

        StringTree tree = new StringTree(0, 9, 1000000);
        TreeSet<Node> set = new TreeSet<>((a, b) -> {
            int ans = StringTree.TrieNode.compare(a.t, b.t);
            if (ans == 0) {
                ans = a.id - b.id;
            }
            return ans;
        });
        nodes[1].t = tree.getRoot();
        StringTree.TrieNode inf = tree.getMax();
        for (int i = 2; i <= n; i++) {
            nodes[i].t = inf;
        }

        set.add(nodes[1]);
        while (!set.isEmpty()) {
            Node head = set.pollFirst();
            for (Edge e : head.next) {
                Node node = e.other(head);
                StringTree.TrieNode trie = insert(tree, head.t, e.len);
                if (StringTree.TrieNode.compare(node.t, trie) <= 0) {
                    continue;
                }
                set.remove(node);
                node.t = trie;
                set.add(node);
            }
        }

        for (int i = 2; i <= n; i++) {
            out.println(nodes[i].t.r);
        }
    }

    public StringTree.TrieNode insert(StringTree tree, StringTree.TrieNode root, int digit) {
        if (digit == 0) {
            return root;
        }
        root = insert(tree, root, digit / 10);
        return tree.insert(root, digit % 10);
    }
}

class Edge {
    Node a;
    Node b;
    int len;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> next = new ArrayList<>();
    StringTree.TrieNode t;
    int id;
}

class StringTree {

    static Modular mod = new Modular(1e9 + 7);
    int l;
    int r;
    int log;
    TrieNode root;
    TrieNode max;
    TrieNode min;

    public TrieNode getRoot() {
        return root;
    }

    public TrieNode getMax() {
        return max;
    }

    public TrieNode getMin() {
        return min;
    }

    public StringTree(int l, int r, int maxDepth) {
        if (maxDepth == Integer.MAX_VALUE) {
            maxDepth--;
        }

        this.l = l;
        this.r = r;
        log = CachedLog2.ceilLog(maxDepth + 1);
        root = newTrieNode();
        max = newTrieNode(root, 0);
        min = newTrieNode(root, 0);
        max.depth = maxDepth + 1;
        min.depth = -1;
    }

    private TrieNode newTrieNode() {
        TrieNode ans = new TrieNode();
        ans.next = new TrieNode[r - l + 1];
        ans.jump = new TrieNode[log + 1];
        return ans;
    }

    public TrieNode newTrieNode(TrieNode p, int c) {
        TrieNode node = newTrieNode();
        node.depth = p.depth + 1;
        node.value = c;
        node.jump[0] = p;
        node.r = mod.valueOf(p.r * 10L + c);
        for (int i = 0; node.jump[i] != null; i++) {
            node.jump[i + 1] = node.jump[i].jump[i];
        }
        return node;
    }

    public TrieNode insert(TrieNode root, int digit) {
        if (root.next[digit - l] == null) {
            root.next[digit - l] = newTrieNode(root, digit);
        }
        return root.next[digit - l];
    }

    public static class TrieNode {
        int depth;
        int value;
        TrieNode[] next;
        TrieNode[] jump;
        int r;

        public int getValue() {
            return value;
        }

        public TrieNode getParent() {
            return jump[0];
        }

        public static int compare(TrieNode a, TrieNode b) {
            if (a.depth != b.depth) {
                return a.depth - b.depth;
            }
            if (a == b) {
                return 0;
            }
            TrieNode lca = lca(a, b);
            a = gotoDepth(a, lca.depth + 1);
            b = gotoDepth(b, lca.depth + 1);
            return a.value - b.value;
        }

        public static TrieNode lca(TrieNode a, TrieNode b) {
            if (a.depth > b.depth) {
                TrieNode tmp = a;
                a = b;
                b = tmp;
            }
            b = gotoDepth(b, a.depth);
            if (a == b) {
                return a;
            }
            for (int i = 20; i >= 0; i--) {
                if (a.jump[i] == b.jump[i]) {
                    continue;
                }
                a = a.jump[i];
                b = b.jump[i];
            }
            return a.jump[0];
        }

        public static TrieNode gotoDepth(TrieNode t, int d) {
            if (t.depth == d) {
                return t;
            }
            int log = CachedLog2.floorLog(t.depth - d);
            return gotoDepth(t.jump[log], d);
        }

        @Override
        public String toString() {
            return jump[0] == null ? "" : jump[0].toString() + value;
        }
    }
}