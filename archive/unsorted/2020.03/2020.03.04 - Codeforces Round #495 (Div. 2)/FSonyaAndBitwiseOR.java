package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntToIntFunction;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerRange2DequeAdapter;
import template.utils.Debug;

public class FSonyaAndBitwiseOR {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int x = in.readInt();

        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }

        State.x = x;
        State.function = i -> a[i];

        Segment segment = new Segment(1, n);

        debug.debug("segment", segment);
        State query = new State();
        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            if (t == 1) {
                int index = in.readInt();
                int y = in.readInt();
                a[index] = y;
                segment.add(index, index, 1, n);
            } else {
                int l = in.readInt();
                int r = in.readInt();
                query.init(l);
                segment.query(l, r, 1, n, query);
                long ans = query.way;
                out.println(ans);
            }
        }
    }
}

class State {
    public static IntToIntFunction function;
    public static int x;

    public int l;
    public int r;
    public long way;

    public IntegerList pre = new IntegerList(20);
    public IntegerList post = new IntegerList(20);
    private static State tmp = new State();
    private static int[] buf = new int[20];

    public void init(int l) {
        this.l = l;
        this.r = l - 1;
        pre.clear();
        post.clear();
        way = 0;
    }

    private static void copy(State a, State b) {
        b.l = a.l;
        b.r = a.r;
        b.way = a.way;

        IntegerList swap = a.pre;
        a.pre = b.pre;
        b.pre = swap;

        swap = a.post;
        a.post = b.post;
        b.post = swap;
    }

    public void mergeInto(State b) {
        merge(this, b, tmp);
        copy(tmp, this);
        return;
    }

    public void singlePoint() {
        int val = function.apply(l);
        if (val >= x) {
            way = 1;
        } else {
            way = 0;
        }
        pre.clear();
        post.clear();
        pre.add(l);
        post.add(l);
    }

    public static void merge(State a, State b, State result) {
        result.pre.clear();
        result.post.clear();
        result.l = a.l;
        result.r = b.r;
        result.way = a.way + b.way;

        b.pre.toArray(buf);
        int n = a.post.size();
        int m = b.pre.size();
        for (int i = 0; i < m; i++) {
            buf[i] = function.apply(buf[i]);
            if (i > 0) {
                buf[i] |= buf[i - 1];
            }
        }
        int leftMask = 0;
        IntegerRange2DequeAdapter dq = new IntegerRange2DequeAdapter(i -> a.post.get(i), 0, a.post.size() - 1);
        int lastEnd = a.r;
        int rightEnd = b.r;
        for (int i = m - 1; i >= 0; i--) {
            int index = b.pre.get(i);
            while (!dq.isEmpty() && (leftMask | buf[i]) < x) {
                lastEnd = dq.peekFirst();
                leftMask |= function.apply(dq.removeFirst());
            }
            if ((leftMask | buf[i]) < x) {
                break;
            }
            result.way += (long) (rightEnd - index + 1) * (lastEnd - a.l + 1);
            rightEnd = index - 1;
        }

        result.pre.addAll(a.pre);
        int mask = combine(a.pre);
        for (int i = 0; mask < x && i < m; i++) {
            int index = b.pre.get(i);
            int val = function.apply(index);
            if ((mask | val) != mask) {
                mask |= val;
                result.pre.add(index);
            }
        }
        mask = combine(b.post);
        result.post.addAll(b.post);
        for (int i = 0; mask < x && i < n; i++) {
            int index = a.post.get(i);
            int val = function.apply(index);
            if ((mask | val) != mask) {
                mask |= val;
                result.post.add(index);
            }
        }
    }

    public static int combine(IntegerList list) {
        int ans = 0;
        for (int i = list.size() - 1; i >= 0; i--) {
            ans |= function.apply(list.get(i));
        }
        return ans;
    }

    @Override
    public String toString() {
        return String.format("[%d,%d]=%d", l, r, way);
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private State state;

    public void pushUp() {
        State.merge(left.state, right.state, state);
    }

    public void pushDown() {
    }

    public void modify() {
        state.singlePoint();
    }

    public Segment(int l, int r) {
        state = new State();
        state.l = l;
        state.r = r;
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
            modify();
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
            modify();
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.add(ll, rr, l, m);
        right.add(ll, rr, m + 1, r);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r, State state) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            state.mergeInto(this.state);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.query(ll, rr, l, m, state);
        right.query(ll, rr, m + 1, r, state);
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
            builder.append(state).append(",");
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
