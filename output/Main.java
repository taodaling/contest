import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
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
        new TaskAdapter().run();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            P1559 solver = new P1559();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class P1559 {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();

            int[][] boyToGirl = new int[n][n];
            int[][] girlToBoy = new int[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    boyToGirl[i][j] = in.readInt();
                }
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    girlToBoy[i][j] = in.readInt();
                }
            }

            long[][] mat = new long[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    mat[i][j] = (long) boyToGirl[i][j] * girlToBoy[j][i];
                }
            }
            KMAlgo km = new KMAlgo(mat);
            long ans = km.solve();
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

    }

    static class KMAlgo {
        private static final long INF = (long) 2e18;
        private long[][] table = null;
        private long[] xl = null;
        private long[] yl = null;
        private int[] xMatch = null;
        private int[] yMatch = null;
        private int n = 0;

        public KMAlgo(long[][] table) {
            this.table = table;
            this.n = table.length;
            this.xl = new long[n];
            this.yl = new long[n];
            Arrays.fill(xl, -INF);
            for (int x = 0; x < n; x++) {
                for (int y = 0; y < n; y++) {
                    if (table[x][y] > xl[x]) {
                        xl[x] = table[x][y];
                    }
                }
            }
            this.xMatch = new int[n];
            this.yMatch = new int[n];
            Arrays.fill(xMatch, -1);
            Arrays.fill(yMatch, -1);
        }

        public long solve() { // 入口，输入权重矩阵
            for (int x = 0; x < n; x++) {
                bfs(x);
            }
            long value = 0;
            for (int x = 0; x < n; x++) {
                value += table[x][xMatch[x]];
            }
            return value;
        }

        private void bfs(int startX) {    // 为一个x点寻找匹配
            boolean find = false;
            int endY = -1;
            int[] yPre = new int[n];      // 标识搜索路径上y点的前一个点
            boolean[] S = new boolean[n], T = new boolean[n]; // S集合，T集合
            long[] slackY = new long[n];    // Y点的松弛变量
            Arrays.fill(yPre, -1);
            Arrays.fill(slackY, INF);
            int[] queue = new int[n];     // 队列
            int qs = 0, qe = 0;           // 队列开始结束索引
            queue[qe++] = startX;
            while (!find) {       // 循环直到找到匹配
                while (qs < qe && !find) {   // 队列不为空
                    int x = queue[qs++];
                    S[x] = true;
                    for (int y = 0; y < n; y++) {
                        if (T[y]) {
                            continue;
                        }
                        long tmp = xl[x] + yl[y] - table[x][y];
                        if (tmp == 0) {  // 相等子树中的边
                            T[y] = true;
                            yPre[y] = x;
                            if (yMatch[y] == -1) {
                                endY = y;
                                find = true;
                                break;
                            } else {
                                queue[qe++] = yMatch[y];
                            }
                        } else if (slackY[y] > tmp) { // 不在相等子树中的边，看是否能够更新松弛变量
                            slackY[y] = tmp;
                            yPre[y] = x;
                        }
                    }
                }
                if (find) {
                    break;
                }
                long a = INF;
                for (int y = 0; y < n; y++) {  // 找到最小的松弛值
                    if (!T[y]) {
                        a = Math.min(a, slackY[y]);
                    }
                }
                for (int i = 0; i < n; i++) {  // 根据a修改标号值
                    if (S[i]) {
                        xl[i] -= a;
                    }
                    if (T[i]) {
                        yl[i] += a;
                    }
                }
                qs = qe = 0;
                for (int y = 0; y < n; y++) {        // 重要！！！控制修改标号之后需要检查的x点
                    if (!T[y] && slackY[y] == a) {   // 查看那些y点新加入到T集合，注意，这些y点的前向x点都记录在了yPre里面，所以这些x点不用再次入队
                        T[y] = true;
                        if (yMatch[y] == -1) {       // 新加入的y点没有匹配，那么就找到可扩路了
                            endY = y;
                            find = true;
                            break;
                        } else {   // 新加入的y点已经有匹配了，将它匹配的x加到队列
                            queue[qe++] = yMatch[y];
                        }
                    }
                    slackY[y] -= a;   // 所有松弛值减去a。(对于T集合中的松弛值已经没用了，对于不在T集合里面的y点，
                }                     // 它们的松弛值是通过S集合中的x点求出的，S集合中的x点的标号值在上面都减去了a，所以这里松弛值也要减去a)
            }
            while (endY != -1) {    // 找到可扩路最后的y点后，回溯并扩充
                int preX = yPre[endY], preY = xMatch[preX];
                xMatch[preX] = endY;
                yMatch[endY] = preX;
                endY = preY;
            }
        }

    }
}

