package template.datastructure;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class IntervalBooleanMap implements Iterable<IntervalBooleanMap.Interval> {
    public static class Interval {
        long l;
        long r;

        long length() {
            return r - l + 1;
        }

        @Override
        public String toString() {
            return String.format("[%d, %d]", l, r);
        }
    }

    private TreeMap<Long, Interval> map = new TreeMap<>();
    private long total = 0;

    public long countTrue() {
        return total;
    }

    public void removeInterval(Interval interval) {
        map.remove(interval.l);
        total -= interval.length();
    }

    public void addInterval(Interval interval) {
        if (interval.length() <= 0) {
            return;
        }
        map.put(interval.l, interval);
        total += interval.length();
    }

    public long firstFalse(long l) {
        Map.Entry<Long, Interval> entry = map.floorEntry(l);
        if (entry == null || entry.getValue().r < l) {
            return l;
        }
        Interval last = entry.getValue();
        while (true) {
            Map.Entry<Long, Interval> ceil = map.ceilingEntry(last.r + 1);
            if (ceil == null || ceil.getValue().l > last.r + 1) {
                break;
            }
            last.r = ceil.getValue().r;
            map.remove(ceil.getKey());
        }
        return entry.getValue().r + 1;
    }

    public void setTrue(long l, long r) {
        if (l > r) {
            return;
        }
        Interval interval = new Interval();
        interval.l = l;
        interval.r = r;
        while (true) {
            Map.Entry<Long, Interval> floorEntry = map.floorEntry(interval.l);
            if (floorEntry == null) {
                break;
            }
            Interval floorInterval = floorEntry.getValue();
            if (floorInterval.r >= interval.r) {
                return;
            } else if (floorInterval.r < interval.l) {
                break;
            } else {
                interval.l = Math.min(interval.l, floorInterval.l);
                removeInterval(floorInterval);
            }
        }
        while (true) {
            Map.Entry<Long, Interval> ceilEntry = map.ceilingEntry(interval.l);
            if (ceilEntry == null) {
                break;
            }
            Interval ceilInterval = ceilEntry.getValue();
            if (ceilInterval.l <= interval.l) {
                return;
            } else if (ceilInterval.l > interval.r) {
                break;
            } else {
                interval.r = Math.max(interval.r, ceilInterval.r);
                removeInterval(ceilInterval);
            }
        }

        addInterval(interval);
    }

    public void setFalse(long l, long r) {
        while (true) {
            Map.Entry<Long, Interval> floorEntry = map.floorEntry(l);
            if (floorEntry == null) {
                break;
            }
            Interval floorInterval = floorEntry.getValue();
            if (floorInterval.r < l) {
                break;
            } else if (floorInterval.r > r) {
                removeInterval(floorInterval);
                Interval lPart = floorInterval;
                Interval rPart = new Interval();
                rPart.l = r + 1;
                rPart.r = floorInterval.r;
                lPart.r = l - 1;
                addInterval(lPart);
                addInterval(rPart);
                return;
            } else if (floorInterval.l >= l) {
                removeInterval(floorInterval);
            } else {
                removeInterval(floorInterval);
                floorInterval.r = l - 1;
                addInterval(floorInterval);
                break;
            }
        }
        while (true) {
            Map.Entry<Long, Interval> ceilEntry = map.ceilingEntry(l);
            if (ceilEntry == null) {
                break;
            }
            Interval ceilInterval = ceilEntry.getValue();
            if (ceilInterval.l > r) {
                break;
            } else if (ceilInterval.l < l) {
                removeInterval(ceilInterval);
                Interval lPart = new Interval();
                Interval rPart = ceilInterval;
                lPart.l = ceilInterval.l;
                lPart.r = l - 1;
                rPart.l = r + 1;
                addInterval(lPart);
                addInterval(rPart);
                return;
            } else if (ceilInterval.r <= r) {
                removeInterval(ceilInterval);
            } else {
                removeInterval(ceilInterval);
                ceilInterval.l = r + 1;
                addInterval(ceilInterval);
                break;
            }
        }
    }

    public boolean getValue(long index) {
        Map.Entry<Long, Interval> entry = map.floorEntry(index);
        return entry != null && entry.getValue().r >= index;
    }

    public boolean or(long l, long r) {
        Map.Entry<Long, Interval> entry = map.floorEntry(r);
        return entry != null && entry.getValue().r >= l;
    }

    @Override
    public Iterator<Interval> iterator() {
        return map.values().iterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Interval interval : map.values()) {
            builder.append(interval).append(',');
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
