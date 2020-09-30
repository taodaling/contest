import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.IOException;
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
            PrintWriter out = new PrintWriter(outputStream);
            PersistentQueue solver = new PersistentQueue();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class PersistentQueue {
        PersistentArray<Integer>[] nexts;
        PersistentArray<Integer> curNext;
        int headIdx = 0;
        int tailIdx = 1;

        public void solve(int testNumber, FastInput in, PrintWriter out) {
            int q = in.readInt();
            nexts = new PersistentArray[q + 1];
            nexts[0] = new PersistentArray<>(q + 10);
            int[] data = new int[q + 10];

            for (int i = 1; i <= q; i++) {
                int t = in.readInt();
                int v = in.readInt() + 1;
                curNext = nexts[v];

                if (t == 0) {
                    int x = in.readInt();
                    data[i + 2] = x;
                    Integer tail = curNext.get(tailIdx);
                    if (tail != null) {
                        curNext = curNext.set(tail, i + 2);
                        curNext = curNext.set(tailIdx, i + 2);
                    } else {
                        curNext = curNext.fill(0, 1, i + 2);
                    }
                } else {
                    Integer head = curNext.get(headIdx);
                    out.println(head == null ? -1 : data[head]);
                    if (head != null) {
                        Integer next = curNext.get(head);
                        curNext = curNext.set(headIdx, next);
                        if (next == null) {
                            curNext = curNext.set(tailIdx, null);
                        }
                    }

                }

                nexts[i] = curNext;
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

    static class PersistentArray<T> {
        private PersistentArray.NoTagPersistentSegment<T> root;
        private int n;

        public PersistentArray(int n) {
            root = PersistentArray.NoTagPersistentSegment.NIL;
            this.n = n;
        }

        public T get(int x) {
            return root.query(x, x, 0, n - 1);
        }

        public PersistentArray<T> set(int x, T val) {
            PersistentArray<T> ans = new PersistentArray<>(n);
            ans.root = root.clone();
            ans.root.update(x, x, 0, n - 1, val);
            return ans;
        }

        public PersistentArray<T> fill(int l, int r, T val) {
            PersistentArray<T> ans = new PersistentArray<>(n);
            ans.root = root.clone();
            ans.root.update(l, r, 0, n - 1, val);
            return ans;
        }

        public String toString() {
            StringBuilder ans = new StringBuilder("[");
            for (int i = 0; i < n; i++) {
                ans.append(get(i)).append(',');
            }
            if (ans.length() > 1) {
                ans.setLength(ans.length() - 1);
            }
            ans.append("]");
            return ans.toString();
        }

        private static class NoTagPersistentSegment<T> implements Cloneable {
            public static final PersistentArray.NoTagPersistentSegment NIL = new PersistentArray.NoTagPersistentSegment();
            private PersistentArray.NoTagPersistentSegment<T> left;
            private PersistentArray.NoTagPersistentSegment<T> right;
            private T val;

            static {
                NIL.left = NIL.right = NIL;
            }

            public void pushUp() {
            }

            public NoTagPersistentSegment() {
                left = right = NIL;
            }

            private boolean covered(int ll, int rr, int l, int r) {
                return ll <= l && rr >= r;
            }

            private boolean noIntersection(int ll, int rr, int l, int r) {
                return ll > r || rr < l;
            }

            public void update(int ll, int rr, int l, int r, T val) {
                if (covered(ll, rr, l, r)) {
                    if (l == r) {
                        this.val = val;
                        return;
                    }
                }
                int m = DigitUtils.floorAverage(l, r);
                if (!noIntersection(ll, rr, l, m)) {
                    left = left.clone();
                    left.update(ll, rr, l, m, val);
                }
                if (!noIntersection(ll, rr, m + 1, r)) {
                    right = right.clone();
                    right.update(ll, rr, m + 1, r, val);
                }
                pushUp();
            }

            public T query(int ll, int rr, int l, int r) {
                if (this == NIL || noIntersection(ll, rr, l, r)) {
                    return null;
                }
                if (covered(ll, rr, l, r)) {
                    return val;
                }
                int m = DigitUtils.floorAverage(l, r);
                T ans = left.query(ll, rr, l, m);
                if (ans == null) {
                    ans = right.query(ll, rr, m + 1, r);
                }
                return ans;
            }

            public PersistentArray.NoTagPersistentSegment clone() {
                try {
                    return (PersistentArray.NoTagPersistentSegment) super.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }

    static class DigitUtils {
        private DigitUtils() {
        }

        public static int floorAverage(int x, int y) {
            return (x & y) + ((x ^ y) >> 1);
        }

    }
}

