package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.primitve.generated.datastructure.IntToIntFunction;
import template.utils.Debug;

public class CPuttingBoxesTogether {
    static Modular mod = new Modular(1e9 + 7);
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] a = new int[n + 1];
        int[] w = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }
        for (int i = 1; i <= n; i++) {
            w[i] = in.readInt();
        }

        Query query = new Query();
        State state = new State();
        Segment seg = new Segment(1, n, i -> w[i], i -> a[i]);
        for (int i = 0; i < q; i++) {
            int x = in.readInt();
            int y = in.readInt();
            if (x < 0) {
                seg.update(-x, -x, 1, n, y);
            } else {
                int l = x;
                int r = y;
                state.clear(0);
                seg.query(l, r, 1, n, state);
                long sumOfW = state.w;
                query.reset(0, sumOfW);
                seg.binarySearch(l, r, 1, n, query);
                int index = query.index;
                debug.debug("index", index);
                long ans = 0;
                state.clear(a[index]);
                seg.query(l, index, 1, n, state);
                ans += state.moveR;
                state.clear(a[index]);
                seg.query(index, r, 1, n, state);
                ans += state.moveL;
                out.println(mod.valueOf(ans));
            }
        }
    }
}

class Query {
    long now;
    long total;
    int index;

    public void reset(long now, long total) {
        this.now = now;
        this.total = total;
        index = -1;
    }

}

class State {
    static Modular mod = new Modular(1e9 + 7);

    int l;
    int r;
    long w;
    int moveL;
    int moveR;
    int size;

    static State tmp = new State();

    public void clear(int i) {
        l = r = i;
        w = 0;
        moveL = moveR = 0;
        size = 0;
    }

    public void init(int w) {
        this.w = w;
        moveL = moveR = 0;
    }

    public void copy(State s) {
        l = s.l;
        r = s.r;
        w = s.w;
        moveL = s.moveL;
        moveR = s.moveR;
        size = s.size;
    }

    public void mergeRight(State r) {
        tmp.copy(this);
        merge(tmp, r);
    }

    public void merge(State a, State b) {
        l = a.l;
        r = b.r;
        w = a.w + b.w;
        size = a.size + b.size;
        moveL = mod.plus(a.moveL, b.moveL);
        moveL = mod.plus(moveL, mod.mul(mod.valueOf(b.w), b.l - a.l - a.size));
        moveR = mod.plus(a.moveR, b.moveR);
        moveR = mod.plus(moveR, mod.mul(mod.valueOf(a.w), b.r - a.r - b.size));
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    static Modular mod = new Modular(1e9 + 7);

    State state = new State();

    private void modify(int w) {
        state.init(w);
    }

    public void pushUp() {
        state.merge(left.state, right.state);
    }

    public void pushDown() {
    }

    public Segment(int l, int r, IntToIntFunction func, IntToIntFunction index) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func, index);
            right = new Segment(m + 1, r, func, index);
            pushUp();
        } else {
            modify(func.apply(l));
            state.l = state.r = index.apply(l);
            state.size = 1;
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

    public void query(int ll, int rr, int l, int r, State ans) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            ans.mergeRight(state);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(ll, rr, l, m, ans);
        right.query(ll, rr, m + 1, r, ans);
    }

    public void binarySearch(int ll, int rr, int l, int r, Query q) {
        if (noIntersection(ll, rr, l, r) || q.now * 2 >= q.total) {
            return;
        }
        if (covered(ll, rr, l, r) && (q.now + state.w) * 2 < q.total) {
            q.now += state.w;
            return;
        }
        if (l == r) {
            q.now += state.w;
            q.index = l;
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.binarySearch(ll, rr, l, m, q);
        right.binarySearch(ll, rr, m + 1, r, q);
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
            builder.append(state.w).append(",");
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
