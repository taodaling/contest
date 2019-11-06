import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.io.UncheckedIOException;
import java.util.Map;
import java.io.Closeable;
import java.util.Map.Entry;
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
            TaskC solver = new TaskC();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskC {
        SubsetGenerator sg = new SubsetGenerator();
        Node[] nodes;
        int n;
        Map<Long, Node> map;
        long notExist;
        long[] mask2Key;
        Map<Long, LongList> sequence;
        DigitUtils.BitOperator bo = new DigitUtils.BitOperator();
        boolean[] dp;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            n = in.readInt();
            nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
            }

            map = new LinkedHashMap<>(200000);
            for (int i = 0; i < n; i++) {
                int k = in.readInt();
                for (int j = 0; j < k; j++) {
                    long x = in.readInt();
                    map.put(x, nodes[i]);
                    nodes[i].sum += x;
                }
            }

            long total = 0;
            for (Node node : nodes) {
                total += node.sum;
            }

            if (total % n != 0) {
                out.println("No");
                return;
            }
            long avg = total / n;

            notExist = (long) 1e18;
            mask2Key = new long[1 << n];
            Arrays.fill(mask2Key, notExist);
            sequence = new HashMap<>(200000);


            for (Map.Entry<Long, Node> kv : map.entrySet()) {
                for (Node node : nodes) {
                    node.handled = false;
                }

                long key = kv.getKey();

                Node node = kv.getValue();
                node.handled = true;
                int mask = bo.setBit(0, node.id, true);

                LongList list = new LongList(15);
                list.add(key);

                long req = avg - (node.sum - key);
                boolean valid = true;

                while (true) {
                    if (req == key) {
                        break;
                    }
                    Node next = map.get(req);
                    if (next == null || next.handled) {
                        valid = false;
                        break;
                    }
                    next.handled = true;
                    list.add(req);
                    req = avg - (next.sum - req);
                    mask = bo.setBit(mask, next.id, true);
                }

                if (!valid) {
                    continue;
                }

                mask2Key[mask] = key;
                sequence.put(key, list);
            }


            dp = new boolean[1 << n];
            for (int i = 0; i < (1 << n); i++) {
                dp[i] = mask2Key[i] != notExist;
                sg.setSet(i);
                while (!dp[i] && sg.hasNext()) {
                    int next = sg.next();
                    if (next == 0 || next == i) {
                        continue;
                    }
                    dp[i] = dp[i] || (dp[next] && dp[i - next]);
                }
            }

            if (!dp[(1 << n) - 1]) {
                out.println("No");
                return;
            }

            populate((1 << n) - 1);
            out.println("Yes");
            for (Node node : nodes) {
                out.append(node.out).append(' ').append(node.to + 1).append('\n');
            }
        }

        public void populate(int mask) {
            if (mask2Key[mask] != notExist) {
                LongList list = sequence.get(mask2Key[mask]);
                int m = list.size();
                for (int i = 0; i < m; i++) {
                    long v = list.get(i);
                    long last = list.get(DigitUtils.mod(i - 1, m));
                    Node which = map.get(v);
                    Node to = map.get(last);

                    which.out = v;
                    which.to = to.id;
                }
                return;
            }

            sg.setSet(mask);
            while (sg.hasNext()) {
                int next = sg.next();
                if (next == 0 || next == mask) {
                    continue;
                }
                if (dp[next] && dp[mask - next]) {
                    populate(next);
                    populate(mask - next);
                    return;
                }
            }
        }

    }

    static class LongList {
        private int size;
        private int cap;
        private long[] data;
        private static final long[] EMPTY = new long[0];

        public LongList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new long[cap];
            }
        }

        public LongList(LongList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public LongList() {
            this(0);
        }

        private void ensureSpace(int need) {
            int req = size + need;
            if (req > cap) {
                while (cap < req) {
                    cap = Math.max(cap + 10, 2 * cap);
                }
                data = Arrays.copyOf(data, cap);
            }
        }

        private void checkRange(int i) {
            if (i < 0 || i >= size) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }

        public long get(int i) {
            checkRange(i);
            return data[i];
        }

        public void add(long x) {
            ensureSpace(1);
            data[size++] = x;
        }

        public int size() {
            return size;
        }

        public String toString() {
            return Arrays.toString(Arrays.copyOf(data, size));
        }

    }

    static class SubsetGenerator {
        private int[] meanings = new int[33];
        private int[] bits = new int[33];
        private int remain;
        private int next;

        public void setSet(int set) {
            int bitCount = 0;
            while (set != 0) {
                meanings[bitCount] = set & -set;
                bits[bitCount] = 0;
                set -= meanings[bitCount];
                bitCount++;
            }
            remain = 1 << bitCount;
            next = 0;
        }

        public boolean hasNext() {
            return remain > 0;
        }

        private void consume() {
            remain = remain - 1;
            int i;
            for (i = 0; bits[i] == 1; i++) {
                bits[i] = 0;
                next -= meanings[i];
            }
            bits[i] = 1;
            next += meanings[i];
        }

        public int next() {
            int returned = next;
            consume();
            return returned;
        }

    }

    static class Node {
        int id;
        long sum;
        boolean handled;
        long out;
        long to;

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

    static class FastOutput implements AutoCloseable, Closeable {
        private StringBuilder cache = new StringBuilder(10 << 20);
        private final Writer os;

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

        public FastOutput println(String c) {
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

    static class DigitUtils {
        private static final long[] DIGIT_VALUES = new long[19];

        static {
            DIGIT_VALUES[0] = 1;
            for (int i = 1; i < 19; i++) {
                DIGIT_VALUES[i] = DIGIT_VALUES[i - 1] * 10;
            }
        }

        private DigitUtils() {
        }

        public static int mod(int x, int mod) {
            x %= mod;
            if (x < 0) {
                x += mod;
            }
            return x;
        }

        public static class BitOperator {
            public int setBit(int x, int i, boolean v) {
                if (v) {
                    x |= 1 << i;
                } else {
                    x &= ~(1 << i);
                }
                return x;
            }

        }

    }
}

