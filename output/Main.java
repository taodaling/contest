import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.TreeMap;
import java.io.Closeable;
import java.util.Map;
import java.io.Writer;
import java.util.Map.Entry;
import java.io.OutputStreamWriter;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "", 1 << 27);
        thread.start();
        thread.join();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            IntervalBooleanMapTest solver = new IntervalBooleanMapTest();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class IntervalBooleanMapTest {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int q = in.readInt();
            IntervalBooleanMap map = new IntervalBooleanMap();
            for (int i = 0; i < q; i++) {
                int cmd = in.readInt();
                if (cmd == 0) {
                    int l = in.readInt();
                    int r = in.readInt();
                    map.setTrue(l, r);
                } else if (cmd == 1) {
                    int l = in.readInt();
                    int r = in.readInt();
                    map.setFalse(l, r);
                } else {
                    out.println(map.countTrue());
                }
            }
        }

    }

    static class IntervalBooleanMap {
        private TreeMap<Long, IntervalBooleanMap.Interval> map = new TreeMap<>();
        private long total = 0;

        public long countTrue() {
            return total;
        }

        public void removeInterval(IntervalBooleanMap.Interval interval) {
            map.remove(interval.l);
            total -= interval.length();
        }

        public void addInterval(IntervalBooleanMap.Interval interval) {
            if (interval.length() <= 0) {
                return;
            }
            map.put(interval.l, interval);
            total += interval.length();
        }

        public void setTrue(long l, long r) {
            if (l > r) {
                return;
            }
            IntervalBooleanMap.Interval interval = new IntervalBooleanMap.Interval();
            interval.l = l;
            interval.r = r;
            while (true) {
                Map.Entry<Long, IntervalBooleanMap.Interval> floorEntry = map.floorEntry(interval.l);
                if (floorEntry == null) {
                    break;
                }
                IntervalBooleanMap.Interval floorInterval = floorEntry.getValue();
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
                Map.Entry<Long, IntervalBooleanMap.Interval> ceilEntry = map.ceilingEntry(interval.l);
                if (ceilEntry == null) {
                    break;
                }
                IntervalBooleanMap.Interval ceilInterval = ceilEntry.getValue();
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
                Map.Entry<Long, IntervalBooleanMap.Interval> floorEntry = map.floorEntry(l);
                if (floorEntry == null) {
                    break;
                }
                IntervalBooleanMap.Interval floorInterval = floorEntry.getValue();
                if (floorInterval.r < l) {
                    break;
                } else if (floorInterval.r > r) {
                    removeInterval(floorInterval);
                    IntervalBooleanMap.Interval lPart = floorInterval;
                    IntervalBooleanMap.Interval rPart = new IntervalBooleanMap.Interval();
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
                Map.Entry<Long, IntervalBooleanMap.Interval> ceilEntry = map.ceilingEntry(l);
                if (ceilEntry == null) {
                    break;
                }
                IntervalBooleanMap.Interval ceilInterval = ceilEntry.getValue();
                if (ceilInterval.l > r) {
                    break;
                } else if (ceilInterval.l < l) {
                    removeInterval(ceilInterval);
                    IntervalBooleanMap.Interval lPart = new IntervalBooleanMap.Interval();
                    IntervalBooleanMap.Interval rPart = ceilInterval;
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

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (IntervalBooleanMap.Interval interval : map.values()) {
                builder.append(interval).append(',');
            }
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }
            return builder.toString();
        }

        private static class Interval {
            long l;
            long r;

            long length() {
                return r - l + 1;
            }

            public String toString() {
                return String.format("[%d, %d]", l, r);
            }

        }

    }

    static class FastInput {
        private final InputStream is;
        private byte[] buf = new byte[1 << 13];
        private int bufLen;
        private int bufOffset;
        private int next;

        public FastInput(InputStream is) {
            this.is = is;
        }

        private int read() {
            while (bufLen == bufOffset) {
                bufOffset = 0;
                try {
                    bufLen = is.read(buf);
                } catch (IOException e) {
                    bufLen = -1;
                }
                if (bufLen == -1) {
                    return -1;
                }
            }
            return buf[bufOffset++];
        }

        public void skipBlank() {
            while (next >= 0 && next <= 32) {
                next = read();
            }
        }

        public int readInt() {
            int sign = 1;

            skipBlank();
            if (next == '+' || next == '-') {
                sign = next == '+' ? 1 : -1;
                next = read();
            }

            int val = 0;
            if (sign == 1) {
                while (next >= '0' && next <= '9') {
                    val = val * 10 + next - '0';
                    next = read();
                }
            } else {
                while (next >= '0' && next <= '9') {
                    val = val * 10 - next + '0';
                    next = read();
                }
            }

            return val;
        }

    }

    static class FastOutput implements AutoCloseable, Closeable {
        private StringBuilder cache = new StringBuilder(10 << 20);
        private final Writer os;

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput println(long c) {
            cache.append(c).append('\n');
            return this;
        }

        public FastOutput flush() {
            try {
                os.append(cache);
                os.flush();
                cache.setLength(0);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return this;
        }

        public void close() {
            flush();
            try {
                os.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public String toString() {
            return cache.toString();
        }

    }
}

