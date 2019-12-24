package on2019_12.on2019_12_24_Codeforces_Round__539__Div__1_.C__Sasha_and_a_Patient_Friend;




import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.IntegerDiscreteMap;
import template.primitve.generated.IntegerList;

import java.util.Map;
import java.util.TreeMap;

public class CSashaAndAPatientFriend {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.readInt();
        TreeMap<Integer, Integer> events = new TreeMap<>();

        int[][] qs = new int[q][];
        IntegerList list = new IntegerList(2 * q);
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 1) {
                qs[i] = new int[]{t, in.readInt(), in.readInt()};
                list.add(qs[i][1]);
            } else if (t == 2) {
                qs[i] = new int[]{t, in.readInt()};
            } else {
                qs[i] = new int[]{t, in.readInt(), in.readInt(), in.readInt()};
                list.add(qs[i][1]);
                list.add(qs[i][2]);
            }
        }

        IntegerDiscreteMap dm = new IntegerDiscreteMap(list.getData(), 0, list.size());

        Interval interval = new Interval();
        Segment segment = new Segment(dm.minRank(), dm.maxRank());
        for (int i = 0; i < q; i++) {
            if (qs[i][0] == 1) {
                int t = dm.rankOf(qs[i][1]);
                int s = qs[i][2];
                Map.Entry<Integer, Integer> floor = events.floorEntry(t);
                Map.Entry<Integer, Integer> ceil = events.ceilingEntry(t);
                if (floor != null && ceil != null) {
                    long contrib = (long) (dm.iThElement(ceil.getKey()) - dm.iThElement(floor.getKey())) * floor.getValue();
                    segment.update(ceil.getKey(), ceil.getKey(), dm.minRank(), dm.maxRank(), -contrib);
                }
                if (floor != null) {
                    long contrib = (long) (dm.iThElement(t) - dm.iThElement(floor.getKey())) * floor.getValue();
                    segment.update(t, t, dm.minRank(), dm.maxRank(), contrib);
                }
                if (ceil != null) {
                    long contrib = (long) (dm.iThElement(ceil.getKey()) - dm.iThElement(t)) * s;
                    segment.update(ceil.getKey(), ceil.getKey(), dm.minRank(), dm.maxRank(), contrib);
                }
                events.put(t, s);
            } else if (qs[i][0] == 2) {
                int t = dm.rankOf(qs[i][1]);
                int s = events.remove(t);
                Map.Entry<Integer, Integer> floor = events.floorEntry(t);
                Map.Entry<Integer, Integer> ceil = events.ceilingEntry(t);
                if (floor != null) {
                    long contrib = (long) (dm.iThElement(t) - dm.iThElement(floor.getKey())) * floor.getValue();
                    segment.update(t, t, dm.minRank(), dm.maxRank(), -contrib);
                }
                if (ceil != null) {
                    long contrib = (long) (dm.iThElement(ceil.getKey()) - dm.iThElement(t)) * s;
                    segment.update(ceil.getKey(), ceil.getKey(), dm.minRank(), dm.maxRank(), -contrib);
                }
                if (floor != null && ceil != null) {
                    long contrib = (long) (dm.iThElement(ceil.getKey()) - dm.iThElement(floor.getKey())) * floor.getValue();
                    segment.update(ceil.getKey(), ceil.getKey(), dm.minRank(), dm.maxRank(), contrib);
                }
            } else {
                int l = dm.rankOf(qs[i][1]);
                int r = dm.rankOf(qs[i][2]);
                int v = qs[i][3];
                if (v == 0) {
                    out.println(dm.iThElement(l));
                    continue;
                }
                Integer ceilingKey = events.ceilingKey(l);
                if (ceilingKey == null || ceilingKey > r) {
                    out.println(-1);
                    continue;
                }
                l = ceilingKey + 1;
                int finalL = l;

                if(l > r){
                    out.println(-1);
                    continue;
                }

                IntBinarySearch ibs = new IntBinarySearch() {
                    @Override
                    public boolean check(int mid) {
                        interval.init();
                        segment.query(finalL, mid, dm.minRank(), dm.maxRank(), interval);
                        return interval.min + v <= 0;
                    }
                };
                int time = ibs.binarySearch(l, r);

                if (!ibs.check(time)) {
                    Map.Entry<Integer, Integer> entry = events.floorEntry(r);
                    if (entry == null || entry.getValue() >= 0) {
                        out.println(-1);
                        continue;
                    }
                    interval.init();
                    segment.query(finalL, entry.getKey(), dm.minRank(), dm.maxRank(), interval);
                    long remain = v + interval.total;
                    if (dm.iThElement(entry.getKey()) + DigitUtils.ceilDiv(remain, -entry.getValue()) > dm.iThElement(r)) {
                        out.println(-1);
                    } else {
                        out.println(dm.iThElement(entry.getKey()) + (double) remain / -entry.getValue());
                    }
                    continue;
                }

                Map.Entry<Integer, Integer> entry = events.floorEntry(time - 1);
                interval.init();
                segment.query(finalL, entry.getKey(), dm.minRank(), dm.maxRank(), interval);
                long remain = v + interval.total;
                if (dm.iThElement(entry.getKey()) + DigitUtils.ceilDiv(remain, -entry.getValue()) > dm.iThElement(r)) {
                    out.println(-1);
                } else {
                    out.println(dm.iThElement(entry.getKey()) + (double) remain / -entry.getValue());
                }
                continue;
            }
        }
    }
}

class Interval {
    long min;
    long total;

    public void merge(Interval a, Interval b) {
        min = Math.min(a.min, a.total + b.min);
        total = a.total + b.total;
    }

    public void rightMerge(Interval r) {
        min = Math.min(min, total + r.min);
        total += r.total;
    }

    public void init() {
        min = total = 0;
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private Interval interval = new Interval();

    private void modify(long x) {
        interval.total += x;
        interval.min = Math.min(0, interval.total);
    }

    public void pushUp() {
        interval.merge(left.interval, right.interval);
    }

    public void pushDown() {
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
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

    public void update(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r, Interval interval) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            interval.rightMerge(this.interval);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.query(ll, rr, l, m, interval);
        right.query(ll, rr, m + 1, r, interval);
    }
}