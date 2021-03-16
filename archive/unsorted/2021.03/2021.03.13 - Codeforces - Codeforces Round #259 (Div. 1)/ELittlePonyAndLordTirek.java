package contest;

import template.algo.BlockSplit;
import template.datastructure.VanEmdeBoasTree;
import template.datastructure.VanEmdeBoasTreeBeta;
import template.datastructure.VanEmdeBoasTreeMap;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToLongFunction;
import template.utils.CloneSupportObject;
import template.utils.CompareUtils;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class ELittlePonyAndLordTirek {
    Debug debug = new Debug(true);
    Interval[] intervals;
    int lim = (int) 1e5;
    VanEmdeBoasTreeMap<Interval> map = new VanEmdeBoasTreeMap<>((int) 1e5 + 1);

    public void split(int l) {
        Interval floor = map.floorValue(l);
        if (floor != null && floor.r >= l) {
            Interval right = floor.clone();
            right.l = l;
            floor.r = l - 1;
            map.put(l, right);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Item[] items = new Item[n + 1];

        NoTagPersistentSegment[] st = new NoTagPersistentSegment[n + 1];
        st[0] = NoTagPersistentSegment.NIL;

        for (int i = 1; i <= n; i++) {
            items[i] = new Item();
            items[i].init = in.ri();
            items[i].cap = in.ri();
            items[i].speed = in.ri();

            st[i] = st[i - 1].clone();
            if (items[i].speed != 0) {
                int time = DigitUtils.ceilDiv(items[i].cap, items[i].speed);
                st[i].update(time, time, 0, lim, items[i].cap, items[i].speed);
            }
        }

        for (int i = 1; i <= n; i++) {
            Interval interval = new Interval();
            interval.dirty = true;
            interval.l = interval.r = i;
            interval.lastVisit = 0;
            map.put(interval.l, interval);
        }

        int m = in.ri();
        for (int i = 0; i < m; i++) {
            int t = in.ri();
            int l = in.ri();
            int r = in.ri();
            split(l);
            split(r + 1);

            Interval head = new Interval();
            head.l = l;
            head.r = r;
            head.lastVisit = t;
            head.dirty = false;

            long sum = 0;
            while (true) {
                Interval interval = map.ceilValue(l);
                if (interval == null || interval.l > r) {
                    break;
                }
                map.remove(interval.l);

                if (interval.dirty) {
                    Item item = items[interval.l];
                    sum += Math.min(item.init + item.speed * (long) t, item.cap);
                } else {
                    int delta = t - interval.lastVisit;
                    sum += (st[interval.r].queryCap(0, delta, 0, lim) - st[interval.l - 1].queryCap(0, delta, 0, lim)) +
                            (st[interval.r].querySpeed(delta + 1, lim, 0, lim) - st[interval.l - 1].querySpeed(delta + 1, lim, 0, lim)) * delta;
                }
            }

            map.put(head.l, head);
            out.println(sum);
        }
    }
}

class Interval extends CloneSupportObject<Interval> {
    int l;
    int r;
    boolean dirty;
    int lastVisit;

    @Override
    public String toString() {
        return String.format("(%d,%d)", l, r);
    }
}

class Item {
    int init;
    int cap;
    int speed;
}

class NoTagPersistentSegment implements Cloneable {
    public static final NoTagPersistentSegment NIL = new NoTagPersistentSegment();

    static {
        NIL.left = NIL.right = NIL;
    }

    private NoTagPersistentSegment left;
    private NoTagPersistentSegment right;
    private long sumOfCap;
    private long sumOfSpeed;

    public void pushUp() {
        sumOfCap = left.sumOfCap + right.sumOfCap;
        sumOfSpeed = left.sumOfSpeed + right.sumOfSpeed;
    }

    public NoTagPersistentSegment() {
        left = right = NIL;
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int cap, int speed) {
        if (covered(ll, rr, l, r)) {
            sumOfSpeed += speed;
            sumOfCap += cap;
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        if (!noIntersection(ll, rr, l, m)) {
            left = left.clone();
            left.update(ll, rr, l, m, cap, speed);
        }
        if (!noIntersection(ll, rr, m + 1, r)) {
            right = right.clone();
            right.update(ll, rr, m + 1, r, cap, speed);
        }
        pushUp();
    }

    public long queryCap(int ll, int rr, int l, int r) {
        if (this == NIL || noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return sumOfCap;
        }
        int m = DigitUtils.floorAverage(l, r);
        return left.queryCap(ll, rr, l, m) +
                right.queryCap(ll, rr, m + 1, r);
    }

    public long querySpeed(int ll, int rr, int l, int r) {
        if (this == NIL || noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return sumOfSpeed;
        }
        int m = DigitUtils.floorAverage(l, r);
        return left.querySpeed(ll, rr, l, m) +
                right.querySpeed(ll, rr, m + 1, r);
    }

    @Override
    public NoTagPersistentSegment clone() {
        try {
            return (NoTagPersistentSegment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
