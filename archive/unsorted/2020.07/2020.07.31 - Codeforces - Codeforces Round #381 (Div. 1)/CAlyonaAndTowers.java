package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.LongBIT;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class CAlyonaAndTowers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int m = in.readInt();
        Segment seg = new Segment(1, n, i -> a[i - 1]);
        for (int i = 0; i < m; i++) {
            int l = in.readInt();
            int r = in.readInt();
            int d = in.readInt();

            seg.updatePlus(l, r, 1, n, d);

            //seg.toString();
            int ans = seg.state.hill;
            out.println(ans);
        }
    }
}

class State implements Cloneable {
    long h;
    long t;
    int hill;
    int size;
    int[] head = new int[3];
    int[] tail = new int[3];

    public void modify(long x) {
        h += x;
        t += x;
    }

    public void init(long x) {
        h = t = x;
        hill = 1;
        size = 1;
        Arrays.fill(head, 1);
        Arrays.fill(tail, 1);
    }

    public static void mergeInto(State a, State b, State ans) {
        ans.h = a.h;
        ans.t = b.t;
        ans.size = a.size + b.size;
        ans.hill = Math.max(a.hill, b.hill);
        if (a.t < b.h) {
            ans.hill = Math.max(ans.hill, a.tail[0] + b.head[2]);
        } else if (a.t > b.h) {
            ans.hill = Math.max(ans.hill, a.tail[2] + b.head[1]);
        }

        for (int i = 0; i < 3; i++) {
            ans.head[i] = a.head[i];
            ans.tail[i] = b.tail[i];
        }

        //concate
        if (a.t < b.h) {
            if (a.head[0] == a.size) {
                ans.head[0] = Math.max(ans.head[0], a.head[0] + b.head[0]);
                ans.head[2] = Math.max(ans.head[2], a.head[0] + b.head[2]);
            }
            if (b.tail[0] == b.size) {
                ans.tail[0] = Math.max(ans.tail[0], a.tail[0] + b.tail[0]);
            }
            if (b.tail[2] == b.size) {
                ans.tail[2] = Math.max(ans.tail[2], a.tail[0] + b.tail[2]);
            }
        } else if (a.t > b.h) {
            if (a.head[1] == a.size) {
                ans.head[1] = Math.max(ans.head[1], a.head[1] + b.head[1]);
            }
            if (a.head[2] == a.size) {
                ans.head[2] = Math.max(ans.head[2], a.head[2] + b.head[1]);
            }
            if (b.tail[1] == b.size) {
                ans.tail[1] = Math.max(ans.tail[1], a.tail[1] + b.tail[1]);
                ans.tail[2] = Math.max(ans.tail[2], a.tail[2] + b.tail[1]);
            }
        }

        for (int i = 0; i < 2; i++) {
            ans.tail[2] = Math.max(ans.tail[2], ans.tail[i]);
            ans.head[2] = Math.max(ans.head[2], ans.head[i]);
        }
    }

    @Override
    public String toString() {
        return "" + h;
    }

    @Override
    public State clone() {
        State state = new State();
        state.h = h;
        state.t = t;
        state.size = size;
        state.head = head.clone();
        state.tail = tail.clone();
        return state;
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    State state = new State();
    long plus;

    private void modify(long x) {
        plus += x;
        state.modify(x);
    }

    public void pushUp() {
        State.mergeInto(left.state, right.state, state);
    }

    public void pushDown() {
        if (plus != 0) {
            left.modify(plus);
            right.modify(plus);
            plus = 0;
        }
    }

    public Segment(int l, int r, IntToIntegerFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
        } else {
            state.init(func.apply(l));
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.updatePlus(ll, rr, l, m, x);
        right.updatePlus(ll, rr, m + 1, r, x);
        pushUp();
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
            Segment ans = (Segment) super.clone();
            ans.state = state.clone();
            return ans;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(state.h).append(",");
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
