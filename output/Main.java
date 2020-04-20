import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
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
            DPickingStrings solver = new DPickingStrings();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DPickingStrings {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int limit = (int) 1e5;
            char[] S = new char[limit];
            char[] T = new char[limit];

            int n = in.readString(S, 0);
            int m = in.readString(T, 0);
            int[] a = new int[n];
            int[] b = new int[m];
            int[] aLeft = new int[n];
            int[] bLeft = new int[m];
            for (int i = 0; i < n; i++) {
                a[i] = S[i] == 'A' ? 0 : 1;
                aLeft[i] = i == 0 ? -1 : aLeft[i - 1];
                if (a[i] == 1) {
                    aLeft[i] = i;
                }
            }
            for (int i = 0; i < m; i++) {
                b[i] = T[i] == 'A' ? 0 : 1;
                bLeft[i] = i == 0 ? -1 : bLeft[i - 1];
                if (b[i] == 1) {
                    bLeft[i] = i;
                }
            }

            IntegerPreSum psA = new IntegerPreSum(a);
            IntegerPreSum psB = new IntegerPreSum(b);

            int q = in.readInt();
            for (int i = 0; i < q; i++) {
                int l1 = in.readInt() - 1;
                int r1 = in.readInt() - 1;
                int l2 = in.readInt() - 1;
                int r2 = in.readInt() - 1;

                int c1 = psA.intervalSum(l1, r1);
                int c2 = psB.intervalSum(l2, r2);
                if (c2 < c1 || (c2 - c1) % 2 == 1) {
                    out.append(0);
                    continue;
                }
                int len1 = r1 - Math.max(l1 - 1, aLeft[r1]);
                int len2 = r2 - Math.max(l2 - 1, bLeft[r2]);
                if (c2 == c1) {
                    if (len2 > len1 || (len1 - len2) % 3 != 0) {
                        out.append(0);
                    } else {
                        out.append(1);
                    }
                    continue;
                }
                if (c2 > 0 && c1 == 0) {
                    len2++;
                }
                if (len2 > len1) {
                    out.append(0);
                } else {
                    out.append(1);
                }
            }
        }

    }

    static class IntegerPreSum {
        private int[] pre;
        private int n;

        public IntegerPreSum(int n) {
            pre = new int[n];
        }

        public void populate(int[] a) {
            n = a.length;
            pre[0] = a[0];
            for (int i = 1; i < n; i++) {
                pre[i] = pre[i - 1] + a[i];
            }
        }

        public IntegerPreSum(int[] a) {
            this(a.length);
            populate(a);
        }

        public int intervalSum(int l, int r) {
            return prefix(r) - prefix(l - 1);
        }

        public int prefix(int i) {
            if (i < 0) {
                return 0;
            }
            return pre[Math.min(i, n - 1)];
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

        public FastOutput append(int c) {
            cache.append(c);
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
        private byte[] buf = new byte[1 << 20];
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

        public int readString(char[] data, int offset) {
            skipBlank();

            int originalOffset = offset;
            while (next > 32) {
                data[offset++] = (char) next;
                next = read();
            }

            return offset - originalOffset;
        }

    }
}

