import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.Map;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.util.stream.Collectors;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Stream;
import java.io.Closeable;
import java.io.Writer;
import java.util.Comparator;
import java.util.Collections;
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
            FAirSafety solver = new FAirSafety();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class FAirSafety {
        long ans = (long) 1e18;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            List<Point>[] upDown = new List[]{new ArrayList(n), new ArrayList(n)};
            List<Point>[] leftRight = new List[]{new ArrayList(n), new ArrayList(n)};
            for (int i = 0; i < n; i++) {
                Point pt = new Point();
                pt.x = in.readInt() * 10;
                pt.y = in.readInt() * 10;
                switch (in.readChar()) {
                    case 'U':
                        upDown[0].add(pt);
                        break;
                    case 'D':
                        upDown[1].add(pt);
                        break;
                    case 'L':
                        leftRight[0].add(pt);
                        break;
                    default:
                        leftRight[1].add(pt);
                }
            }

            Comparator<Point> sortByX = (a, b) -> Integer.compare(a.x, b.x);
            for (int i = 0; i < 2; i++) {
                upDown[i].sort(sortByX);
                leftRight[i].sort(sortByX);
            }

            optimizeHorizontal(cast(leftRight[1], x -> x.y, x -> x.x),
                    cast(leftRight[0], x -> x.y, x -> x.x));
            optimizeHorizontal(cast(upDown[0], x -> x.x, x -> x.y),
                    cast(upDown[1], x -> x.x, x -> x.y));


            //inc
            Map<Integer, List<Point>>[] uDGroupByInc = new Map[2];
            Map<Integer, List<Point>>[] lRGGroupByInc = new Map[2];
            for (int i = 0; i < 2; i++) {
                uDGroupByInc[i] = upDown[i].stream().collect(Collectors.groupingBy(x -> x.y - x.x));
                lRGGroupByInc[i] = leftRight[i].stream().collect(Collectors.groupingBy(x -> x.y - x.x));
            }

            //dec
            Map<Integer, List<Point>>[] uDGroupByDec = new Map[2];
            Map<Integer, List<Point>>[] lRGGroupByDec = new Map[2];
            for (int i = 0; i < 2; i++) {
                uDGroupByDec[i] = upDown[i].stream().collect(Collectors.groupingBy(x -> x.y + x.x));
                lRGGroupByDec[i] = leftRight[i].stream().collect(Collectors.groupingBy(x -> x.y + x.x));
            }

            optimize(uDGroupByInc[0], lRGGroupByInc[0]);
            optimize(lRGGroupByInc[1], uDGroupByInc[1]);

            optimize(uDGroupByDec[1], lRGGroupByDec[0]);
            optimize(lRGGroupByDec[1], uDGroupByDec[0]);

            if (ans == 1e18) {
                out.println("SAFE");
                return;
            }

            out.println(ans);
        }

        public Map<Integer, List<Integer>> cast(List<Point> a, Function<Point, Integer> key,
                                                Function<Point, Integer> value) {
            Map<Integer, List<Integer>> map = new HashMap<>(a.size());
            for (Point pt : a) {
                map.computeIfAbsent(key.apply(pt), x -> new ArrayList<>())
                        .add(value.apply(pt));
            }
            return map;
        }

        public void optimizeHorizontal(Map<Integer, List<Integer>> a, Map<Integer, List<Integer>> b) {
            for (Integer key : a.keySet()) {
                List<Integer> l1 = a.get(key);
                List<Integer> l2 = b.getOrDefault(key, Collections.emptyList());
                l1.sort(Comparator.naturalOrder());
                l2.sort(Comparator.naturalOrder());

                int l2Idx = 0;
                for (int x : l1) {
                    while (l2Idx < l2.size() && l2.get(l2Idx) < x) {
                        l2Idx++;
                    }
                    if (l2Idx >= l2.size()) {
                        break;
                    }
                    ans = Math.min(ans, (l2.get(l2Idx) - x) / 2);
                }
            }
        }

        public void optimize(Map<Integer, List<Point>> a, Map<Integer, List<Point>> b) {
            for (Integer key : a.keySet()) {
                List<Point> l1 = a.get(key);
                List<Point> l2 = b.getOrDefault(key, Collections.emptyList());

                int l2Idx = 0;
                for (Point pt : l1) {
                    while (l2Idx < l2.size() && l2.get(l2Idx).x < pt.x) {
                        l2Idx++;
                    }
                    if (l2Idx >= l2.size()) {
                        break;
                    }
                    ans = Math.min(ans, l2.get(l2Idx).x - pt.x);
                }
            }
        }

    }

    static class Point {
        int x;
        int y;

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

        public FastOutput append(long c) {
            cache.append(c);
            return this;
        }

        public FastOutput append(String c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(String c) {
            return append(c).println();
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

        public char readChar() {
            skipBlank();
            char c = (char) next;
            next = read();
            return c;
        }

    }
}

