package template.primitve.generated.datastructure;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class LongIntervalMap implements Iterable<LongIntervalMap.Interval> {
    private long total = 0;
    private TreeMap<Long, Interval> map = new TreeMap<>();
    private static long inf = Long.MAX_VALUE / 4;

    private void add(Interval interval) {
        if (interval.length() <= 0) {
            return;
        }
        map.put(interval.l, interval);
        total += interval.length();
    }

    private void remove(Interval interval) {
        map.remove(interval.l);
        total -= interval.length();
    }


    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Interval last() {
        return map.lastEntry().getValue();
    }

    public Interval first() {
        return map.firstEntry().getValue();
    }

    public long total() {
        return total;
    }

    public int blockCount() {
        return map.size();
    }

    public void and(LongIntervalMap map) {
        long l = -inf;
        for (Interval interval : map) {
            remove(l, interval.l);
            l = interval.r;
        }
    }

    public void or(LongIntervalMap map) {
        for (Interval interval : map) {
            add(interval.l, interval.r);
        }
    }

    @Override
    public Iterator<Interval> iterator() {
        return map.values().iterator();
    }

    /**
     * [l, r)
     */
    public void add(long l, long r) {
        if (l >= r) {
            return;
        }
        Interval interval = new Interval();
        interval.l = l;
        interval.r = r;
        while (true) {
            Map.Entry<Long, Interval> ceilEntry = map.ceilingEntry(interval.l);
            if (ceilEntry == null || ceilEntry.getValue().l > interval.r) {
                break;
            }
            Interval ceil = ceilEntry.getValue();
            remove(ceil);
            interval.r = Math.max(interval.r, ceil.r);
        }
        while (true) {
            Map.Entry<Long, Interval> floorEntry = map.floorEntry(interval.l);
            if (floorEntry == null || floorEntry.getValue().r < interval.l) {
                break;
            }
            Interval floor = floorEntry.getValue();
            remove(floor);
            interval.l = Math.min(interval.l, floor.l);
            interval.r = Math.max(interval.r, floor.r);
        }
        add(interval);
    }

    public void remove(long l, long r) {
        if (l >= r) {
            return;
        }
        while (true) {
            Map.Entry<Long, Interval> ceilEntry = map.ceilingEntry(l);
            if (ceilEntry == null || ceilEntry.getValue().l >= r) {
                break;
            }
            Interval ceil = ceilEntry.getValue();
            remove(ceil);
            ceil.l = r;
            add(ceil);
        }
        while (true) {
            Map.Entry<Long, Interval> floorEntry = map.floorEntry(l);
            if (floorEntry == null || floorEntry.getValue().r <= l) {
                break;
            }
            Interval floor = floorEntry.getValue();
            remove(floor);
            if (floor.r > r) {
                Interval left = floor;
                Interval right = new Interval();
                right.l = r;
                right.r = left.r;
                left.r = l;
                add(left);
                add(right);
                break;
            }
            floor.r = l;
            add(floor);
        }
    }

    /**
     * [l, r)
     */
    public static class Interval {
        public long l;
        public long r;

        public long length() {
            return r - l;
        }

        @Override
        public String toString() {
            return "[" + l + "," + r + ")";
        }
    }

    @Override
    public String toString() {
        return map.values().toString();
    }

    public void clear() {
        map.clear();
        total = 0;
    }
}
