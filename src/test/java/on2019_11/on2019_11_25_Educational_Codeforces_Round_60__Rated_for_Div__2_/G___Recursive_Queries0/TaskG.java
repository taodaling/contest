package on2019_11.on2019_11_25_Educational_Codeforces_Round_60__Rated_for_Div__2_.G___Recursive_Queries0;




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
        segR = segL;
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
                    segR.queryR(head.l, head.l, 0, n)
                    + segL.queryL(head.r, head.r, 0, n);
        }
        long leftSum = segL.queryL(root.id - 1, root.id - 1, 0, n);
        segL.update(root.id, root.rangeR, 0, n, leftSum - root.rangeL + 1, 0, 1, 0);
        long rightSum = segR.queryR(root.id + 1, root.id + 1, 0, n);
        segR.update(root.rangeL, root.id, 0, n, 0, rightSum + root.rangeR + 1, 0, -1);
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
    long leftVal;
    long rightVal;
    int index;
    long leftIndex;
    long rightIndex;

    public void modify(long leftVal, long rightVal, long incLeft, long incRight) {
        this.leftVal += leftVal;
        this.rightVal += rightVal;
        leftIndex += incLeft;
        rightIndex += incRight;
    }


    public void pushUp() {
    }

    public void pushDown() {
        left.modify(leftVal, rightVal, leftIndex, rightIndex);
        right.modify(leftVal, rightVal, leftIndex, rightIndex);
        leftVal = rightVal = 0;
        rightIndex = leftIndex = 0;
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

    public void update(int ll, int rr, int l, int r, long leftVal, long rightVal, long incLeft, long incRight) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(leftVal, rightVal, incLeft, incRight);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, leftVal, rightVal, incLeft, incRight);
        right.update(ll, rr, m + 1, r, leftVal, rightVal, incLeft, incRight);
        pushUp();
    }

    public long queryL(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return leftVal + leftIndex * index;
        }
        pushDown();
        int m = (l + r) >> 1;
        return left.queryL(ll, rr, l, m) +
                right.queryL(ll, rr, m + 1, r);
    }

    public long queryR(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return rightVal + rightIndex * index;
        }
        pushDown();
        int m = (l + r) >> 1;
        return left.queryR(ll, rr, l, m) +
                right.queryR(ll, rr, m + 1, r);
    }
}
