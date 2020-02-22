package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class TaskB2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[1 + n];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }
        int m = in.readInt();
        Query[] qs = new Query[m];
        for (int i = 0; i < m; i++) {
            qs[i] = new Query();
            qs[i].k = in.readInt();
            qs[i].pos = in.readInt();
        }
        Query[] sortedQs = qs.clone();
        Arrays.sort(sortedQs, (x, y) -> x.k - y.k);
        Deque<Query> deque = new ArrayDeque<>(Arrays.asList(sortedQs));
        Integer[] addIndex = new Integer[n];
        for (int i = 0; i < n; i++) {
            addIndex[i] = i + 1;
        }
        Arrays.sort(addIndex, (x, y) -> a[x] == a[y] ? x - y : -(a[x] - a[y]));
        Segment seg = new Segment(1, n, a);
        for (int i = 1; i <= n; i++) {
            seg.updateClear(addIndex[i - 1], addIndex[i - 1], 1, n);
            while(!deque.isEmpty() && deque.peekFirst().k == i){
                Query q = deque.removeFirst();
                q.ans = seg.query(q.pos, 1, n);
            }
        }

        for(Query q : qs){
            out.println(q.ans);
        }
    }
}

class Query {
    int k;
    int pos;
    int ans;
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int val;
    private int size;

    public void pushUp() {
        size = left.size + right.size;
    }

    public void pushDown() {
    }

    public Segment(int l, int r, int[] val) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m, val);
            right = new Segment(m + 1, r, val);
            pushUp();
        } else {
            this.val = val[l];
            size = 0;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            size = 1;
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.updateClear(ll, rr, l, m);
        right.updateClear(ll, rr, m + 1, r);
        pushUp();
    }

    public int query(int k, int l, int r) {
        Segment trace = this;
        while (l < r) {
            int m = (l + r) >> 1;
            trace.pushDown();
            if (trace.left.size >= k) {
                r = m;
                trace = trace.left;
            } else {
                l = m + 1;
                k -= trace.left.size;
                trace = trace.right;
            }
        }
        return trace.val;
    }
}
