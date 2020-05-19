import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.io.Writer;
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
            DKarenAndCards solver = new DKarenAndCards();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DKarenAndCards {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int[] limits = new int[3];
            for (int i = 0; i < 3; i++) {
                limits[i] = in.readInt();
            }

            int[][] cards = new int[n][3];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < 3; j++) {
                    cards[i][j] = in.readInt();
                }
            }

            List<Rect> cur = new ArrayList<>(10);
            cur.add(new Rect(0, 0, 0, limits[0], limits[1], limits[2]));
            List<Rect> tmp = new ArrayList<>(10);
            List<Rect> mid = new ArrayList<>(10);
            List<Rect> scan = new ArrayList<>(10);
            for (int[] card : cards) {
                scan.clear();
                scan.add(new Rect(card[0], 0, card[2], limits[0], card[1], limits[2]));
                scan.add(new Rect(0, card[1], card[2], card[0], limits[1], limits[2]));
                scan.add(new Rect(card[0], card[1], 0, limits[0], limits[1], limits[2]));

                tmp.clear();
                for (Rect a : cur) {
                    mid.clear();
                    for (Rect b : scan) {
                        Rect rt = Rect.merge(a, b);
                        if (rt.isEmpty()) {
                            continue;
                        }
                        boolean find = false;
                        do {
                            find = false;
                            for (int i = 0; i < mid.size(); i++) {
                                if (Rect.canMerge(mid.get(i), rt)) {
                                    find = true;
                                    rt = Rect.merge(rt, mid.get(i));
                                    mid.remove(i);
                                    break;
                                }
                            }
                        } while (find);
                        mid.add(rt);
                    }
                    tmp.addAll(mid);
                }

                List<Rect> x = tmp;
                tmp = cur;
                cur = x;
            }

            long ans = 0;
            for (Rect r : cur) {
                ans += r.volume();
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

    static class Rect {
        int[] lb = new int[3];
        int[] rt = new int[3];

        public Rect(int x0, int y0, int z0, int x1, int y1, int z1) {
            lb[0] = x0;
            lb[1] = y0;
            lb[2] = z0;
            rt[0] = x1;
            rt[1] = y1;
            rt[2] = z1;
        }

        public Rect() {
        }

        public static boolean canMerge(Rect a, Rect b) {
            for (int i = 0; i < 3; i++) {
                boolean valid = a.lb[i] == b.rt[i] || a.rt[i] == b.lb[i];
                for (int j = 0; j < 3 && valid; j++) {
                    if (i == j) {
                        continue;
                    }
                    if (a.lb[j] != b.lb[j] || a.rt[j] != b.rt[j]) {
                        valid = false;
                    }
                }
                if (valid) {
                    return true;
                }
            }
            return false;
        }

        public static Rect merge(Rect a, Rect b) {
            Rect ans = new Rect();
            for (int i = 0; i < 3; i++) {
                ans.lb[i] = Math.max(a.lb[i], b.lb[i]);
                ans.rt[i] = Math.min(a.rt[i], b.rt[i]);
            }
            return ans;
        }

        long volume() {
            long ans = 1;
            for (int i = 0; i < 3; i++) {
                ans *= rt[i] - lb[i];
            }
            return ans;
        }

        public boolean isEmpty() {
            for (int i = 0; i < 3; i++) {
                if (lb[i] >= rt[i]) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return Arrays.toString(lb) + ":" + Arrays.toString(rt);
        }

    }
}

