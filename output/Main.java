import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.TreeMap;
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
            CTournament solver = new CTournament();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class CTournament {
        TreeMap<Integer, Node>[] tops;
        TreeMap<Integer, Node>[] bots;
        int k;
        Debug debug = new Debug(true);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            k = in.readInt();

            Node min = new Node(k);
            tops = new TreeMap[k];
            bots = new TreeMap[k];
            for (int i = 0; i < k; i++) {
                tops[i] = new TreeMap<>();
                bots[i] = new TreeMap<>();
                tops[i].put(0, min);
                bots[i].put(0, min);
            }

            for (int i = 0; i < n; i++) {
                Node newer = new Node(k);
                for (int j = 0; j < k; j++) {
                    newer.bot[j] = newer.top[j] = in.readInt();
                }

                Node top = null;
                Node bot = null;
                for (int j = 0; j < k; j++) {
                    Map.Entry<Integer, Node> floor = tops[j].ceilingEntry(newer.top[j]);
                    if (floor != null) {
                        top = min(top, floor.getValue());
                    }
                    Map.Entry<Integer, Node> ceil = bots[j].floorEntry(newer.top[j]);
                    if (ceil != null) {
                        bot = max(bot, ceil.getValue());
                    }
                }


                debug.debug("newer", newer);
                debug.debug("bot", bot);
                debug.debug("top", top);
                if (top == null) {
                    install(newer);
                } else if (bot.top[0] < top.top[0]) {
                    //insert into the middle
                    install(newer);
                } else {
                    //merge all
                    uninstall(bot);
                    while (bot != top) {
                        Node prev = tops[0].floorEntry(bot.top[0]).getValue();
                        uninstall(prev);
                        prev.mergeInto(bot);
                        bot = prev;
                    }
                    bot.mergeInto(newer);
                    install(bot);
                }

                int ans = tops[0].lastEntry().getValue().size;
                out.println(ans);

                debug.debug("tops[0]", tops[0]);
            }
        }

        public void uninstall(Node node) {
            for (int i = 0; i < k; i++) {
                tops[i].remove(node.top[i]);
                bots[i].remove(node.bot[i]);
            }
        }

        public void install(Node node) {
            for (int i = 0; i < k; i++) {
                tops[i].put(node.top[i], node);
                bots[i].put(node.bot[i], node);
            }
        }

        public Node min(Node a, Node b) {
            if (a == null) {
                return b;
            }
            if (b == null) {
                return a;
            }
            return a.top[0] < b.top[0] ? a : b;
        }

        public Node max(Node a, Node b) {
            if (a == null) {
                return b;
            }
            if (b == null) {
                return a;
            }
            return a.top[0] > b.top[0] ? a : b;
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

    static class Node {
        int[] top;
        int[] bot;
        int size = 1;

        public Node(int k) {
            top = new int[k];
            bot = new int[k];
        }

        void mergeInto(Node other) {
            int k = top.length;
            for (int i = 0; i < k; i++) {
                top[i] = Math.max(top[i], other.top[i]);
                bot[i] = Math.min(bot[i], other.bot[i]);
            }
            size += other.size;
        }

        public String toString() {
            return Arrays.toString(top) + " > " + Arrays.toString(bot);
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;
        static int[] empty = new int[0];

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, Object x) {
            return debug(name, x, empty);
        }

        public Debug debug(String name, Object x, int... indexes) {
            if (offline) {
                if (x == null || !x.getClass().isArray()) {
                    out.append(name);
                    for (int i : indexes) {
                        out.printf("[%d]", i);
                    }
                    out.append("=").append("" + x);
                    out.println();
                } else {
                    indexes = Arrays.copyOf(indexes, indexes.length + 1);
                    if (x instanceof byte[]) {
                        byte[] arr = (byte[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof short[]) {
                        short[] arr = (short[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof boolean[]) {
                        boolean[] arr = (boolean[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof char[]) {
                        char[] arr = (char[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof int[]) {
                        int[] arr = (int[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof float[]) {
                        float[] arr = (float[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof double[]) {
                        double[] arr = (double[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof long[]) {
                        long[] arr = (long[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else {
                        Object[] arr = (Object[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    }
                }
            }
            return this;
        }

    }
}

