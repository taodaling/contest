package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntUnaryOperator;

public class DYouAreGivenATree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

    }
}

class Node {
    List<Node> next = new ArrayList<>();
    Node heavy;
    int depth;
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int a; //a and max
    private int b; //a + d(a)
    private int c; //a - d(a)
    private int d;

    public void pushUp() {
        a = Math.max(left.a, right.a);
        a = Math.max(left.c + right.b, a);
        b = Math.max(left.b, right.b);
        c = Math.max(left.c, right.c);
    }

    public void pushDown() {
    }

    public int setVal(int val) {
        int ans = a;
        a = Math.max(a, val);
        b = a + d;
        c = a - d;
        return ans;
    }

    public Segment(int l, int r, IntUnaryOperator depthFunc) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m, depthFunc);
            right = new Segment(m + 1, r, depthFunc);
            pushUp();
        } else {
            d = depthFunc.applyAsInt(l);
            a = 0;
            b = a + d;
            c = a - d;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public int update(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return -1;
        }
        if (covered(ll, rr, l, r)) {
            return setVal(x);
        }
        pushDown();
        int m = (l + r) >> 1;
        int ans = Math.max(left.update(ll, rr, l, m, x),
                right.update(ll, rr, m + 1, r, x));
        pushUp();
        return ans;
    }

    public int query(int ll, int rr, int l, int r, int k) {
        if (noIntersection(ll, rr, l, r) || a < k) {
            return -1;
        }
        if (covered(ll, rr, l, r)) {
            return l;
        }
        pushDown();
        int m = (l + r) >> 1;
        int index = right.query(ll, rr, m + 1, r, k);
        if (index == -1) {
            index = left.query(ll, rr, l, m, k);
        }
        return index;
    }
}
