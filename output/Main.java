import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
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
            FDFS solver = new FDFS();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class FDFS {
        int order;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int q = in.readInt();
            LongHashSet set = new LongHashSet(q, false);

            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
            }

            for (int i = 0; i < n - 1; i++) {
                Node a = nodes[in.readInt() - 1];
                Node b = nodes[in.readInt() - 1];
                a.next.add(b);
                b.next.add(a);
            }

            dfs(nodes[0], null);
            Segment seg = new Segment(1, n);
            for (int i = 0; i < q; i++) {
                Node a = nodes[in.readInt() - 1];
                Node b = nodes[in.readInt() - 1];
                if (a.l > b.l) {
                    Node tmp = a;
                    a = b;
                    b = tmp;
                }

                long key = DigitUtils.asLong(Math.min(a.id, b.id), Math.max(a.id, b.id));
                if (set.contain(key)) {
                    set.remove(key);
                    seg.update(b.l, b.r, 1, n, -1);
                    if (a.l <= b.l && a.r >= b.r) {
                        //ancestor
                        seg.update(1, n, 1, n, -1);
                        seg.update(a.l + 1, a.r, 1, n, 1);
                    } else {
                        seg.update(a.l, a.r, 1, n, -1);
                    }
                } else {
                    set.add(key);
                    seg.update(b.l, b.r, 1, n, 1);
                    if (a.l <= b.l && a.r >= b.r) {
                        //ancestor
                        seg.update(1, n, 1, n, 1);
                        seg.update(a.l + 1, a.r, 1, n, -1);
                    } else {
                        seg.update(a.l, a.r, 1, n, 1);
                    }
                }

                int ans = seg.max == set.size() ? seg.cnt : 0;
                out.println(ans);
            }
        }

        public void dfs(Node root, Node p) {
            root.l = ++order;
            for (Node node : root.next) {
                if (node == p) {
                    continue;
                }
                dfs(node, root);
            }
            root.r = order;
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

        public FastOutput println(int c) {
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

    static class LongHashSet {
        private int[] slot;
        private int[] next;
        private long[] keys;
        private int alloc;
        private boolean[] removed;
        private int mask;
        private int size;
        private boolean rehash;
        private Hasher hasher = new Hasher();

        public LongHashSet(int cap, boolean rehash) {
            this.mask = (1 << (32 - Integer.numberOfLeadingZeros(cap - 1))) - 1;
            this.rehash = rehash;
            slot = new int[mask + 1];
            next = new int[cap + 1];
            keys = new long[cap + 1];
            removed = new boolean[cap + 1];
        }

        private void doubleCapacity() {
            int newSize = Math.max(next.length + 10, next.length * 2);
            next = Arrays.copyOf(next, newSize);
            keys = Arrays.copyOf(keys, newSize);
            removed = Arrays.copyOf(removed, newSize);
        }

        public void alloc() {
            alloc++;
            if (alloc >= next.length) {
                doubleCapacity();
            }
            next[alloc] = 0;
            removed[alloc] = false;
            size++;
        }

        private int hash(long x) {
            return hasher.hash(x);
        }

        public void add(long x) {
            int h = hash(x);
            int s = h & mask;
            if (slot[s] == 0) {
                alloc();
                slot[s] = alloc;
                keys[alloc] = x;
                return;
            } else {
                int index = findIndexOrLastEntry(s, x);
                if (keys[index] != x) {
                    alloc();
                    next[index] = alloc;
                    keys[alloc] = x;
                }
            }
            if (rehash && size >= slot.length) {
                rehash();
            }
        }

        private void rehash() {
            int[] newSlots = new int[Math.max(16, slot.length * 2)];
            int newMask = newSlots.length - 1;
            for (int i = 0; i < slot.length; i++) {
                if (slot[i] == 0) {
                    continue;
                }
                int head = slot[i];
                while (head != 0) {
                    int n = next[head];
                    int s = hash(keys[head]) & newMask;
                    next[head] = newSlots[s];
                    newSlots[s] = head;
                    head = n;
                }
            }
            this.slot = newSlots;
            this.mask = newMask;
        }

        public boolean contain(long x) {
            int h = hash(x);
            int s = h & mask;
            if (slot[s] == 0) {
                return false;
            }
            return keys[findIndexOrLastEntry(s, x)] == x;
        }

        public void remove(long x) {
            int h = hash(x);
            int s = h & mask;
            if (slot[s] == 0) {
                return;
            }
            int pre = 0;
            int index = slot[s];
            while (keys[index] != x) {
                pre = index;
                if (next[index] != 0) {
                    index = next[index];
                } else {
                    break;
                }
            }
            if (keys[index] != x) {
                return;
            }
            if (slot[s] == index) {
                slot[s] = next[index];
            } else {
                next[pre] = next[index];
            }
            removed[index] = true;
            size--;
        }

        private int findIndexOrLastEntry(int s, long x) {
            int iter = slot[s];
            while (keys[iter] != x) {
                if (next[iter] != 0) {
                    iter = next[iter];
                } else {
                    return iter;
                }
            }
            return iter;
        }

        public LongIterator iterator() {
            return new LongIterator() {
                int index = 1;


                public boolean hasNext() {
                    while (index <= alloc && removed[index]) {
                        index++;
                    }
                    return index <= alloc;
                }


                public long next() {
                    if (!hasNext()) {
                        throw new IllegalStateException();
                    }
                    return keys[index++];
                }
            };
        }

        public int size() {
            return size;
        }

        public String toString() {
            LongIterator iterator = iterator();
            StringBuilder builder = new StringBuilder("{");
            while (iterator.hasNext()) {
                builder.append(iterator.next()).append(',');
            }
            if (builder.charAt(builder.length() - 1) == ',') {
                builder.setLength(builder.length() - 1);
            }
            builder.append('}');
            return builder.toString();
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

    static class Segment implements Cloneable {
        private Segment left;
        private Segment right;
        int max;
        int cnt;
        private int dirty;

        private void modify(int x) {
            dirty += x;
            max += x;
        }

        public void pushUp() {
            max = Math.max(left.max, right.max);
            cnt = 0;
            if (max == left.max) {
                cnt += left.cnt;
            }
            if (max == right.max) {
                cnt += right.cnt;
            }
        }

        public void pushDown() {
            if (dirty != 0) {
                left.modify(dirty);
                right.modify(dirty);
                dirty = 0;
            }
        }

        public Segment(int l, int r) {
            if (l < r) {
                int m = (l + r) >> 1;
                left = new Segment(l, m);
                right = new Segment(m + 1, r);
                pushUp();
            } else {
                cnt = 1;
            }
        }

        private boolean covered(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean noIntersection(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void update(int ll, int rr, int l, int r, int x) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                modify(x);
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.update(ll, rr, l, m, x);
            right.update(ll, rr, m + 1, r, x);
            pushUp();
        }

        private Segment deepClone() {
            Segment seg = clone();
            if (seg.left != null) {
                seg.left = seg.left.deepClone();
            }
            if (seg.right != null) {
                seg.right = seg.right.deepClone();
            }
            return seg;
        }

        protected Segment clone() {
            try {
                return (Segment) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        private void toString(StringBuilder builder) {
            if (left == null && right == null) {
                builder.append("val").append(",");
                return;
            }
            pushDown();
            left.toString(builder);
            right.toString(builder);
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            deepClone().toString(builder);
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }
            return builder.toString();
        }

    }

    static class DigitUtils {
        private static long LONG_TO_INT_MASK = (1L << 32) - 1;

        private DigitUtils() {
        }

        public static long asLong(int high, int low) {
            return (((long) high) << 32) | (((long) low) & LONG_TO_INT_MASK);
        }

    }

    static class Hasher {
        private long time = System.nanoTime() + System.currentTimeMillis();

        private int shuffle(long x) {
            x += time;
            x += 0x9e3779b97f4a7c15L;
            x = (x ^ (x >>> 30)) * 0xbf58476d1ce4e5b9L;
            x = (x ^ (x >>> 27)) * 0x94d049bb133111ebL;
            return (int) (x ^ (x >>> 31));
        }

        public int hash(long x) {
            return shuffle(x);
        }

    }

    static interface LongIterator {
        boolean hasNext();

        long next();

    }

    static class Node {
        int id;
        int l;
        int r;
        List<Node> next = new ArrayList<>();

    }
}

