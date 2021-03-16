package template.datastructure;

import template.binary.Log2;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * range min query, O(n) prehandle, O(1) query
 */
public class RMQ {
    private IntegerMultiWayStack stack;
    private LcaOnTree lca;
    private Deque deque;
    private Node[] nodes;
    private int offset;

    public RMQ(RMQ model) {
        this.stack = model.stack;
        this.deque = model.deque;
        this.nodes = model.nodes;
        this.lca = new LcaOnTree(this.nodes.length);
    }

    public RMQ(int n) {
        stack = new IntegerMultiWayStack(n, n - 1);
        lca = new LcaOnTree(n);
        deque = new ArrayDeque(n);
        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
    }

    public void init(int l, int r, IntegerComparator comp) {
        assert l <= r;
        int len = r - l + 1;
        stack.expandStackNum(len);
        stack.clear();
        deque.clear();
        offset = l;

        for (int i = 0; i < len; i++) {
            nodes[i].index = i;
            nodes[i].left = nodes[i].right = null;
        }
        Deque<Node> deque = new ArrayDeque<>(len);
        for (int i = 0; i < len; i++) {
            while (!deque.isEmpty() && comp.compare(deque.peekLast().index + offset,
                    nodes[i].index + offset) > 0) {
                Node tail = deque.removeLast();
                tail.right = nodes[i].left;
                nodes[i].left = tail;
            }
            deque.addLast(nodes[i]);
        }
        while (deque.size() > 1) {
            Node tail = deque.removeLast();
            deque.peekLast().right = tail;
        }
        Node root = deque.removeLast();
        for (int i = 0; i < len; i++) {
            if (nodes[i].left != null) {
                stack.addLast(i, nodes[i].left.index);
            }
            if (nodes[i].right != null) {
                stack.addLast(i, nodes[i].right.index);
            }
        }

        lca.reset(stack, root.index);
    }

    public int query(int l, int r) {
        l -= offset;
        r -= offset;
        return lca.lca(l, r) + offset;
    }

    private static class LcaOnTree {
        int[] parent;
        int[] preOrder;
        int[] i;
        int[] head;
        int[] a;
        int time;

        void dfs1(IntegerMultiWayStack tree, int u, int p) {
            parent[u] = p;
            i[u] = preOrder[u] = time++;
            for (IntegerIterator iterator = tree.iterator(u); iterator.hasNext(); ) {
                int v = iterator.next();
                if (v == p) continue;
                dfs1(tree, v, u);
                if (Integer.lowestOneBit(i[u]) < Integer.lowestOneBit(i[v])) {
                    i[u] = i[v];
                }
            }
            head[i[u]] = u;
        }

        void dfs2(IntegerMultiWayStack tree, int u, int p, int up) {
            a[u] = up | Integer.lowestOneBit(i[u]);
            for (IntegerIterator iterator = tree.iterator(u); iterator.hasNext(); ) {
                int v = iterator.next();
                if (v == p) continue;
                dfs2(tree, v, u, a[u]);
            }
        }

        public void reset(IntegerMultiWayStack tree, int root) {
            time = 0;
            dfs1(tree, root, -1);
            dfs2(tree, root, -1, 0);
        }

        public LcaOnTree(int n) {
            preOrder = new int[n];
            i = new int[n];
            head = new int[n];
            a = new int[n];
            parent = new int[n];
        }

        private int enterIntoStrip(int x, int hz) {
            if (Integer.lowestOneBit(i[x]) == hz)
                return x;
            int hw = 1 << Log2.floorLog(a[x] & (hz - 1));
            return parent[head[i[x] & -hw | hw]];
        }

        public int lca(int x, int y) {
            int hb = i[x] == i[y] ? Integer.lowestOneBit(i[x]) : (1 << Log2.floorLog(i[x] ^ i[y]));
            int hz = Integer.lowestOneBit(a[x] & a[y] & -hb);
            int ex = enterIntoStrip(x, hz);
            int ey = enterIntoStrip(y, hz);
            return preOrder[ex] < preOrder[ey] ? ex : ey;
        }
    }

    private static class Node {
        public int index;
        public Node left;
        public Node right;

        @Override
        public String toString() {
            return "" + index;
        }
    }
}
