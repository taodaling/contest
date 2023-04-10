import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.io.OutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.io.UncheckedIOException;
import java.io.Closeable;
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
            BZOJ1018 solver = new BZOJ1018();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class BZOJ1018 {
        int C;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            C = in.ri();

            DynamicConnectivity dc = new DynamicConnectivity(2 * C, 100000);
            while (true) {
                String cmd = in.rs();
                if (cmd.equals("Exit")) {
                    return;
                }
                int x = in.ri() - 1;
                int y = in.ri() - 1;
                int a = in.ri() - 1;
                int b = in.ri() - 1;
                int id1 = id(x, y);
                int id2 = id(a, b);
                if (cmd.equals("Open")) {
                    dc.addEdge(id1, id2);
                } else if (cmd.equals("Close")) {
                    dc.deleteEdge(id1, id2);
                } else {
                    if (dc.check(id1, id2)) {
                        out.println("Y");
                    } else {
                        out.println("N");
                    }
                }
            }
        }

        public int id(int a, int b) {
            return a * C + b;
        }

    }

    static class LongObjectHashMap<V> {
        private int[] slot;
        private int[] next;
        private long[] keys;
        private Object[] values;
        private int alloc;
        private boolean[] removed;
        private int mask;
        private int size;
        private boolean rehash;
        private Hasher hasher = new Hasher();
        private int[] version;
        private int now;

        private void access(int i) {
            if (version[i] != now) {
                slot[i] = 0;
                version[i] = now;
            }
        }

        public LongObjectHashMap(int cap, boolean rehash) {
            this.mask = (1 << (32 - Integer.numberOfLeadingZeros(cap - 1))) - 1;
            slot = new int[mask + 1];
            version = new int[mask + 1];
            next = new int[cap + 1];
            keys = new long[cap + 1];
            values = new Object[cap + 1];
            removed = new boolean[cap + 1];
            this.rehash = rehash;
        }

        private void doubleCapacity() {
            int newSize = Math.max(next.length + 10, next.length * 2);
            next = Arrays.copyOf(next, newSize);
            keys = Arrays.copyOf(keys, newSize);
            values = Arrays.copyOf(values, newSize);
            removed = Arrays.copyOf(removed, newSize);
        }

        private void rehash() {
            int[] newSlots = new int[Math.max(16, slot.length * 2)];
            int[] newVersions = new int[newSlots.length];
            int newMask = newSlots.length - 1;
            for (int i = 0; i < slot.length; i++) {
                access(i);
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
            this.version = newVersions;
            now = 0;
        }

        private void alloc() {
            alloc++;
            if (alloc >= next.length) {
                doubleCapacity();
            }
            next[alloc] = 0;
            removed[alloc] = false;
            values[alloc] = null;
            size++;
        }

        private int hash(long x) {
            return hasher.hash(x);
        }

        public void put(long x, V y) {
            int h = hash(x);
            int s = h & mask;
            access(s);
            if (slot[s] == 0) {
                alloc();
                slot[s] = alloc;
                keys[alloc] = x;
                values[alloc] = y;
            } else {
                int index = findIndexOrLastEntry(s, x);
                if (keys[index] != x) {
                    alloc();
                    next[index] = alloc;
                    keys[alloc] = x;
                    values[alloc] = y;
                } else {
                    values[index] = y;
                }
            }
            if (rehash && size >= slot.length) {
                rehash();
            }
        }

        public boolean containKey(long x) {
            int h = hash(x);
            int s = h & mask;
            access(s);
            if (slot[s] == 0) {
                return false;
            }
            return keys[findIndexOrLastEntry(s, x)] == x;
        }

        public V remove(long x) {
            int h = hash(x);
            int s = h & mask;
            access(s);
            if (slot[s] == 0) {
                return null;
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
                return null;
            }
            if (slot[s] == index) {
                slot[s] = next[index];
            } else {
                next[pre] = next[index];
            }
            removed[index] = true;
            size--;
            return (V) values[index];
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

        public LongObjectEntryIterator<V> iterator() {
            return new LongObjectEntryIterator() {
                int index = 1;
                int readIndex = -1;


                public boolean hasNext() {
                    while (index <= alloc && removed[index]) {
                        index++;
                    }
                    return index <= alloc;
                }


                public long getEntryKey() {
                    return keys[readIndex];
                }


                public Object getEntryValue() {
                    return values[readIndex];
                }


                public void next() {
                    if (!hasNext()) {
                        throw new IllegalStateException();
                    }
                    readIndex = index;
                    index++;
                }
            };
        }

        public String toString() {
            LongObjectEntryIterator<V> iterator = iterator();
            StringBuilder builder = new StringBuilder("{");
            while (iterator.hasNext()) {
                iterator.next();
                builder.append(iterator.getEntryKey()).append("->").append(iterator.getEntryValue()).append(',');
            }
            if (builder.charAt(builder.length() - 1) == ',') {
                builder.setLength(builder.length() - 1);
            }
            builder.append('}');
            return builder.toString();
        }

    }

    static class DynamicConnectivity {
        DynamicConnectivity.EulerTourTree[] levels;
        int n;
        int log2;
        LongObjectHashMap<DynamicConnectivity.EdgeInfo> map;

        public DynamicConnectivity(int n, int m) {
            this.n = n;
            this.log2 = 32 - Integer.numberOfLeadingZeros(n - 1);
            levels = new DynamicConnectivity.EulerTourTree[log2 + 1];
            for (int i = 1; i <= log2; i++) {
                levels[i] = new DynamicConnectivity.EulerTourTree(n, 0);
                levels[i].id = i;
            }
            map = new LongObjectHashMap<>(m, true);
        }

        public boolean check(int i, int j) {
            if (i == j) {
                return true;
            }
            int r1 = levels[log2].rootOf(i);
            int r2 = levels[log2].rootOf(j);
            return r1 == r2;
        }

        public void addEdge(int i, int j) {
            DynamicConnectivity.EdgeInfo info = new DynamicConnectivity.EdgeInfo();
            info.a = i;
            info.b = j;
            info.level = log2 + 1;
            map.put(idOfEdge(i, j), info);
            pushDownEdge(info, levels[log2].rootOf(info.a) != levels[log2].rootOf(info.b));
        }

        private void pushDownEdge(DynamicConnectivity.EdgeInfo info, boolean link) {
            info.level--;
            if (info.level == 0) {
                return;
            }

            addEdgeChain(info);
            if (link) {
                levels[info.level].link(info);
            }
        }

        private void addEdgeChain(DynamicConnectivity.EdgeInfo info) {
            DynamicConnectivity.EdgeChain chain1 = new DynamicConnectivity.EdgeChain(info);
            DynamicConnectivity.EdgeChain chain2 = new DynamicConnectivity.EdgeChain(info);
            DynamicConnectivity.Splay.splay(levels[info.level].nodes[info.a]);
            levels[info.level].nodes[info.a].addEdge(chain1);
            levels[info.level].nodes[info.a].pushUp();
            DynamicConnectivity.Splay.splay(levels[info.level].nodes[info.b]);
            levels[info.level].nodes[info.b].addEdge(chain2);
            levels[info.level].nodes[info.b].pushUp();
        }

        public void deleteEdge(int i, int j) {
            long idOfEdge = idOfEdge(i, j);
            DynamicConnectivity.EdgeInfo info = map.remove(idOfEdge);
            if (info.level == 0) {
                return;
            }

            int curLevel = info.level;
            info.level = -1;
            if (!levels[log2].map.containKey(idOfEdge)) {
                return;
            }

            for (int k = curLevel; k <= log2; k++) {
                levels[k].cut(i, j);
            }

            for (int k = curLevel; k <= log2; k++) {
                DynamicConnectivity.Splay ti = levels[k].nodes[i];
                DynamicConnectivity.Splay tj = levels[k].nodes[j];
                DynamicConnectivity.Splay.splay(ti);
                DynamicConnectivity.Splay.splay(tj);

                if (ti.size > tj.size) {
                    DynamicConnectivity.Splay tmp = ti;
                    ti = tj;
                    tj = tmp;
                }

                DynamicConnectivity.Splay.splay(ti);
                while (ti.infoWithMaxLevel != null) {
                    ti = ti.infoWithMaxLevel;
                    DynamicConnectivity.Splay.splay(ti);
                    if (ti.infoWithMaxLevel.info.level < k) {
                        break;
                    }
                    pushDownEdge(ti.info, true);
                    DynamicConnectivity.Splay.splay(ti);
                }
                DynamicConnectivity.Splay.splay(ti);
                while (ti.containEdge != null) {
                    ti = ti.containEdge;
                    DynamicConnectivity.Splay.splay(ti);
                    DynamicConnectivity.EdgeInfo e = ti.popEdge().info;
                    ti.pushUp();
                    if (e.level < k) {
                    } else if (levels[log2].rootOf(e.a) ==
                            levels[log2].rootOf(e.b)) {
                        if (e.level == k) {
                            pushDownEdge(e, false);
                        }
                    } else {
                        addEdgeChain(e);
                        for (int t = k; t <= log2; t++) {
                            levels[t].link(e);
                        }
                        return;
                    }
                    DynamicConnectivity.Splay.splay(ti);
                }
            }
        }

        private static long idOfEdge(int i, int j) {
            if (i > j) {
                int tmp = i;
                i = j;
                j = tmp;
            }
            return (((long) i) << 32) | j;
        }

        private static class EdgeInfo {
            int a;
            int b;
            int level;

            public String toString() {
                return String.format("%d -> %d", a, b);
            }

        }

        private static class EdgeChain {
            final DynamicConnectivity.EdgeInfo info;
            DynamicConnectivity.EdgeChain next;

            private EdgeChain(DynamicConnectivity.EdgeInfo info) {
                this.info = info;
            }

            public String toString() {
                if (next == null) {
                    return info.toString();
                }
                return info.toString() + ", " + next.toString();
            }

        }

        public static class EulerTourTree {
            DynamicConnectivity.Splay[] nodes;
            int id;
            LongObjectHashMap<DynamicConnectivity.EulerTourTree.Edge> map;

            private DynamicConnectivity.Splay alloc(int id) {
                DynamicConnectivity.Splay splay = new DynamicConnectivity.Splay();//buffer.alloc();
                splay.id = id;
                return splay;
            }

            public EulerTourTree(int n, int m) {
                map = new LongObjectHashMap<>(m, true);
                nodes = new DynamicConnectivity.Splay[n];
                for (int i = 0; i < n; i++) {
                    nodes[i] = alloc(i);
                    nodes[i].node = 1;
                    nodes[i].pushUp();
                }
            }

            private void destroy(DynamicConnectivity.Splay s) {
//            s.info = null;
//            s.chain = null;
//            s.infoWithMaxLevel = null;
//            s.containEdge = null;
//            buffer.release(s);
            }

            public int rootOf(int i) {
                return rootOf(nodes[i]).id;
            }

            public void setRoot(int i) {
                if (rootOf(i) == i) {
                    return;
                }

                DynamicConnectivity.Splay.splay(nodes[i]);
                DynamicConnectivity.Splay l = DynamicConnectivity.Splay.splitLeft(nodes[i]);
                if (l == DynamicConnectivity.Splay.NIL) {
                    return;
                }
                DynamicConnectivity.Splay a = DynamicConnectivity.Splay.selectMinAsRoot(l);
                DynamicConnectivity.Splay b = DynamicConnectivity.Splay.selectMaxAsRoot(nodes[i]);

                if (nodes[a.id] == a) {
                    DynamicConnectivity.Splay.splitLeft(b);
                    destroy(b);
                } else {
                    l = DynamicConnectivity.Splay.splitRight(a);
                    destroy(a);
                }

                DynamicConnectivity.Splay newNode = alloc(i);
                DynamicConnectivity.Splay.splay(nodes[i]);
                DynamicConnectivity.Splay.splay(l);
                DynamicConnectivity.Splay.merge(nodes[i], DynamicConnectivity.Splay.merge(l, newNode));
            }

            public void link(DynamicConnectivity.EdgeInfo info) {
                int i = info.a;
                int j = info.b;
                setRoot(i);
                setRoot(j);

                DynamicConnectivity.EulerTourTree.Edge e = new DynamicConnectivity.EulerTourTree.Edge();

                long id = idOfEdge(i, j);
                e.a = alloc(-i * 10000 - j);
                e.b = alloc(-i * 10000 - j);
                e.a.info = info;
                e.a.pushUp();
                e.b.pushUp();
                map.put(id, e);

                DynamicConnectivity.Splay.splay(nodes[i]);
                DynamicConnectivity.Splay.splay(nodes[j]);
                DynamicConnectivity.Splay.merge(nodes[i], e.a);
                DynamicConnectivity.Splay.merge(nodes[j], e.b);
                DynamicConnectivity.Splay.splay(nodes[i]);
                DynamicConnectivity.Splay.splay(nodes[j]);
                DynamicConnectivity.Splay.merge(nodes[i], nodes[j]);

                DynamicConnectivity.Splay newNode = alloc(i);
                DynamicConnectivity.Splay.splay(nodes[i]);
                DynamicConnectivity.Splay.merge(nodes[i], newNode);
            }

            private DynamicConnectivity.Splay rootOf(DynamicConnectivity.Splay x) {
                DynamicConnectivity.Splay.splay(x);
                return DynamicConnectivity.Splay.selectMinAsRoot(x);
            }

            public void cut(int i, int j) {
                long id = idOfEdge(i, j);
                DynamicConnectivity.EulerTourTree.Edge e = map.remove(id);

                DynamicConnectivity.Splay.splay(e.a);
                DynamicConnectivity.Splay al = DynamicConnectivity.Splay.splitLeft(e.a);
                DynamicConnectivity.Splay ar = DynamicConnectivity.Splay.splitRight(e.a);


                DynamicConnectivity.Splay l, r;
                if (rootOf(ar) == rootOf(e.b)) {
                    DynamicConnectivity.Splay.splay(e.b);
                    DynamicConnectivity.Splay bl = DynamicConnectivity.Splay.splitLeft(e.b);
                    DynamicConnectivity.Splay br = DynamicConnectivity.Splay.splitRight(e.b);

                    l = al;
                    r = br;
                } else {
                    DynamicConnectivity.Splay.splay(e.b);
                    DynamicConnectivity.Splay bl = DynamicConnectivity.Splay.splitLeft(e.b);
                    DynamicConnectivity.Splay br = DynamicConnectivity.Splay.splitRight(e.b);

                    l = bl;
                    r = ar;
                }

                DynamicConnectivity.Splay.splay(l);
                DynamicConnectivity.Splay.splay(r);
                l = DynamicConnectivity.Splay.selectMaxAsRoot(l);
                r = DynamicConnectivity.Splay.selectMinAsRoot(r);

                if (nodes[l.id] == l) {
                    DynamicConnectivity.Splay rSnapshot = r;
                    r = DynamicConnectivity.Splay.splitRight(r);
                    destroy(rSnapshot);
                } else {
                    DynamicConnectivity.Splay lSnapshot = l;
                    l = DynamicConnectivity.Splay.splitLeft(l);
                    destroy(lSnapshot);
                }

                DynamicConnectivity.Splay.merge(l, r);
                destroy(e.a);
                destroy(e.b);
            }

            private static class Edge {
                DynamicConnectivity.Splay a;
                DynamicConnectivity.Splay b;

            }

        }

        public static class Splay implements Cloneable {
            public static final DynamicConnectivity.Splay NIL = new DynamicConnectivity.Splay();
            DynamicConnectivity.Splay left = NIL;
            DynamicConnectivity.Splay right = NIL;
            DynamicConnectivity.Splay father = NIL;
            int size;
            byte node;
            int id;
            DynamicConnectivity.EdgeChain chain;
            DynamicConnectivity.EdgeInfo info;
            DynamicConnectivity.Splay containEdge;
            DynamicConnectivity.Splay infoWithMaxLevel;

            static {
                NIL.left = NIL;
                NIL.right = NIL;
                NIL.father = NIL;
                NIL.size = 0;
                NIL.id = -2;
            }

            public void addEdge(DynamicConnectivity.EdgeChain newChain) {
                newChain.next = chain;
                chain = newChain;
                containEdge = this;
            }

            public DynamicConnectivity.EdgeChain popEdge() {
                DynamicConnectivity.EdgeChain head = chain;
                chain = head.next;
                head.next = null;
                return head;
            }

            public static void splay(DynamicConnectivity.Splay x) {
                if (x == NIL) {
                    return;
                }
                DynamicConnectivity.Splay y, z;
                while ((y = x.father) != NIL) {
                    if ((z = y.father) == NIL) {
                        y.pushDown();
                        x.pushDown();
                        if (x == y.left) {
                            zig(x);
                        } else {
                            zag(x);
                        }
                    } else {
                        z.pushDown();
                        y.pushDown();
                        x.pushDown();
                        if (x == y.left) {
                            if (y == z.left) {
                                zig(y);
                                zig(x);
                            } else {
                                zig(x);
                                zag(x);
                            }
                        } else {
                            if (y == z.left) {
                                zag(x);
                                zig(x);
                            } else {
                                zag(y);
                                zag(x);
                            }
                        }
                    }
                }

                x.pushDown();
                x.pushUp();
            }

            public static void zig(DynamicConnectivity.Splay x) {
                DynamicConnectivity.Splay y = x.father;
                DynamicConnectivity.Splay z = y.father;
                DynamicConnectivity.Splay b = x.right;

                y.setLeft(b);
                x.setRight(y);
                z.changeChild(y, x);

                y.pushUp();
            }

            public static void zag(DynamicConnectivity.Splay x) {
                DynamicConnectivity.Splay y = x.father;
                DynamicConnectivity.Splay z = y.father;
                DynamicConnectivity.Splay b = x.left;

                y.setRight(b);
                x.setLeft(y);
                z.changeChild(y, x);

                y.pushUp();
            }

            public void setLeft(DynamicConnectivity.Splay x) {
                left = x;
                x.father = this;
            }

            public void setRight(DynamicConnectivity.Splay x) {
                right = x;
                x.father = this;
            }

            public void changeChild(DynamicConnectivity.Splay y, DynamicConnectivity.Splay x) {
                if (left == y) {
                    setLeft(x);
                } else {
                    setRight(x);
                }
            }

            public void pushUp() {
                if (this == NIL) {
                    return;
                }
                size = left.size + right.size + node;

                containEdge = null;
                if (chain != null) {
                    containEdge = this;
                } else if (left.containEdge != null) {
                    containEdge = left.containEdge;
                } else {
                    containEdge = right.containEdge;
                }

                infoWithMaxLevel = null;
                if (info != null) {
                    infoWithMaxLevel = this;
                }
                if (left.infoWithMaxLevel != null &&
                        (infoWithMaxLevel == null || infoWithMaxLevel.info.level < left.infoWithMaxLevel.info.level)) {
                    infoWithMaxLevel = left.infoWithMaxLevel;
                }
                if (right.infoWithMaxLevel != null &&
                        (infoWithMaxLevel == null || infoWithMaxLevel.info.level < right.infoWithMaxLevel.info.level)) {
                    infoWithMaxLevel = right.infoWithMaxLevel;
                }
            }

            public void pushDown() {
            }

            public static void toString(DynamicConnectivity.Splay root, StringBuilder builder) {
                if (root == NIL) {
                    return;
                }
                root.pushDown();
                toString(root.left, builder);
                builder.append(root.id).append(',');
                toString(root.right, builder);
            }

            public DynamicConnectivity.Splay clone() {
                try {
                    return (DynamicConnectivity.Splay) super.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }

            public static DynamicConnectivity.Splay cloneTree(DynamicConnectivity.Splay splay) {
                if (splay == NIL) {
                    return NIL;
                }
                splay = splay.clone();
                splay.left = cloneTree(splay.left);
                splay.right = cloneTree(splay.right);
                return splay;
            }

            public static DynamicConnectivity.Splay selectMinAsRoot(DynamicConnectivity.Splay root) {
                if (root == NIL) {
                    return root;
                }
                root.pushDown();
                while (root.left != NIL) {
                    root = root.left;
                    root.pushDown();
                }
                splay(root);
                return root;
            }

            public static DynamicConnectivity.Splay selectMaxAsRoot(DynamicConnectivity.Splay root) {
                if (root == NIL) {
                    return root;
                }
                root.pushDown();
                while (root.right != NIL) {
                    root = root.right;
                    root.pushDown();
                }
                splay(root);
                return root;
            }

            public static DynamicConnectivity.Splay splitLeft(DynamicConnectivity.Splay root) {
                root.pushDown();
                DynamicConnectivity.Splay left = root.left;
                left.father = NIL;
                root.setLeft(NIL);
                root.pushUp();
                return left;
            }

            public static DynamicConnectivity.Splay splitRight(DynamicConnectivity.Splay root) {
                root.pushDown();
                DynamicConnectivity.Splay right = root.right;
                right.father = NIL;
                root.setRight(NIL);
                root.pushUp();
                return right;
            }

            public static DynamicConnectivity.Splay merge(DynamicConnectivity.Splay a, DynamicConnectivity.Splay b) {
                if (a == NIL) {
                    return b;
                }
                if (b == NIL) {
                    return a;
                }
                a = selectMaxAsRoot(a);
                a.setRight(b);
                a.pushUp();
                return a;
            }

            public String toString() {
                StringBuilder builder = new StringBuilder().append(id).append(":");
                toString(cloneTree(this), builder);
                return builder.toString();
            }

        }

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

        public FastOutput append(String c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput println(String c) {
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

    static class FastInput {
        private final InputStream is;
        private StringBuilder defaultStringBuf = new StringBuilder(1 << 13);
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

        public int ri() {
            return readInt();
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

        public String rs() {
            return readString();
        }

        public String readString(StringBuilder builder) {
            skipBlank();

            while (next > 32) {
                builder.append((char) next);
                next = read();
            }

            return builder.toString();
        }

        public String readString() {
            defaultStringBuf.setLength(0);
            return readString(defaultStringBuf);
        }

    }

    static class Hasher {
        private final long time = System.nanoTime() + System.currentTimeMillis() * 31L;

        public int shuffle(long z) {
            z += time;
            z = (z ^ (z >>> 33)) * 0x62a9d9ed799705f5L;
            return (int) (((z ^ (z >>> 28)) * 0xcb24d0a5c88c35b3L) >>> 32);
        }

        public int hash(long x) {
            return shuffle(x);
        }

    }

    static interface LongObjectEntryIterator<V> {
        boolean hasNext();

        void next();

        long getEntryKey();

        V getEntryValue();

    }
}

