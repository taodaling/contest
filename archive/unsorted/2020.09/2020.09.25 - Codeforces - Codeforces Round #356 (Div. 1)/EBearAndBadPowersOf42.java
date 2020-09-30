package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class EBearAndBadPowersOf42 {
    int n;
    TreeMap<Integer, Integer>[] maps;
    Segment[] segs;

    Query query = new Query();

    public void breakOut(int l, int r) {
        for (int i = 0; i < 10; i++) {
            Map.Entry<Integer, Integer> floor = maps[i].floorEntry(l - 1);
            if (floor != null && floor.getValue() >= l) {
                query.reset();
                segs[i].query(floor.getKey(), floor.getKey(), 0, n - 1, query);
                set(l, floor.getValue(), query.max, maps[i], segs[i]);
                maps[i].put(floor.getKey(), l - 1);
            }
        }
        for (int i = 0; i < 10; i++) {
            Map.Entry<Integer, Integer> floor = maps[i].floorEntry(r);
            if (floor != null && floor.getValue() > r) {
                query.reset();
                segs[i].query(floor.getKey(), floor.getKey(), 0, n - 1, query);
                set(r + 1, floor.getValue(), query.max, maps[i], segs[i]);
                maps[i].put(floor.getKey(), r);
            }
        }
    }

    Debug debug = new Debug(true);

    public long query(int x) {
        for (int j = 0; j < 10; j++) {
            Map.Entry<Integer, Integer> floor = maps[j].floorEntry(x);
            if (floor != null && floor.getValue() >= x) {
                query.reset();
                segs[j].query(floor.getKey(), floor.getKey(), 0, n - 1, query);
                return query.max;
            }
        }
        throw new IllegalStateException();
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int q = in.readInt();
        int base = 42;

        long[] exp = new long[10];
        exp[0] = 1;
        for (int i = 1; i < 10; i++) {
            exp[i] = exp[i - 1] * base;
        }

        maps = new TreeMap[10];
        for (int i = 0; i < 10; i++) {
            maps[i] = new TreeMap<>();
        }

        segs = new Segment[10];
        for (int i = 0; i < 10; i++) {
            segs[i] = new Segment(0, n - 1);
        }

        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            for (int j = 0; j < 10; j++) {
                if (x < exp[j] * base) {
                    set(i, i, x, maps[j], segs[j]);
                    break;
                }
            }
        }

        Query query = new Query();
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 1) {
                int x = in.readInt() - 1;
                out.println(query(x));
            } else if (t == 2) {
                int l = in.readInt() - 1;
                int r = in.readInt() - 1;
                int x = in.readInt();

                breakOut(l, r);
                //remove
                for (int j = 0; j < 10; j++) {
                    while (true) {
                        Map.Entry<Integer, Integer> floor = maps[j].floorEntry(r);
                        if (floor != null && floor.getKey() >= l) {
                            maps[j].remove(floor.getKey());
                        } else {
                            break;
                        }
                    }
                    segs[j].updateSet(l, r, 0, n - 1, -inf);
                }

                for (int j = 0; j < 10; j++) {
                    if (x < exp[j] * base) {
                        set(l, r, x, maps[j], segs[j]);
                        break;
                    }
                }
            } else {
                int l = in.readInt() - 1;
                int r = in.readInt() - 1;
                breakOut(l, r);

                int x = in.readInt();
                boolean goon = true;

                while (goon) {
                    goon = false;
                    for (int j = 10 - 1; j >= 0; j--) {
                        segs[j].update(l, r, 0, n - 1, x);

                        while (true) {
                            query.reset();
                            segs[j].query(0, n - 1, 0, n - 1, query);
                            if (query.max >= exp[j] * base) {
                                int right = remove(query.x, maps[j], segs[j]);
                                for (int k = j + 1; k < 10; k++) {
                                    if (exp[k] * base > query.max) {
                                        goon = goon || exp[k] == query.max;
                                        set(query.x, right, query.max, maps[k], segs[k]);
                                        break;
                                    }
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            }


            debug.run(() -> {
                long[] data = new long[n];
                for (int j = 0; j < n; j++) {
                    data[j] = query(j);
                }
                debug.debug("data", Arrays.toString(data));
            });
        }
    }


    static long inf = (long) 1e18;

    public int remove(int i, TreeMap<Integer, Integer> map, Segment seg) {
        Integer right = map.remove(i);
        seg.updateSet(i, i, 0, n - 1, -inf);
        return right;
    }

    public void set(int l, int r, long x, TreeMap<Integer, Integer> map, Segment seg) {
        map.put(l, r);
        seg.updateSet(l, l, 0, n - 1, x);
    }


}

class Query {
    int x;
    long max;
    static long inf = (long) 1e18;

    public void reset() {
        x = -1;
        max = -inf;
    }

    public void update(int x, long max) {
        if (this.max < max) {
            this.max = max;
            this.x = x;
        }
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private long max;
    private long modify;
    private boolean set;
    private int maxIndex;
    private static long inf = (long) 1e18;

    private void set(long x) {
        set = true;
        modify = x;
        max = x;
    }

    private void modify(long x) {
        modify += x;
        max += x;
    }

    public void pushUp() {
        max = Math.max(left.max, right.max);
        maxIndex = left.max == max ? left.maxIndex : right.maxIndex;
    }

    public void pushDown() {
        if (set) {
            left.set(modify);
            right.set(modify);
            set = false;
            modify = 0;
        }
        if (modify != 0) {
            left.modify(modify);
            right.modify(modify);
            modify = 0;
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
            max = -inf;
            maxIndex = l;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }


    public void updateSet(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            set(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.updateSet(ll, rr, l, m, x);
        right.updateSet(ll, rr, m + 1, r, x);
        pushUp();
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

    public void query(int ll, int rr, int l, int r, Query q) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            q.update(maxIndex, max);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(ll, rr, l, m, q);
        right.query(ll, rr, m + 1, r, q);
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
            builder.append(max).append(",");
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
