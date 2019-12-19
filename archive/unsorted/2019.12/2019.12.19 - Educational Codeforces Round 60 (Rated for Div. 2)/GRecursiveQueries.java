package contest;


import template.datastructure.BIT;
import template.datastructure.IntRMQ;
import template.datastructure.LongBIT;
import template.datastructure.RMQ;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.IntComparator;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

public class GRecursiveQueries {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt();
        }

        DescartesTree tree = new DescartesTree(p, 0, n - 1, (a, b) -> -Integer.compare(a, b));
        IntRMQ rmq = new IntRMQ(p, (a, b) -> -(a - b));
        Query[] queries = new Query[q];
        for (int i = 0; i < q; i++) {
            queries[i] = new Query();
        }

        for (int i = 0; i < q; i++) {
            queries[i].l = in.readInt() - 1;
        }

        for (int i = 0; i < q; i++) {
            queries[i].r = in.readInt() - 1;
        }

        for (int i = 0; i < q; i++) {
            int maxIndex = rmq.query(queries[i].l, queries[i].r);
            queries[i].next = tree.nodes[maxIndex].next;
            tree.nodes[maxIndex].next = queries[i];
        }

        lCntBit = new BIT(n + 1);
        rCntBit = new BIT(n + 1);
        lValueBit = new LongBIT(n + 1);
        rValueBit = new LongBIT(n + 1);

        dfs(tree.root, 0, n - 1);

        for (Query query : queries) {
            out.println(query.ans);
        }
    }

    int n;
    BIT lCntBit;
    BIT rCntBit;
    LongBIT lValueBit;
    LongBIT rValueBit;

    public void update(BIT bit, int l, int r, int x) {
        bit.update(l, x);
        bit.update(r + 1, -x);
    }

    public void update(LongBIT bit, int l, int r, long x) {
        bit.update(l, x);
        bit.update(r + 1, -x);
    }

    public void dfs(DescartesTree.Node root, int l, int r) {
        if (root == null) {
            return;
        }
        dfs(root.left, l, root.index - 1);
        dfs(root.right, root.index + 1, r);
        for (Query q = root.next; q != null; q = q.next) {
            q.ans = q.r - q.l + 1;
            q.ans += (long) lCntBit.query(q.r + 1) * q.r + lValueBit.query(q.r + 1);
            q.ans += (long) rCntBit.query(q.l + 1) * q.l + rValueBit.query(q.l + 1);
        }

        long lPart = 0;
        if (l < root.index) {
            lPart = (long) lCntBit.query(root.index) * (root.index - 1) +
                    lValueBit.query(root.index);
        }
        update(lCntBit, root.index + 1, r + 1, 1);
        update(lValueBit, root.index + 1, r + 1, lPart - (l - 1));

        long rPart = 0;
        if (r > root.index) {
            rPart = (long) rCntBit.query(root.index + 2) * (root.index + 1) +
                    rValueBit.query(root.index + 2);
        }
        update(rCntBit, l + 1, root.index + 1, -1);
        update(rValueBit, l + 1, root.index + 1, rPart + (r + 1));
    }
}

class Query {
    int l;
    int r;
    long ans;
    Query next;
}


/**
 * 笛卡尔树，中序遍历为下标递增，根元素为最小元素（如果有多个，下标较小的作为根）
 */
class DescartesTree {
    Node root;
    Node[] nodes;

    public <T> DescartesTree(T[] data, int l, int r, Comparator<T> comp) {
        int len = r - l + 1;
        nodes = new Node[len];
        for (int i = 0; i < len; i++) {
            nodes[i] = new Node();
            nodes[i].index = i + l;
        }
        Deque<Node> deque = new ArrayDeque<>(len);
        for (int i = 0; i < len; i++) {
            while (!deque.isEmpty() && comp.compare(data[deque.peekLast().index], data[nodes[i].index]) > 0) {
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
        root = deque.removeLast();
    }

    public DescartesTree(int[] data, int l, int r, IntComparator comparator) {
        int len = r - l + 1;
        nodes = new Node[len];
        for (int i = 0; i < len; i++) {
            nodes[i] = new Node();
            nodes[i].index = i + l;
        }
        Deque<Node> deque = new ArrayDeque<>(len);
        for (int i = 0; i < len; i++) {
            while (!deque.isEmpty() && comparator.compare(data[deque.peekLast().index], data[nodes[i].index]) > 0) {
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
        root = deque.removeLast();
    }

    public Node getRoot() {
        return root;
    }

    public static class Node {
        public int index;
        public Node left;
        public Node right;
        public Query next;

        @Override
        public String toString() {
            return "" + index;
        }
    }
}