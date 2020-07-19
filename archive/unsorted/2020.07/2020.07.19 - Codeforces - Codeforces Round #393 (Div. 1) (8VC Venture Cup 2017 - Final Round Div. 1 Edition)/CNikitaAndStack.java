package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class CNikitaAndStack {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.readInt();
        Segment seg = new Segment(1, m);
        int[] push = new int[m + 1];

        Query q = new Query();
        for (int i = 0; i < m; i++) {
            int p = in.readInt();
            int t = in.readInt();
            if (t == 1) {
                int x = in.readInt();
                push[p] = x;
                seg.update(p, p, 1, m, 1);
            } else {
                seg.update(p, p, 1, m, -1);
            }
            q.reset();
            seg.query(1, m, 1, m, q);
            if(q.index == -1){
                out.println(-1);
            }else{
                out.println(push[q.index]);
            }
        }
    }
}

class State {
    int maxSuf;
    int size;
    int sum;

    public void reset() {
        maxSuf = 0;
        size = 0;
        sum = 0;
    }

    public void modify(int x) {
        size = 1;
        sum = x;
        maxSuf = x;
    }

    static State buf = new State();

    public static void mergeIntoRight(State a, State b) {
        buf.size = b.size;
        buf.maxSuf = b.maxSuf;
        buf.sum = b.sum;
        mergeInto(a, buf, b);
    }

    public static void mergeInto(State a, State b, State output) {
        output.size = a.size + b.size;
        output.sum = a.sum + b.sum;
        output.maxSuf = Math.max(b.maxSuf, b.sum + a.maxSuf);
    }
}

class Query {
    int index;
    State state = new State();

    public void reset() {
        index = -1;
        state.reset();
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    State state = new State();

    private void modify(int x) {
        state.modify(x);
    }

    public void pushUp() {
        State.mergeInto(left.state, right.state, state);
    }

    public void pushDown() {

    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {

        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r, Query query) {
        if (state.size == 0 || query.index != -1) {
            return;
        }
        if (state.maxSuf + query.state.sum <= 0) {
            State.mergeIntoRight(state, query.state);
            return;
        }
        if (l == r) {
            State.mergeIntoRight(state, query.state);
            query.index = l;
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        right.query(ll, rr, m + 1, r, query);
        left.query(ll, rr, l, m, query);
    }

    private Segment deepClone() {
        Segment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected Segment clone() {
        try {
            return (Segment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append("val").append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
