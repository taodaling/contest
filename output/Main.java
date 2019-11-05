import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.InputStream;

/**
 * Built using CHelper plug-in Actual solution is at the top
 * 
 * @author daltao
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "daltao", 1 << 27);
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
            TaskE solver = new TaskE();
            solver.solve(1, in, out);
            out.close();
        }
    }
    static class TaskE {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int h = in.readInt();
            int w = in.readInt();

            List<Point> pts = new ArrayList<>(h * w);
            int[][] rowData = new int[1801][1801];
            int[][] colData = new int[1801][1801];
            for (int i = 1; i <= h; i++) {
                for (int j = 1; j <= w; j++) {
                    if (in.readChar() == '#') {
                        Point pt = new Point();
                        pt.x = i + j + 600;
                        pt.y = i - j + w + 600;
                        pts.add(pt);
                        rowData[pt.y][pt.x] = 1;
                        colData[pt.x][pt.y] = 1;
                    }
                }
            }


            PreSum[] rows = new PreSum[1801];
            PreSum[] cols = new PreSum[1801];
            for (int i = 1; i <= 1800; i++) {
                rows[i] = new PreSum(rowData[i]);
                cols[i] = new PreSum(colData[i]);
            }


            long ans = 0;

            int[][] rowDp = new int[1801][1801];
            int[][] colDp = new int[1801][1801];
            for (int side = 2; side <= h + w; side += 2) {
                int len = 1 + side * 2;
                // rowDp
                for (int i = 1 + 600; i <= h + w + 600; i++) {
                    for (int j = 600; j <= 1800; j++) {
                        rowDp[i][j] = rowDp[i][j - 1];
                        if (j - len > 0 && rowData[i][j - len] == 1 && rowData[i][j - len + side] == 1) {
                            rowDp[i][j]--;
                        }
                        if (j - side > 0 && rowData[i][j - side] == 1 && rowData[i][j] == 1) {
                            rowDp[i][j]++;
                        }
                    }
                }
                // colDp
                for (int j = 1 + 600; j <= h + w + 600; j++) {
                    for (int i = 600; i <= 1800; i++) {
                        colDp[i][j] = colDp[i - 1][j];
                        if (i - len > 0 && rowData[i - len][j] == 1 && rowData[i - len + side][j] == 1) {
                            colDp[i][j]--;
                        }
                        if (i - side > 0 && rowData[i - side][j] == 1 && rowData[i][j] == 1) {
                            colDp[i][j]++;
                        }
                    }
                }

                for (Point pt : pts) {
                    int l = pt.x - side;
                    int r = pt.x + side;
                    int b = pt.y - side;
                    int t = pt.y + side;

                    long contri = 0;
                    contri += rowDp[t][r];
                    contri += rowDp[b][r];
                    contri += colDp[t][l];
                    contri += colDp[t][r];

                    contri += rowData[t][pt.x] * (cols[l].intervalSum(pt.y, t - 1) + cols[r].intervalSum(pt.y, t - 1));
                    contri += rowData[b][pt.x] * (cols[l].intervalSum(b + 1, pt.y) + cols[r].intervalSum(b + 1, pt.y));
                    contri += rowData[pt.y][l]
                                    * (rows[b].intervalSum(l + 1, pt.x - 1) + rows[t].intervalSum(l + 1, pt.x - 1));
                    contri += rowData[pt.y][r]
                                    * (rows[b].intervalSum(pt.x + 1, r - 1) + rows[t].intervalSum(pt.x + 1, r - 1));

                    ans += contri;
                }
            }

            out.println(ans / 3);
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
    static class PreSum {
        private long[] pre;

        public PreSum(long[] a) {
            int n = a.length;
            pre = new long[n];
            pre[0] = a[0];
            for (int i = 1; i < n; i++) {
                pre[i] = pre[i - 1] + a[i];
            }
        }

        public PreSum(int[] a) {
            int n = a.length;
            pre = new long[n];
            pre[0] = a[0];
            for (int i = 1; i < n; i++) {
                pre[i] = pre[i - 1] + a[i];
            }
        }

        public long intervalSum(int l, int r) {
            if (l == 0) {
                return pre[r];
            }
            return pre[r] - pre[l - 1];
        }

    }
    static class Point {
        int x;
        int y;

        public String toString() {
            return String.format("(%d,%d)", x, y);
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

