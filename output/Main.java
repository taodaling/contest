import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Deque;
import java.util.ArrayDeque;
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
            PrintWriter out = new PrintWriter(outputStream);
            TaskB solver = new TaskB();
            solver.solve(1, in, out);
            out.close();
        }
    }
    static class TaskB {
        public void solve(int testNumber, FastInput in, PrintWriter out) {
            int n = in.readInt();
            int[] perm = new int[n + 1];
            int[] prev = new int[n + 1];
            int[] next = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                perm[i] = in.readInt();
            }
            Deque<Integer> decQueue = new ArrayDeque<>(n);
            Deque<Integer> incQueue = new ArrayDeque<>(n);
            for (int i = 1; i <= n; i++) {
                while (decQueue.size() > 0 && perm[decQueue.peekLast()] > perm[i]) {
                    decQueue.removeLast();
                }
                if (decQueue.size() == 0) {
                    prev[i] = 1;
                } else {
                    prev[i] = decQueue.peekLast() + 1;
                }
                decQueue.addLast(i);
            }
            for (int i = n; i >= 1; i--) {
                while (incQueue.size() > 0 && perm[incQueue.peekFirst()] >= perm[i]) {
                    incQueue.removeFirst();
                }
                if (incQueue.size() == 0) {
                    next[i] = n;
                } else {
                    next[i] = incQueue.peekFirst() - 1;
                }
                incQueue.addFirst(i);
            }

            long ans = 0;
            for (int i = 1; i <= n; i++) {
                int l = i - prev[i] + 1;
                int r = next[i] + 1 - i;
                ans += (long) l * r * perm[i];
            }

            out.println(ans);
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
                    throw new RuntimeException(e);
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
}

