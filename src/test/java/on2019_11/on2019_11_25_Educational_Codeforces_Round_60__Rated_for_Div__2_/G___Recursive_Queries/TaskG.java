package on2019_11.on2019_11_25_Educational_Codeforces_Round_60__Rated_for_Div__2_.G___Recursive_Queries;



import template.datastructure.LeftistTree;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class TaskG {
    int n;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int q = in.readInt();
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt();
        }
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].val = p[i];
            nodes[i].id = i;
        }
        Deque<Node> deque = new ArrayDeque<>(n);
        for (int i = 0; i < n; i++) {
            Node node = nodes[i];
            while (!deque.isEmpty() && deque.peekLast().val < node.val) {
                Node last = deque.removeLast();
                last.r = node.l;
                node.l = last;
            }
            deque.addLast(node);
        }
        while (deque.size() > 1) {
            Node last = deque.removeLast();
            deque.peekLast().r = last;
        }
        Node root = deque.removeFirst();
        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            qs[i] = new Query();
        }
        for (int i = 0; i < q; i++) {
            qs[i].l = in.readInt() - 1;
        }
        for (int i = 0; i < q; i++) {
            qs[i].r = in.readInt() - 1;
        }
        for (int i = 0; i < q; i++) {
            nodes[qs[i].l].pq = LeftistTree.merge(nodes[qs[i].l].pq,
                    new LeftistTree<>(qs[i]), Query.sortByR);
        }
        segL = new Segment(0, n);
        segR = new Segment(0, n);
        dfs(root);

        for (Query query : qs) {
            out.println(query.ans);
        }
    }

    Segment segL;
    Segment segR;

    public void dfs(Node root) {
        root.rangeR = root.rangeL = root.id;
        if (root.l != null) {
            dfs(root.l);
            root.rangeL = root.l.rangeL;
            root.pq = LeftistTree.merge(root.pq, root.l.pq, Query.sortByR);
        }
        if (root.r != null) {
            dfs(root.r);
            root.rangeR = root.r.rangeR;
            root.pq = LeftistTree.merge(root.pq, root.r.pq, Query.sortByR);
        }
        while (!root.pq.isEmpty() && root.pq.peek().r <= root.rangeR) {
            Query head = root.pq.peek();
            root.pq = LeftistTree.pop(root.pq, Query.sortByR);
            head.ans = (head.r - head.l + 1) +
                    segR.query(head.l, head.l, 0, n)
                    + segL.query(head.r, head.r, 0, n);
        }
        long leftSum = segL.query(root.id - 1, root.id - 1, 0, n);
        segL.update(root.id, root.rangeR, 0, n, leftSum - root.rangeL + 1, 1);
        long rightSum = segR.query(root.id + 1, root.id + 1, 0, n);
        segR.update(root.rangeL, root.id, 0, n, rightSum + root.rangeR + 1, -1);
    }
}

class Query {
    int l;
    int r;
    long ans;
    static Comparator<Query> sortByR = (a, b) -> a.r - b.r;
}

class Node {
    Node l;
    Node r;
    int val;
    int rangeL;
    int rangeR;
    int id;
    LeftistTree<Query> pq = LeftistTree.NIL;
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    long val;
    int index;
    long indexTime;

    public void modify(long x, long incTime) {
        val += x;
        indexTime += incTime;
    }


    public void pushUp() {
    }

    public void pushDown() {
        left.modify(val, indexTime);
        right.modify(val, indexTime);
        val = 0;
        indexTime = 0;
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
            index = l;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long x, long incTime) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x, incTime);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, x, incTime);
        right.update(ll, rr, m + 1, r, x, incTime);
        pushUp();
    }

    public long query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return val + indexTime * index;
        }
        pushDown();
        int m = (l + r) >> 1;
        return left.query(ll, rr, l, m) +
                right.query(ll, rr, m + 1, r);
    }
}
