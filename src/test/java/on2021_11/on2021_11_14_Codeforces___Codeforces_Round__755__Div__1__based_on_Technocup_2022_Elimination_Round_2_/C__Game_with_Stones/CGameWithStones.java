package on2021_11.on2021_11_14_Codeforces___Codeforces_Round__755__Div__1__based_on_Technocup_2022_Elimination_Round_2_.C__Game_with_Stones;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToLongFunction;
import template.primitve.generated.datastructure.LongHashMap;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CGameWithStones {
    LongHashMap map = new LongHashMap((int) 1e6, false);
    Query q = new Query();

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        long[] ps = new long[n];
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                ps[i] = -ps[i - 1];
            }
            ps[i] += a[i];
        }
        long inf = (long) 1e18;
        long[] p0 = ps.clone();
        long[] p1 = ps.clone();
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                p1[i] = inf;
            } else {
                p0[i] = inf;
            }
        }
        Segment st = new Segment(0, n - 1, i -> ps[i]);
        List<Event> E0 = new ArrayList<>(n);
        List<Event> E1 = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            debug.debug("i", i);
            long prev = i > 0 ? ps[i - 1] : 0;
            long d0, d1;
            if (i % 2 == 0) {
                d0 = -prev;
                d1 = prev;
            } else {
                d1 = -prev;
                d0 = prev;
            }
            q.init(d0, d1);
            st.search(i, n - 1, 0, n - 1, q);
            if (q.rightest < i) {
                continue;
            }
            int l = i;
            int r = q.rightest;
            debug.debug("l", l);
            debug.debug("r", r);
            E0.add(new Event(l - 1, -1, d0));
            E0.add(new Event(r, 1, d0));
            E1.add(new Event(l - 1, -1, d1));
            E1.add(new Event(r, 1, d1));
        }
        map.clear();

        long ans = 0;
        E0.sort(Comparator.comparingInt(x -> x.l));
        E1.sort(Comparator.comparingInt(x -> x.l));
        for (int i = 0, iter = 0; i < n; i++) {
            if (i % 2 == 0) {
                map.modify(ps[i], 1);
            }
            while(iter < E0.size() && E0.get(iter).l <= i){
                Event e = E0.get(iter++);
                if(e.l == i){
                    ans += map.getOrDefault(e.v, 0) * e.sigh;
                }
            }
        }
        map.clear();
        for (int i = 0, iter = 0; i < n; i++) {
            if (i % 2 == 1) {
                map.modify(ps[i], 1);
            }
            while(iter < E1.size() && E1.get(iter).l <= i){
                Event e = E1.get(iter++);
                if(e.l == i){
                    ans += map.getOrDefault(e.v, 0) * e.sigh;
                }
            }
        }
        out.println(ans);
    }

    Debug debug = new Debug(false);
}

class Event {
    int l;
    int sigh;
    long v;

    public Event(int l, int sign, long v) {
        this.l = l;
        this.sigh = sign;
        this.v = v;
    }
}

class Query {
    void init(long d0, long d1) {
        min0 = min1 = inf;
        rightest = -1;
        this.d0 = d0;
        this.d1 = d1;
    }

    static long inf = (long) 1e18;
    long min0 = inf;
    long min1 = inf;
    long d0;
    long d1;
    int rightest = -1;


    public boolean update(long min0, long min1, int rightest) {
        if (min0 < d0 || min1 < d1) {
            return false;
        }
        this.min0 = Math.min(this.min0, min0);
        this.min1 = Math.min(this.min1, min1);
        this.rightest = Math.max(this.rightest, rightest);
        return true;
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    static long inf = (long) 1e18;
    long min0;
    long min1;

    private void modify() {
    }

    public void pushUp() {
        min0 = Math.min(left.min0, right.min0);
        min1 = Math.min(left.min1, right.min1);
    }

    public void pushDown() {
    }

    public Segment(int l, int r, IntToLongFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
        } else {
            min0 = min1 = inf;
            if (l % 2 == 0) {
                min0 = func.apply(l);
            } else {
                min1 = func.apply(l);
            }
        }
    }

    private boolean enter(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    private boolean leave(int L, int R, int l, int r) {
        return L > r || R < l;
    }

    public void update(int L, int R, int l, int r) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (enter(L, R, l, r)) {
            modify();
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(L, R, l, m);
        right.update(L, R, m + 1, r);
        pushUp();
    }

    public boolean search(int L, int R, int l, int r, Query q) {
        if (leave(L, R, l, r)) {
            return false;
        }
        if (enter(L, R, l, r) && q.update(min0, min1, r)) {
            return false;
        }
        if (l == r) {
            return true;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        if (!left.search(L, R, l, m, q)) {
            return right.search(L, R, m + 1, r, q);
        }
        return true;
    }

    public void query(int L, int R, int l, int r) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (enter(L, R, l, r)) {
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(L, R, l, m);
        right.query(L, R, m + 1, r);
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