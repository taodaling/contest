package template;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LUOGU1401 {
    public static void main(String[] args) throws Exception {
        boolean local = System.getSecurityManager() == null;
        boolean async = false;

        Charset charset = Charset.forName("ascii");

        FastIO io = local ? new FastIO(new FileInputStream("D:\\DATABASE\\TESTCASE\\Code.in"), System.out, charset) : new FastIO(System.in, System.out, charset);
        Task task = new Task(io, new Debug(local));

        if (async) {
            Thread t = new Thread(null, task, "dalt", 1 << 27);
            t.setPriority(Thread.MAX_PRIORITY);
            t.start();
            t.join();
        } else {
            task.run();
        }

        if (local) {
            io.cache.append("\n\n--memory -- \n" + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) >> 20) + "M");
        }

        io.flush();
    }

    public static class Task implements Runnable {
        final FastIO io;
        final Debug debug;
        int inf = (int) 1e8;
        long lInf = (long) 1e18;
        double dInf = 1e50;

        public Task(FastIO io, Debug debug) {
            this.io = io;
            this.debug = debug;
        }

        @Override
        public void run() {
            solve();
        }

        int n;
        int m;
        int t;
        int[][] edges;

        public void solve() {
            n = io.readInt();
            m = io.readInt();
            t = io.readInt();
            edges = new int[m][3];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < 3; j++) {
                    edges[i][j] = io.readInt();
                }
            }
            Arrays.sort(edges, (a, b) -> a[2] - b[2]);

            ISAP isap = new ISAP(n);
            isap.setSource(1);
            isap.setTarget(n);

            int rangeSince = 0;
            int sqrt = 100;
            double flow = 0;
            for (int i = 0; i < m; i += sqrt) {
                rangeSince = i;
                for (int j = i; j < i + sqrt && j < m; j++) {
                    isap.getChannel(edges[j][0], edges[j][1]).modify(2, 1);
                }
                isap.bfsFlag = false;
                flow += isap.sendFlow(inf);
                if(flow >= t){
                    break;
                }
            }

            isap = new ISAP(n);
            isap.setSource(1);
            isap.setTarget(n);
            for (int i = 0; i < rangeSince; i++) {
                isap.getChannel(edges[i][0], edges[i][1]).modify(2, 1);
            }
            flow = isap.sendFlow(inf);
            while (flow < t && rangeSince < m) {
                isap.getChannel(edges[rangeSince][0], edges[rangeSince][1]).modify(2, 1);
                isap.bfsFlag = false;
                flow += isap.sendFlow(1);
                rangeSince++;
            }

            io.cache.append(edges[rangeSince - 1][2]);
        }
    }

    public static class FastIO {
        public final StringBuilder cache = new StringBuilder(1 << 13);
        private final InputStream is;
        private final OutputStream os;
        private final Charset charset;
        private StringBuilder defaultStringBuf = new StringBuilder(1 << 13);
        private byte[] buf = new byte[1 << 13];
        private int bufLen;
        private int bufOffset;
        private int next;

        public FastIO(InputStream is, OutputStream os, Charset charset) {
            this.is = is;
            this.os = os;
            this.charset = charset;
        }

        public FastIO(InputStream is, OutputStream os) {
            this(is, os, Charset.forName("ascii"));
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

        public long readLong() {
            int sign = 1;

            skipBlank();
            if (next == '+' || next == '-') {
                sign = next == '+' ? 1 : -1;
                next = read();
            }

            long val = 0;
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

        public double readDouble() {
            boolean sign = true;
            skipBlank();
            if (next == '+' || next == '-') {
                sign = next == '+';
                next = read();
            }

            long val = 0;
            while (next >= '0' && next <= '9') {
                val = val * 10 + next - '0';
                next = read();
            }
            if (next != '.') {
                return sign ? val : -val;
            }
            next = read();
            long radix = 1;
            long point = 0;
            while (next >= '0' && next <= '9') {
                point = point * 10 + next - '0';
                radix = radix * 10;
                next = read();
            }
            double result = val + (double) point / radix;
            return sign ? result : -result;
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

        public int readLine(char[] data, int offset) {
            int originalOffset = offset;
            while (next != -1 && next != '\n') {
                data[offset++] = (char) next;
                next = read();
            }
            return offset - originalOffset;
        }

        public int readString(char[] data, int offset) {
            skipBlank();

            int originalOffset = offset;
            while (next > 32) {
                data[offset++] = (char) next;
                next = read();
            }

            return offset - originalOffset;
        }

        public int readString(byte[] data, int offset) {
            skipBlank();

            int originalOffset = offset;
            while (next > 32) {
                data[offset++] = (byte) next;
                next = read();
            }

            return offset - originalOffset;
        }

        public char readChar() {
            skipBlank();
            char c = (char) next;
            next = read();
            return c;
        }

        public void flush() throws IOException {
            os.write(cache.toString().getBytes(charset));
            os.flush();
            cache.setLength(0);
        }

        public boolean hasMore() {
            skipBlank();
            return next != -1;
        }
    }

    public static class Debug {
        private boolean allowDebug;

        public Debug(boolean allowDebug) {
            this.allowDebug = allowDebug;
        }

        public void assertTrue(boolean flag) {
            if (!allowDebug) {
                return;
            }
            if (!flag) {
                fail();
            }
        }

        public void fail() {
            throw new RuntimeException();
        }

        public void assertFalse(boolean flag) {
            if (!allowDebug) {
                return;
            }
            if (flag) {
                fail();
            }
        }

        private void outputName(String name) {
            System.out.print(name + " = ");
        }

        public void debug(String name, int x) {
            if (!allowDebug) {
                return;
            }

            outputName(name);
            System.out.println("" + x);
        }

        public void debug(String name, long x) {
            if (!allowDebug) {
                return;
            }
            outputName(name);
            System.out.println("" + x);
        }

        public void debug(String name, double x) {
            if (!allowDebug) {
                return;
            }
            outputName(name);
            System.out.println("" + x);
        }

        public void debug(String name, int[] x) {
            if (!allowDebug) {
                return;
            }
            outputName(name);
            System.out.println(Arrays.toString(x));
        }

        public void debug(String name, long[] x) {
            if (!allowDebug) {
                return;
            }
            outputName(name);
            System.out.println(Arrays.toString(x));
        }

        public void debug(String name, double[] x) {
            if (!allowDebug) {
                return;
            }
            outputName(name);
            System.out.println(Arrays.toString(x));
        }

        public void debug(String name, Object x) {
            if (!allowDebug) {
                return;
            }
            outputName(name);
            System.out.println("" + x);
        }

        public void debug(String name, Object... x) {
            if (!allowDebug) {
                return;
            }
            outputName(name);
            System.out.println(Arrays.deepToString(x));
        }
    }

    public static class ISAP {
        Node[] nodes;
        int[] distanceCnt;
        Node source;
        Node target;
        int nodeNum;
        Map<Long, DirectChannel> channelMap = new HashMap();
        Deque<Node> deque;
        boolean bfsFlag = false;

        public List<Node> getComponentS() {
            List<Node> result = new ArrayList();
            for (int i = 1; i <= nodeNum; i++) {
                nodes[i].visited = false;
            }
            deque.addLast(source);
            source.visited = true;
            while (!deque.isEmpty()) {
                Node head = deque.removeFirst();
                result.add(head);
                for (Channel channel : head.channelList) {
                    if (channel.getFlow() == channel.getCapacity()) {
                        continue;
                    }
                    Node node = channel.getDst();
                    if (node.visited) {
                        continue;
                    }
                    node.visited = true;
                    deque.addLast(node);
                }
            }
            return result;
        }

        public Collection<DirectChannel> getChannels() {
            return channelMap.values();
        }

        public DirectChannel addChannel(int src, int dst) {
            DirectChannel channel = new DirectChannel(nodes[src], nodes[dst], 0, 0);
            nodes[src].channelList.add(channel);
            nodes[dst].channelList.add(channel.getInverse());
            return channel;
        }

        public DirectChannel getChannel(int src, int dst) {
            Long id = (((long) src) << 32) | dst;
            DirectChannel channel = channelMap.get(id);
            if (channel == null) {
                channel = addChannel(src, dst);
                channelMap.put(id, channel);
            }
            return channel;
        }

        public ISAP(int nodeNum) {
            this.nodeNum = nodeNum;
            deque = new ArrayDeque(nodeNum);
            nodes = new Node[nodeNum + 1];
            distanceCnt = new int[nodeNum + 2];
            for (int i = 1; i <= nodeNum; i++) {
                Node node = new Node();
                node.id = i;
                nodes[i] = node;
            }
        }

        public double sendFlow(double flow) {
            bfs();
            double flowSnapshot = flow;
            while (flow > 0 && source.distance < nodeNum) {
                flow -= send(source, flow);
            }
            return flowSnapshot - flow;
        }

        public double send(Node node, double flowRemain) {
            if (node == target) {
                return flowRemain;
            }

            double flowSnapshot = flowRemain;
            int nextDistance = node.distance - 1;
            for (Channel channel : node.channelList) {
                double channelRemain = channel.getCapacity() - channel.getFlow();
                Node dst = channel.getDst();
                if (channelRemain == 0 || dst.distance != nextDistance) {
                    continue;
                }
                double actuallySend = send(channel.getDst(), Math.min(flowRemain, channelRemain));
                channel.sendFlow(actuallySend);
                flowRemain -= actuallySend;
                if (flowRemain == 0) {
                    break;
                }
            }

            if (flowSnapshot == flowRemain) {
                if (--distanceCnt[node.distance] == 0) {
                    distanceCnt[source.distance]--;
                    source.distance = nodeNum;
                    distanceCnt[source.distance]++;
                    if (node != source) {
                        distanceCnt[++node.distance]++;
                    }
                } else {
                    distanceCnt[++node.distance]++;
                }
            }

            return flowSnapshot - flowRemain;
        }

        public void setSource(int id) {
            source = nodes[id];
        }

        public void setTarget(int id) {
            target = nodes[id];
        }

        public void bfs() {
            if (bfsFlag) {
                return;
            }
            bfsFlag = true;
            Arrays.fill(distanceCnt, 0);
            deque.clear();

            for (int i = 1; i <= nodeNum; i++) {
                nodes[i].distance = nodeNum;
            }

            target.distance = 0;
            deque.addLast(target);

            while (!deque.isEmpty()) {
                Node head = deque.removeFirst();
                distanceCnt[head.distance]++;
                for (Channel channel : head.channelList) {
                    Channel inverse = channel.getInverse();
                    if (inverse.getCapacity() == inverse.getFlow()) {
                        continue;
                    }
                    Node dst = channel.getDst();
                    if (dst.distance != nodeNum) {
                        continue;
                    }
                    dst.distance = head.distance + 1;
                    deque.addLast(dst);
                }
            }
        }

        public static interface Channel {
            public Node getSrc();

            public Node getDst();

            public double getCapacity();

            public double getFlow();

            public void sendFlow(double volume);

            public Channel getInverse();
        }

        public static class DirectChannel implements Channel {
            final Node src;
            final Node dst;
            final int id;
            double capacity;
            double flow;
            Channel inverse;

            public DirectChannel(Node src, Node dst, int capacity, int id) {
                this.src = src;
                this.dst = dst;
                this.capacity = capacity;
                this.id = id;
                inverse = new InverseChannelWrapper(this);
            }

            public void reset(double cap, double flow) {
                this.flow = flow;
                this.capacity = cap;
            }

            public void modify(double cap, double flow) {
                this.capacity += cap;
                this.flow += flow;
            }

            @Override
            public String toString() {
                return String.format("%s--%s/%s-->%s", getSrc(), getFlow(), getCapacity(), getDst());
            }

            @Override
            public Node getSrc() {
                return src;
            }

            @Override
            public Channel getInverse() {
                return inverse;
            }


            public void setCapacity(int expand) {
                capacity = expand;
            }

            @Override
            public Node getDst() {
                return dst;
            }

            @Override
            public double getCapacity() {
                return capacity;
            }

            @Override
            public double getFlow() {
                return flow;
            }

            @Override
            public void sendFlow(double volume) {
                flow += volume;
            }


        }

        public static class InverseChannelWrapper implements Channel {
            final Channel channel;

            public InverseChannelWrapper(Channel channel) {
                this.channel = channel;
            }

            @Override
            public Channel getInverse() {
                return channel;
            }


            @Override
            public Node getSrc() {
                return channel.getDst();
            }

            @Override
            public Node getDst() {
                return channel.getSrc();
            }

            @Override
            public double getCapacity() {
                return channel.getFlow();
            }

            @Override
            public double getFlow() {
                return 0;
            }

            @Override
            public void sendFlow(double volume) {
                channel.sendFlow(-volume);
            }


            @Override
            public String toString() {
                return String.format("%s--%s/%s-->%s", getSrc(), getFlow(), getCapacity(), getDst());
            }
        }

        public static class Node {
            int id;
            int distance;
            boolean visited;
            List<Channel> channelList = new ArrayList(1);

            @Override
            public String toString() {
                return "" + id;
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (DirectChannel channel : getChannels()) {
                if (channel.getFlow() == 0) {
                    continue;
                }
                builder.append(channel).append('\n');
            }

            for (DirectChannel channel : getChannels()) {
                if (channel.getFlow() != 0) {
                    continue;
                }
                builder.append(channel).append('\n');
            }
            return builder.toString();
        }
    }
}
