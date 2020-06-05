import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.AbstractCollection;
import java.util.Map;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.TreeMap;
import java.io.Closeable;
import java.io.Writer;
import java.util.Comparator;
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
            DJohnnyAndJames solver = new DJohnnyAndJames();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DJohnnyAndJames {
        Debug debug = new Debug(true);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int k = in.readInt();


            TreeMap<IntegerPoint2, List<Node>> map = new TreeMap<>(IntegerPoint2.SORT_BY_POLAR_ANGLE);

            Node center = null;
            for (int i = 0; i < n; i++) {
                Node node = new Node();
                node.pt = new IntegerPoint2(in.readInt(), in.readInt());
                node.d = node.pt.abs();
                if (node.pt.x == 0 && node.pt.y == 0) {
                    center = node;
                } else {
                    map.computeIfAbsent(node.pt, x -> new ArrayList<>()).add(node);
                }
            }

            debug.debug("map", map);

            PriorityQueue<Node> pq = new PriorityQueue<>(map.size(), (a, b) -> -Double.compare(a.weight, b.weight));
            for (List<Node> list : map.values()) {
                list.sort((a, b) -> Double.compare(a.d, b.d));
                Node last = null;
                for (int i = 0; i < list.size(); i++) {
                    Node node = list.get(i);
                    node.after = list.size() - i - 1;
                    node.weight = node.d * (k - 2 * node.after - 1);
                    node.p = last;
                    last = node;
                }
                pq.add(list.get(list.size() - 1));
            }

            int threshold = k / 2;
            double ans1 = 0;
            int got = 0;
            while (got < k - 1 && !pq.isEmpty()) {
                got++;
                Node head = pq.remove();
                ans1 += head.weight;
                if (head.p != null && head.p.after < threshold) {
                    pq.add(head.p);
                }
            }

            if (!pq.isEmpty() && pq.peek().weight > 0) {
                ans1 += pq.peek().weight;
            }

            if (got < k - 1) {
                ans1 = -1;
            }

            boolean valid = false;
            double ans2 = 0;
            for (List<Node> list : map.values()) {
                double local = 0;
                if (k - (n - list.size() + threshold) > 0) {
                    //pick all
                    int prev = k - (n - list.size() + threshold);
                    valid = true;
                    Node last = center;
                    int l = prev;
                    int r = list.size() - threshold - 1;
                    for (int i = 0; i < list.size(); i++) {
                        if (i >= l && i <= r) {
                            continue;
                        }
                        Node node = list.get(i);
                        int t = (n - list.size()) + (i < l ? i : i - (r - l + 1));
                        local += (node.d - last.d) * t * (k - t);
                        last = node;
                    }
                } else {
                    //fill
                    Node last = center;
                    for (int i = 0; i < list.size(); i++) {
                        Node node = list.get(i);
                        int t = list.size() - i;
                        local += (node.d - last.d) * t * (k - t);
                        last = node;
                    }
                }

                ans2 += local;
            }

            double ans = ans1;
            debug.debug("ans1", ans1);
            debug.debug("ans2", ans2);
            if (valid) {
                ans = Math.max(ans, ans2);
            }

            out.println(ans);
        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private StringBuilder cache = new StringBuilder(10 << 20);
        private final Writer os;

        public FastOutput append(CharSequence csq) {
            cache.append(csq);
            return this;
        }

        public FastOutput append(CharSequence csq, int start, int end) {
            cache.append(csq, start, end);
            return this;
        }

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput append(char c) {
            cache.append(c);
            return this;
        }

        public FastOutput append(double c) {
            cache.append(new BigDecimal(c).toPlainString());
            return this;
        }

        public FastOutput println(double c) {
            return append(c).println();
        }

        public FastOutput println() {
            cache.append(System.lineSeparator());
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

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;
        static int[] empty = new int[0];

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, double x) {
            if (offline) {
                debug(name, "" + x);
            }
            return this;
        }

        public Debug debug(String name, String x) {
            if (offline) {
                out.printf("%s=%s", name, x);
                out.println();
            }
            return this;
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

    static class Node {
        double d;
        IntegerPoint2 pt;
        Node p;
        int after;
        double weight;

        public String toString() {
            return pt.toString();
        }

    }

    static class IntegerPoint2 {
        public static final Comparator<IntegerPoint2> SORT_BY_POLAR_ANGLE = (a, b) ->
        {
            if (a.half() != b.half()) {
                return a.half() - b.half();
            }
            return orient(b, a);
        };
        public final long x;
        public final long y;

        public IntegerPoint2(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public int half() {
            return y > 0 || y == 0 && x < 0 ? 1 : 0;
        }

        public long square() {
            return x * x + y * y;
        }

        public double abs() {
            return Math.sqrt(square());
        }

        public static long cross(IntegerPoint2 a, IntegerPoint2 b) {
            return a.x * b.y - a.y * b.x;
        }

        public static int orient(IntegerPoint2 b, IntegerPoint2 c) {
            return GeoConstant.sign(cross(b, c));
        }

        public IntegerPoint2 clone() {
            try {
                return (IntegerPoint2) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        public String toString() {
            return String.format("(%d, %d)", x, y);
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

    static class GeoConstant {
        public static final double PREC = 1e-10;

        public static boolean isZero(double x) {
            return -PREC <= x && x <= PREC;
        }

        public static int sign(double x) {
            return isZero(x) ? 0 : x < 0 ? -1 : 1;
        }

    }
}

