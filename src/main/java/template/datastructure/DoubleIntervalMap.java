package template.datastructure;

import template.math.KahanSummation;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class DoubleIntervalMap implements Iterable<DoubleIntervalMap.Interval> {
    public static class Interval {
        public double l;
        public double r;

        public double length() {
            return r - l;
        }

        @Override
        public String toString() {
            return String.format("[%f, %f]", l, r);
        }
    }

    private TreeMap<Double, DoubleIntervalMap.Interval> map = new TreeMap<>();
    private KahanSummation total = new KahanSummation();

    public double countTrue() {
        return total.sum();
    }

    private void removeInterval(DoubleIntervalMap.Interval interval) {
        map.remove(interval.l);
        total.subtract(interval.length());
    }

    private void addInterval(DoubleIntervalMap.Interval interval) {
        if (interval.length() <= 0) {
            return;
        }
        map.put(interval.l, interval);
        total.add(interval.length());
    }

    public double firstFalse(double l) {
        Map.Entry<Double, DoubleIntervalMap.Interval> entry = map.floorEntry(l);
        if (entry == null || entry.getValue().r < l) {
            return l;
        }
        return entry.getValue().r;
    }

    /**
     * First true index, or null if no such index
     */
    public Double firstTrue(double l) {
        Map.Entry<Double, DoubleIntervalMap.Interval> entry = map.floorEntry(l);
        if (entry != null && entry.getValue().r >= l) {
            return Math.max(l, entry.getValue().l);
        }
        entry = map.ceilingEntry(l);
        if (entry != null) {
            return entry.getKey();
        }
        return null;
    }

    public void set(double l, double r, boolean v) {
        if (v) {
            setTrue(l, r);
        } else {
            setFalse(l, r);
        }
    }

    public void setTrue(double l, double r) {
        if (r <= l) {
            return;
        }
        DoubleIntervalMap.Interval interval = new DoubleIntervalMap.Interval();
        interval.l = l;
        interval.r = r;
        while (true) {
            Map.Entry<Double, DoubleIntervalMap.Interval> floorEntry = map.floorEntry(interval.l);
            if (floorEntry == null) {
                break;
            }
            DoubleIntervalMap.Interval floorInterval = floorEntry.getValue();
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
            Map.Entry<Double, DoubleIntervalMap.Interval> ceilEntry = map.ceilingEntry(interval.l);
            if (ceilEntry == null) {
                break;
            }
            DoubleIntervalMap.Interval ceilInterval = ceilEntry.getValue();
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

    public void setFalse(double l, double r) {
        if (r <= l) {
            return;
        }
        while (true) {
            Map.Entry<Double, DoubleIntervalMap.Interval> floorEntry = map.floorEntry(l);
            if (floorEntry == null) {
                break;
            }
            DoubleIntervalMap.Interval floorInterval = floorEntry.getValue();
            if (floorInterval.r < l) {
                break;
            } else if (floorInterval.r > r) {
                removeInterval(floorInterval);
                DoubleIntervalMap.Interval lPart = floorInterval;
                DoubleIntervalMap.Interval rPart = new DoubleIntervalMap.Interval();
                rPart.l = r;
                rPart.r = floorInterval.r;
                lPart.r = l;
                addInterval(lPart);
                addInterval(rPart);
                return;
            } else if (floorInterval.l >= l) {
                removeInterval(floorInterval);
            } else {
                removeInterval(floorInterval);
                floorInterval.r = l;
                addInterval(floorInterval);
                break;
            }
        }
        while (true) {
            Map.Entry<Double, DoubleIntervalMap.Interval> ceilEntry = map.ceilingEntry(l);
            if (ceilEntry == null) {
                break;
            }
            DoubleIntervalMap.Interval ceilInterval = ceilEntry.getValue();
            if (ceilInterval.l > r) {
                break;
            } else if (ceilInterval.l < l) {
                removeInterval(ceilInterval);
                DoubleIntervalMap.Interval lPart = new DoubleIntervalMap.Interval();
                DoubleIntervalMap.Interval rPart = ceilInterval;
                lPart.l = ceilInterval.l;
                lPart.r = l;
                rPart.l = r;
                addInterval(lPart);
                addInterval(rPart);
                return;
            } else if (ceilInterval.r <= r) {
                removeInterval(ceilInterval);
            } else {
                removeInterval(ceilInterval);
                ceilInterval.l = r;
                addInterval(ceilInterval);
                break;
            }
        }
    }

    public boolean getValue(double index) {
        Map.Entry<Double, DoubleIntervalMap.Interval> entry = map.floorEntry(index);
        return entry != null && entry.getValue().r >= index;
    }

    public boolean or(double l, double r) {
        Map.Entry<Double, DoubleIntervalMap.Interval> entry = map.floorEntry(r);
        return entry != null && entry.getValue().r >= l;
    }

    @Override
    public Iterator<DoubleIntervalMap.Interval> iterator() {
        return map.values().iterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (DoubleIntervalMap.Interval interval : map.values()) {
            builder.append(interval).append(',');
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
