import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Collection;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.TreeMap;
import java.io.Closeable;
import java.io.Writer;
import java.util.Map.Entry;
import java.util.Comparator;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "", 1 << 29);
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
            EGeraldAndPath solver = new EGeraldAndPath();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class EGeraldAndPath {
        Debug debug = new Debug(true);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.ri();
            long[] x = new long[n];
            long[] l = new long[n];
            for (int i = 0; i < n; i++) {
                x[i] = in.ri();
                l[i] = in.ri();
            }
            Pair<int[], Long> res = StickFallProblem.solve(x, l);
            long ans = res.b;
            debug.debug("res.a", res.a);
            debug.debug("res.b", res.b);
            out.println(ans);
            LongIntervalMap map = new LongIntervalMap();
            for (int i = 0; i < n; i++) {
                if (res.a[i] == -1) {
                    map.add(x[i] - l[i], x[i]);
                } else {
                    map.add(x[i], x[i] + l[i]);
                }
            }
            if (map.total() != res.b) {
                throw new RuntimeException();
            }
        }

    }

    static class StickFallProblem {
        static long inf = (long) 2e18;

        public static Pair<int[], Long> solve(long[] x, long[] l) {
            int n = x.length;
            StickFallProblem.Stick[] light = new StickFallProblem.Stick[n + 1];
            for (int i = 0; i < n; i++) {
                light[i] = new StickFallProblem.Stick();
                light[i].id = i;
                light[i].x = x[i];
                light[i].l = l[i];
            }
            light[n] = new StickFallProblem.Stick();
            light[n].x = -inf;
            light[n].l = 0;
            Arrays.sort(light, Comparator.comparingLong(t -> t.x));
            long[][][] dp = new long[2][n + 1][n + 1];
            SequenceUtils.deepFill(dp, -inf);
            dp[0][0][0] = 0;
            for (int i = 1; i <= n; i++) {
                for (int k = 0; k <= i; k++) {
                    for (int t = 0; t < 2; t++) {
                        //skip
                        dp[t][i][k] = dp[t][i - 1][k];
                    }
                }
                for (int k = 0; k < i; k++) {
                    for (int t = 0; t < 2; t++) {
                        long r = light[k].x + t * light[k].l;
                        if (r <= light[i].x) {
                            long cand = Math.min(light[i].x - r, light[i].l) + dp[t][i - 1][k];
                            if (cand > dp[0][i][i]) {
                                dp[0][i][i] = cand;
                            }
                        }
                        if (r <= light[i].x + light[i].l) {
                            long cand = Math.min(light[i].x + light[i].l - r, light[i].l) + dp[t][i - 1][k];
                            if (cand > dp[1][i][i]) {
                                dp[1][i][i] = cand;
                            }
                        }
                    }
                }

                long left = light[i].x - light[i].l;
                //cross
                for (int j = 0; j < i - 1; j++) {
                    for (int t = 0; t <= j; t++) {
                        for (int d = 0; d < 2; d++) {
                            long r = light[t].x + light[t].l * d;
                            if (r >= light[j + 1].x) {
                                continue;
                            }
                            long rr = light[j + 1].x + light[j + 1].l;
                            if (rr < light[i].x || left >= light[j + 1].x) {
                                continue;
                            }
                            long cand = dp[d][j][t] + light[j + 1].x - Math.max(left, r) +
                                    light[j + 1].l;
                            if (cand > dp[1][i][j + 1]) {
                                dp[1][i][j + 1] = cand;
                            }
                        }
                    }
                }
            }
            long ans = -1;
            int fi = -1;
            int fj = -1;
            int fk = -1;
            for (int i = 0; i <= n; i++) {
                for (int j = 0; j <= n; j++) {
                    for (int k = 0; k < 2; k++) {
                        if (dp[k][i][j] > ans) {
                            ans = dp[k][i][j];
                            fi = i;
                            fj = j;
                            fk = k;
                        }
                    }
                }
            }
            int[] sol = new int[n];
            Arrays.fill(sol, -1);
            while (fi > 0) {
                boolean find = false;
                if (dp[fk][fi][fj] == dp[fk][fi - 1][fj]) {
                    //skip
                    fi--;
                    find = true;
                }
                if (fj == fi && !find) {
                    for (int k = 0; k < fi && !find; k++) {
                        for (int t = 0; t < 2 && !find; t++) {
                            long r = light[k].x + t * light[k].l;
                            if (r <= light[fi].x && fk == 0) {
                                long cand = Math.min(light[fi].x - r, light[fi].l) + dp[t][fi - 1][k];
                                if (cand == dp[fk][fi][fj]) {
                                    sol[light[fi].id] = -1;
                                    fi = fi - 1;
                                    fk = t;
                                    fj = k;
                                    find = true;
                                    break;
                                }
                            }
                            if (r <= light[fi].x + light[fi].l && fk == 1) {
                                long cand = Math.min(light[fi].x + light[fi].l - r, light[fi].l) + dp[t][fi - 1][k];
                                if (cand == dp[fk][fi][fj]) {
                                    sol[light[fi].id] = 1;
                                    fi = fi - 1;
                                    fk = t;
                                    fj = k;
                                    find = true;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (!find && fk == 1) {
                    long left = light[fi].x - light[fi].l;
                    //cross
                    int j = fj - 1;
                    for (int t = 0; t <= j && !find; t++) {
                        for (int d = 0; d < 2 && !find; d++) {
                            long r = light[t].x + light[t].l * d;
                            if (r >= light[j + 1].x) {
                                continue;
                            }
                            long rr = light[j + 1].x + light[j + 1].l;
                            if (rr < light[fi].x || left >= light[j + 1].x) {
                                continue;
                            }
                            long cand = dp[d][j][t] + light[j + 1].x - Math.max(left, r) +
                                    light[j + 1].l;
                            if (cand == dp[fk][fi][fj]) {
                                sol[light[fi].id] = -1;
                                sol[light[fj].id] = 1;
                                fk = d;
                                fi = j;
                                fj = t;
                                find = true;
                                break;
                            }
                        }
                    }
                }
            }
            return new Pair<>(sol, ans);
        }

        static class Stick {
            long x;
            long l;
            int id;

        }

    }

    static class LongIntervalMap implements Iterable<LongIntervalMap.Interval> {
        private long total = 0;
        private TreeMap<Long, LongIntervalMap.Interval> map = new TreeMap<>();

        private void add(LongIntervalMap.Interval interval) {
            if (interval.length() <= 0) {
                return;
            }
            map.put(interval.l, interval);
            total += interval.length();
        }

        private void remove(LongIntervalMap.Interval interval) {
            map.remove(interval.l);
            total -= interval.length();
        }

        public long total() {
            return total;
        }

        public Iterator<LongIntervalMap.Interval> iterator() {
            return map.values().iterator();
        }

        public void add(long l, long r) {
            if (l >= r) {
                return;
            }
            LongIntervalMap.Interval interval = new LongIntervalMap.Interval();
            interval.l = l;
            interval.r = r;
            while (true) {
                Map.Entry<Long, LongIntervalMap.Interval> ceilEntry = map.ceilingEntry(interval.l);
                if (ceilEntry == null || ceilEntry.getValue().l > interval.r) {
                    break;
                }
                LongIntervalMap.Interval ceil = ceilEntry.getValue();
                remove(ceil);
                interval.r = Math.max(interval.r, ceil.r);
            }
            while (true) {
                Map.Entry<Long, LongIntervalMap.Interval> floorEntry = map.floorEntry(interval.l);
                if (floorEntry == null || floorEntry.getValue().r < interval.l) {
                    break;
                }
                LongIntervalMap.Interval floor = floorEntry.getValue();
                remove(floor);
                interval.l = Math.min(interval.l, floor.l);
                interval.r = Math.max(interval.r, floor.r);
            }
            add(interval);
        }

        public String toString() {
            return map.values().toString();
        }

        public static class Interval {
            public long l;
            public long r;

            public long length() {
                return r - l;
            }

            public String toString() {
                return "[" + l + "," + r + ")";
            }

        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private static final int THRESHOLD = 1 << 13;
        private final Writer os;
        private StringBuilder cache = new StringBuilder(THRESHOLD * 2);

        public FastOutput append(CharSequence csq) {
            cache.append(csq);
            return this;
        }

        public FastOutput append(CharSequence csq, int start, int end) {
            cache.append(csq, start, end);
            return this;
        }

        private void afterWrite() {
            if (cache.length() < THRESHOLD) {
                return;
            }
            flush();
        }

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput append(char c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput append(long c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput append(String c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput println(long c) {
            return append(c).println();
        }

        public FastOutput println() {
            return append(System.lineSeparator());
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

        public int ri() {
            return readInt();
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

    static class SequenceUtils {
        public static void deepFill(Object array, long val) {
            if (!array.getClass().isArray()) {
                throw new IllegalArgumentException();
            }
            if (array instanceof long[]) {
                long[] longArray = (long[]) array;
                Arrays.fill(longArray, val);
            } else {
                Object[] objArray = (Object[]) array;
                for (Object obj : objArray) {
                    deepFill(obj, val);
                }
            }
        }

    }

    static class Pair<A, B> implements Cloneable {
        public A a;
        public B b;

        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }

        public String toString() {
            return "a=" + a + ",b=" + b;
        }

        public int hashCode() {
            return a.hashCode() * 31 + b.hashCode();
        }

        public boolean equals(Object obj) {
            Pair<A, B> casted = (Pair<A, B>) obj;
            return Objects.equals(casted.a, a) && Objects.equals(casted.b, b);
        }

        public Pair<A, B> clone() {
            try {
                return (Pair<A, B>) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;
        static int[] empty = new int[0];

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, Object x) {
            return debug(name, x, empty);
        }

        public Debug debug(String name, Object x, int... indexes) {
            if (offline) {
                if (x == null || !x.getClass().isArray()) {
                    out.append(name);
                    for (int i : indexes) {
                        out.printf("[%d]", i);
                    }
                    out.append("=").append("" + x);
                    out.println();
                } else {
                    indexes = Arrays.copyOf(indexes, indexes.length + 1);
                    if (x instanceof byte[]) {
                        byte[] arr = (byte[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof short[]) {
                        short[] arr = (short[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof boolean[]) {
                        boolean[] arr = (boolean[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof char[]) {
                        char[] arr = (char[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof int[]) {
                        int[] arr = (int[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof float[]) {
                        float[] arr = (float[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof double[]) {
                        double[] arr = (double[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof long[]) {
                        long[] arr = (long[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else {
                        Object[] arr = (Object[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    }
                }
            }
            return this;
        }

    }
}

