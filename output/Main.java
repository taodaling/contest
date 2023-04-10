import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Deque;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Map;
import java.io.OutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.util.ArrayDeque;
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
            Task solver = new Task();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class Task {
        UndoDSU dsu;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int N = in.ri();
            int M = in.ri();
            int[][] ops = new int[M][];
            PriorityCommutativeUndoOperation[] undoOperations = new PriorityCommutativeUndoOperation[M];
            dsu = new UndoDSU(N + 1);
            dsu.init();
            UndoPriorityQueue pq = new UndoPriorityQueue(M);
            for (int i = 0; i < M; i++) {
                ops[i] = in.ri(3);
            }
            Map<Long, Edge> E = new HashMap<>(M);
            for (int i = 0; i < M; i++) {
                int[] op = ops[i];
                if (op[0] == 2) {
                    continue;
                }
                if (op[0] == 0) {
                    Edge e = new Edge();
                    e.a = op[1];
                    e.b = op[2];
                    e.addTime = i;
                    E.put(idOfEdge(e.a, e.b), e);
                } else {
                    Edge e = E.remove(idOfEdge(op[1], op[2]));
                    e.delTime = i;
                    undoOperations[e.addTime] = asOp(e);
                }
            }
            int delTimeEnum = M;
            for (Edge e : E.values()) {
                e.delTime = delTimeEnum++;
                undoOperations[e.addTime] = asOp(e);
            }
            for (int i = 0; i < M; i++) {
                int[] op = ops[i];
                if (op[0] == 2) {
                    //answer query
                    if (dsu.find(op[1]) == dsu.find(op[2])) {
                        out.println('Y');
                    } else {
                        out.println('N');
                    }
                } else if (op[0] == 0) {
                    pq.push(undoOperations[i]);
                } else if (op[0] == 1) {
                    pq.pop();
                }
            }

        }

        PriorityCommutativeUndoOperation asOp(Edge e) {
            PriorityCommutativeUndoOperation op = new PriorityCommutativeUndoOperation(e.delTime,
                    dsu.merge(e.a, e.b));
            return op;
        }

        long idOfEdge(int a, int b) {
            if (a > b) {
                return idOfEdge(b, a);
            }
            return DigitUtils.asLong(a, b);
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

        public void populate(int[] data) {
            for (int i = 0; i < data.length; i++) {
                data[i] = readInt();
            }
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

        public int[] ri(int n) {
            int[] ans = new int[n];
            populate(ans);
            return ans;
        }

        public int readInt() {
            boolean rev = false;

            skipBlank();
            if (next == '+' || next == '-') {
                rev = next == '-';
                next = read();
            }

            int val = 0;
            while (next >= '0' && next <= '9') {
                val = val * 10 - next + '0';
                next = read();
            }

            return rev ? val : -val;
        }

    }

    static class Edge {
        int addTime;
        int delTime;
        int a;
        int b;

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private static final int THRESHOLD = 32 << 10;
        private OutputStream writer;
        private StringBuilder cache = new StringBuilder(THRESHOLD * 2);
        private static Field stringBuilderValueField;
        private char[] charBuf = new char[THRESHOLD * 2];
        private byte[] byteBuf = new byte[THRESHOLD * 2];

        static {
            try {
                stringBuilderValueField = StringBuilder.class.getSuperclass().getDeclaredField("value");
                stringBuilderValueField.setAccessible(true);
            } catch (Exception e) {
                stringBuilderValueField = null;
            }
            stringBuilderValueField = null;
        }

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

        public FastOutput(OutputStream writer) {
            this.writer = writer;
        }

        public FastOutput append(char c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput println(char c) {
            return append(c).println();
        }

        public FastOutput println() {
            return append('\n');
        }

        public FastOutput flush() {
            try {
                if (stringBuilderValueField != null) {
                    try {
                        byte[] value = (byte[]) stringBuilderValueField.get(cache);
                        writer.write(value, 0, cache.length());
                    } catch (Exception e) {
                        stringBuilderValueField = null;
                    }
                }
                if (stringBuilderValueField == null) {
                    int n = cache.length();
                    if (n > byteBuf.length) {
                        //slow
                        writer.write(cache.toString().getBytes(StandardCharsets.UTF_8));
//                writer.append(cache);
                    } else {
                        cache.getChars(0, n, charBuf, 0);
                        for (int i = 0; i < n; i++) {
                            byteBuf[i] = (byte) charBuf[i];
                        }
                        writer.write(byteBuf, 0, n);
                    }
                }
                writer.flush();
                cache.setLength(0);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return this;
        }

        public void close() {
            flush();
            try {
                writer.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public String toString() {
            return cache.toString();
        }

    }

    static class UndoPriorityQueue {
        TreeSet<PriorityCommutativeUndoOperation> set = new TreeSet<>(Comparator.comparingLong(x -> x.priority));
        UndoStack stack;
        List<PriorityCommutativeUndoOperation> bufferForLowPriority;
        List<PriorityCommutativeUndoOperation> bufferForHighPriority;

        public UndoPriorityQueue(int cap) {
            stack = new UndoStack(cap);
            bufferForLowPriority = new ArrayList<>(cap);
            bufferForHighPriority = new ArrayList<>(cap);
        }

        public void push(PriorityCommutativeUndoOperation op) {
            if (!set.add(op)) {
                throw new IllegalArgumentException("Duplicate priority");
            }
            pushStack(op);
        }

        private void pushStack(PriorityCommutativeUndoOperation op) {
            op.offsetToBottom = stack.size();
            stack.push(op);
        }

        public PriorityCommutativeUndoOperation pop() {
            int k = 0;
            int size = size();
            bufferForLowPriority.clear();
            for (PriorityCommutativeUndoOperation op : set) {
                bufferForLowPriority.add(op);
                k = Math.max(k, size - op.offsetToBottom);
                op.offsetToBottom = -1;
                if (bufferForLowPriority.size() * 2 >= k) {
                    break;
                }
            }
            if (k > 1) {
                bufferForHighPriority.clear();
                for (int i = 0; i < k; i++) {
                    PriorityCommutativeUndoOperation op = (PriorityCommutativeUndoOperation) stack.pop();
                    if (op.offsetToBottom != -1) {
                        bufferForHighPriority.add(op);
                    }
                }
                for (PriorityCommutativeUndoOperation op : bufferForHighPriority) {
                    pushStack(op);
                }
                Collections.reverse(bufferForLowPriority);
                for (PriorityCommutativeUndoOperation op : bufferForLowPriority) {
                    pushStack(op);
                }
            }
            PriorityCommutativeUndoOperation ans = set.pollFirst();
            stack.pop();
            return ans;
        }

        public int size() {
            return stack.size();
        }

    }

    static class DigitUtils {
        public static long INT_TO_LONG_MASK = (1L << 32) - 1;

        private DigitUtils() {
        }

        public static long asLong(int high, int low) {
            return (((long) high) << 32) | (((long) low) & INT_TO_LONG_MASK);
        }

    }

    static class UndoDSU {
        int[] rank;
        int[] p;

        public UndoDSU(int n) {
            rank = new int[n];
            p = new int[n];
        }

        public void init() {
            Arrays.fill(rank, 1);
            Arrays.fill(p, -1);
        }

        public int find(int x) {
            while (p[x] != -1) {
                x = p[x];
            }
            return x;
        }

        public UndoOperation merge(int a, int b) {
            return new UndoOperation() {
                int x, y;


                public void apply() {
                    x = find(a);
                    y = find(b);
                    if (x == y) {
                        return;
                    }
                    if (rank[x] < rank[y]) {
                        int tmp = x;
                        x = y;
                        y = tmp;
                    }
                    p[y] = x;
                    rank[x] += rank[y];
                }


                public void undo() {
                    int cur = y;
                    while (p[cur] != -1) {
                        cur = p[cur];
                        rank[cur] -= rank[y];
                    }
                    p[y] = -1;
                }
            };
        }

    }

    static class PriorityCommutativeUndoOperation implements UndoOperation {
        public long priority;
        int offsetToBottom;
        UndoOperation op;

        public PriorityCommutativeUndoOperation(long priority, UndoOperation op) {
            this.priority = priority;
            this.op = op;
        }

        public void apply() {
            op.apply();
        }

        public void undo() {
            op.undo();
        }

    }

    static class UndoStack {
        private Deque<UndoOperation> dq;

        public UndoStack(int size) {
            dq = new ArrayDeque<>(size);
        }

        public void push(UndoOperation op) {
            dq.addLast(op);
            op.apply();
        }

        public UndoOperation pop() {
            UndoOperation ans = dq.removeLast();
            ans.undo();
            return ans;
        }

        public int size() {
            return dq.size();
        }

    }

    static interface UndoOperation {
        void apply();

        void undo();

    }
}

