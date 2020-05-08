import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.io.Writer;
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
            CGoodbyeSouvenir solver = new CGoodbyeSouvenir();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class CGoodbyeSouvenir {
        Debug debug = new Debug(true);
        LongBIT bit = new LongBIT(200000);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            int[] a = new int[n];
            Point[] lPoints = new Point[n];
            Point[] rPoints = new Point[n];
            for (int i = 0; i < n; i++) {
                a[i] = in.readInt();
            }

            List<Point> left = new ArrayList<>(10 * (n + m));
            List<Point> right = new ArrayList<>(10 * (n + m));
            List<Query> queries = new ArrayList<>(m);

            TreeSet<Integer>[] maps = new TreeSet[n + 1];
            for (int i = 1; i <= n; i++) {
                maps[i] = new TreeSet<>();
            }
            for (int i = 0; i < n; i++) {
                maps[a[i]].add(i);
            }

            for (int i = 0; i < n; i++) {
                Integer pre = maps[a[i]].floor(i - 1);
                Integer post = maps[a[i]].ceiling(i + 1);

                Point p1 = new Point();
                p1.x = i;
                p1.y = pre == null ? -1 : pre;
                p1.z = 1;
                p1.w = i;
                lPoints[i] = p1;

                Point p2 = new Point();
                p2.x = i;
                p2.y = post == null ? n : post;
                p2.z = 1;
                p2.w = i;
                rPoints[i] = p2;

                left.add(p1);
                right.add(p2);
            }

            for (int i = 2; i < 2 + m; i++) {
                int t = in.readInt();
                if (t == 1) {
                    int p = in.readInt() - 1;
                    int x = in.readInt();

                    {
                        //remove
                        maps[a[p]].remove(p);
                        Integer pre = maps[a[p]].floor(p - 1);
                        Integer post = maps[a[p]].ceiling(p + 1);

                        left.add(lPoints[p].negate(i));
                        right.add(rPoints[p].negate(i));
                        if (pre != null) {
                            right.add(rPoints[pre].negate(i));
                            rPoints[pre] = rPoints[pre].clone(i);
                            rPoints[pre].y = post == null ? n : post;
                            right.add(rPoints[pre]);
                        }
                        if (post != null) {
                            left.add(lPoints[post].negate(i));
                            lPoints[post] = lPoints[post].clone(i);
                            lPoints[post].y = pre == null ? -1 : pre;
                            left.add(lPoints[post]);
                        }
                    }

                    {
                        //add
                        a[p] = x;

                        maps[a[p]].add(p);
                        Integer pre = maps[a[p]].floor(p - 1);
                        Integer post = maps[a[p]].ceiling(p + 1);

                        lPoints[p] = lPoints[p].clone(i);
                        lPoints[p].y = pre == null ? -1 : pre;
                        rPoints[p] = rPoints[p].clone(i);
                        rPoints[p].y = post == null ? n : post;
                        left.add(lPoints[p]);
                        right.add(rPoints[p]);


                        if (pre != null) {
                            right.add(rPoints[pre].negate(i));
                            rPoints[pre] = rPoints[pre].clone(i);
                            rPoints[pre].y = p;
                            right.add(rPoints[pre]);
                        }
                        if (post != null) {
                            left.add(lPoints[post].negate(i));
                            lPoints[post] = lPoints[post].clone(i);
                            lPoints[post].y = p;
                            left.add(lPoints[post]);
                        }
                    }
                } else {
                    int l = in.readInt() - 1;
                    int r = in.readInt() - 1;
                    Query q = new Query();
                    Point lQ = new Point();
                    lQ.x = r;
                    lQ.y = l - 1;
                    lQ.z = i;
                    lQ.t = 1;
                    q.sub[0] = lQ;
                    q.add[0] = lQ.clone(i);
                    q.add[0].x = l - 1;


                    Point rQ = new Point();
                    rQ.x = l;
                    rQ.y = r + 1;
                    rQ.z = i;
                    rQ.t = 1;
                    q.add[1] = rQ;
                    q.sub[1] = rQ.clone(i);
                    q.sub[1].x = r + 1;

                    left.add(q.add[0]);
                    left.add(q.sub[0]);
                    right.add(q.add[1]);
                    right.add(q.sub[1]);

                    queries.add(q);
                }
            }


            for (Point r : right) {
                r.z = 200000 - r.z;
            }

            debug.debug("left", left);
            debug.debug("right", right);

            left.sort((x, y) -> {
                int cp = x.x - y.x;
                if (cp == 0) {
                    cp = x.y - y.y;
                }
                if (cp == 0) {
                    cp = x.z - y.z;
                }
                if (cp == 0) {
                    cp = x.t - y.t;
                }
                return cp;
            });


            right.sort((x, y) -> {
                int cp = x.x - y.x;
                if (cp == 0) {
                    cp = x.y - y.y;
                }
                if (cp == 0) {
                    cp = x.z - y.z;
                }
                if (cp == 0) {
                    cp = -(x.t - y.t);
                }
                return cp;
            });


            dac1(left.toArray(new Point[0]), 0, left.size() - 1);
            dac2(right.toArray(new Point[0]), 0, right.size() - 1);


            for (int i = 0; i < queries.size(); i++) {
                Query q = queries.get(i);
                long ans = 0;
                for (int j = 0; j < 2; j++) {
                    ans += q.add[j].sum - q.sub[j].sum;
                }
                out.println(ans);
            }
        }

        public void dac1(Point[] pts, int l, int r) {
            if (l == r) {
                return;
            }
            int m = (l + r) / 2;
            dac1(pts, l, m);
            dac1(pts, m + 1, r);
            Arrays.sort(pts, l, m + 1, (a, b) -> Integer.compare(a.y, b.y));
            Arrays.sort(pts, m + 1, r + 1, (a, b) -> Integer.compare(a.y, b.y));

            int i = l;
            int j = m + 1;
            while (j <= r) {
                while (i <= m && pts[i].y <= pts[j].y) {
                    bit.update(pts[i].z, pts[i].w);
                    i++;
                }
                pts[j].sum += bit.query(pts[j].z);
                j++;
            }

            while (i - 1 >= l) {
                i--;
                bit.update(pts[i].z, -pts[i].w);
            }
        }

        public void dac2(Point[] pts, int l, int r) {
            if (l == r) {
                return;
            }
            int m = (l + r) / 2;
            dac2(pts, l, m);
            dac2(pts, m + 1, r);
            Arrays.sort(pts, l, m + 1, (a, b) -> -Integer.compare(a.y, b.y));
            Arrays.sort(pts, m + 1, r + 1, (a, b) -> -Integer.compare(a.y, b.y));

            int i = l;
            int j = m + 1;
            while (i <= m) {
                while (j <= r && pts[i].y <= pts[j].y) {
                    bit.update(pts[j].z, pts[j].w);
                    j++;
                }
                pts[i].sum += bit.query(200000) - bit.query(pts[i].z - 1);
                i++;
            }

            while (j - 1 > m) {
                j--;
                bit.update(pts[j].z, -pts[j].w);
            }
        }

    }

    static class Query {
        Point[] add = new Point[2];
        Point[] sub = new Point[2];

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

    static class LongBIT {
        private long[] data;
        private int n;

        public LongBIT(int n) {
            this.n = n;
            data = new long[n + 1];
        }

        public long query(int i) {
            long sum = 0;
            for (; i > 0; i -= i & -i) {
                sum += data[i];
            }
            return sum;
        }

        public void update(int i, long mod) {
            if (i <= 0) {
                return;
            }
            for (; i <= n; i += i & -i) {
                data[i] += mod;
            }
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i <= n; i++) {
                builder.append(query(i) - query(i - 1)).append(' ');
            }
            return builder.toString();
        }

    }

    static class Point implements Cloneable {
        int x;
        int y;
        int z;
        int w;
        int t;
        long sum;

        public Point negate(int z) {
            Point ans = clone(z);
            ans.w = -ans.w;
            return ans;
        }

        public Point clone(int z) {
            try {
                Point pt = (Point) super.clone();
                pt.z = z;
                return pt;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        public String toString() {
            return String.format("(%d, %d, %d) = %d", x, y, z, w);
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

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private StringBuilder cache = new StringBuilder(1 << 20);
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

        public FastOutput append(long c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(long c) {
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
}

