import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.HashMap;
import java.io.IOException;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.Comparator;
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
            TaskF solver = new TaskF();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskF {
        private List<Hash> hashes = Arrays.asList(new Hash(10000, 31), new Hash(10000, 11), new Hash(10000, 41));

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            Node[] nodes = new Node[n + 1];
            for (int i = 1; i <= n; i++) {
                nodes[i] = new Node();
            }
            for (int i = 1; i < n; i++) {
                Node a = nodes[in.readInt()];
                Node b = nodes[in.readInt()];
                a.next.add(b);
                b.next.add(a);
            }

            List<Node> list = new ArrayList<>();
            dfs(nodes[1], null);
            dfsForCenter(nodes[1], null, n, list);
            if (list.size() != 1) {
                out.println(-1);
                return;
            }

            Map<List<Integer>, Integer> cntMap = new HashMap<>();
            Node center = list.get(0);
            for (Node node : center.next) {
                node.next.remove(center);
                handle(node, cntMap);
            }

            boolean exist = false;
            for (int value : cntMap.values()) {
                if (value == center.next.size()) {
                    exist = true;
                    break;
                }
            }

            if (exist) {
                out.println(center.next.size());
            } else {
                out.println(-1);
            }
        }

        public void handle(Node root, Map<List<Integer>, Integer> cntMap) {
            dfs(root, null);
            List<Node> centers = new ArrayList<>();
            dfsForCenter(root, null, root.size, centers);

            Set<List<Integer>> hashValue = new HashSet<>();
            for (Node node : centers) {
                List<Integer> hash = new ArrayList<>();
                for (Hash h : hashes) {
                    hash.add(dfsForHash(node, null, h));
                }
                hashValue.add(hash);
            }

            for (List<Integer> key : hashValue) {
                cntMap.put(key, cntMap.getOrDefault(key, 0) + 1);
            }
        }

        public int dfsForHash(Node root, Node p, Hash h) {
            List<Integer> children = new ArrayList<>();
            for (Node node : root.next) {
                if (node == p) {
                    continue;
                }
                children.add(dfsForHash(node, root, h));
            }
            children.sort(Comparator.naturalOrder());
            int[] data = new int[children.size() + 1];
            data[0] = 1;
            for (int i = 1; i < data.length; i++) {
                data[i] = children.get(i - 1);
            }
            h.populate(data, data.length);
            return h.hashVerbose(0, data.length - 1);
        }

        public void dfsForCenter(Node root, Node p, int total, List<Node> ans) {
            int max = total - root.size;
            for (Node node : root.next) {
                if (node == p) {
                    continue;
                }
                max = Math.max(max, node.size);
                dfsForCenter(node, root, total, ans);
            }
            if (max <= total / 2) {
                ans.add(root);
            }
        }

        public void dfs(Node root, Node p) {
            root.size = 1;
            for (Node node : root.next) {
                if (node == p) {
                    continue;
                }
                dfs(node, root);
                root.size += node.size;
            }
        }

    }

    static class Power {
        final Modular modular;

        public Power(Modular modular) {
            this.modular = modular;
        }

        public int pow(int x, long n) {
            if (n == 0) {
                return modular.valueOf(1);
            }
            long r = pow(x, n >> 1);
            r = modular.valueOf(r * r);
            if ((n & 1) == 1) {
                r = modular.valueOf(r * x);
            }
            return (int) r;
        }

        public int inverse(int x) {
            return pow(x, modular.m - 2);
        }

    }

    static class Hash {
        public static final Modular MOD = new Modular((int) (1e9 + 7));
        public static final Power POWER = new Power(MOD);
        private int[] inverse;
        private int[] xs;
        private int[] hash;

        public Hash(Hash model) {
            inverse = model.inverse;
            hash = new int[model.hash.length];
            xs = model.xs;
        }

        public Hash(int size, int x) {
            inverse = new int[size + 1];
            hash = new int[size + 1];
            xs = new int[size + 1];
            int invX = POWER.inverse(x);
            inverse[0] = 1;
            xs[0] = 1;
            for (int i = 1; i <= size; i++) {
                this.inverse[i] = MOD.mul(this.inverse[i - 1], invX);
                xs[i] = MOD.mul(xs[i - 1], x);
            }
        }

        public void populate(int[] data, int n) {
            hash[0] = data[0];
            for (int i = 1; i < n; i++) {
                hash[i] = MOD.plus(hash[i - 1], MOD.mul(data[i], xs[i]));
            }
        }

        public int hashVerbose(int l, int r) {
            int h = hash(l, r);
            h = MOD.plus(h, xs[r - l + 1]);
            return h;
        }

        public int hash(int l, int r) {
            long h = hash[r];
            if (l > 0) {
                h -= hash[l - 1];
                h *= inverse[l];
            }
            return MOD.valueOf(h);
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

        public FastOutput println(int c) {
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

    static class Modular {
        int m;

        public Modular(int m) {
            this.m = m;
        }

        public Modular(long m) {
            this.m = (int) m;
            if (this.m != m) {
                throw new IllegalArgumentException();
            }
        }

        public Modular(double m) {
            this.m = (int) m;
            if (this.m != m) {
                throw new IllegalArgumentException();
            }
        }

        public int valueOf(int x) {
            x %= m;
            if (x < 0) {
                x += m;
            }
            return x;
        }

        public int valueOf(long x) {
            x %= m;
            if (x < 0) {
                x += m;
            }
            return (int) x;
        }

        public int mul(int x, int y) {
            return valueOf((long) x * y);
        }

        public int plus(int x, int y) {
            return valueOf(x + y);
        }

        public String toString() {
            return "mod " + m;
        }

    }

    static class Node {
        List<Node> next = new ArrayList<>();
        int size;

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
}

