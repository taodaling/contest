import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Deque;
import java.util.ArrayList;
import java.util.Map;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.util.stream.DoubleStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.io.Writer;
import java.util.ArrayDeque;
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
            TaskE solver = new TaskE();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskE {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int[] data = new int[n];
            for (int i = 0; i < n; i++) {
                data[i] = in.readInt();
            }
            long sum = 0;
            for (int i = 0; i < n; i++) {
                sum += data[i];
            }

            double[] dDouble = Arrays.stream(data).mapToDouble(x -> -x).toArray();
            MinimumCloseSubGraph mcsg = new MinimumCloseSubGraph(dDouble);
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if ((j + 1) % (i + 1) == 0) {
                        mcsg.addDependency(i, j);
                    }
                }
            }

            long ans = sum + DigitUtils.round(mcsg.solve());
            out.println(ans);
        }

    }

    static class MinimumCloseSubGraph {
        private ISAP isap;
        private int n;
        private double sumOfPositive;
        private boolean solved;
        private double minCut;

        public MinimumCloseSubGraph(double[] weight) {
            this.n = weight.length;
            isap = new ISAP(n + 2);
            int idOfSrc = n;
            int idOfDst = n + 1;
            isap.setSource(idOfSrc);
            isap.setTarget(idOfDst);

            for (int i = 0; i < n; i++) {
                if (weight[i] > 0) {
                    isap.getChannel(idOfSrc, i).reset(weight[i], 0);
                    sumOfPositive += weight[i];
                }
                if (weight[i] < 0) {
                    isap.getChannel(i, idOfDst).reset(-weight[i], 0);
                }
            }
        }

        public void addDependency(int from, int to) {
            if (!(from >= 0 && from < n && to >= 0 && to < n)) {
                throw new IllegalArgumentException();
            }
            isap.getChannel(from, to).reset(1e50, 0);
        }

        public double solve() {
            if (!solved) {
                minCut = isap.sendFlow(1e50);
                solved = true;
            }
            return sumOfPositive - minCut;
        }

    }

    static class DigitUtils {
        private DigitUtils() {
        }

        public static long round(double x) {
            return (long) (x + 0.5);
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

    static class FastOutput implements AutoCloseable, Closeable {
        private StringBuilder cache = new StringBuilder(10 << 20);
        private final Writer os;

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput println(long c) {
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

    static class ISAP {
        ISAP.Node[] nodes;
        int[] distanceCnt;
        ISAP.Node source;
        ISAP.Node target;
        int nodeNum;
        Map<Long, ISAP.DirectChannel> channelMap = new HashMap();
        Deque<ISAP.Node> deque;
        boolean bfsFlag = false;

        private Collection<ISAP.DirectChannel> getChannels() {
            return channelMap.values();
        }

        private ISAP.DirectChannel addChannel(int src, int dst) {
            ISAP.DirectChannel channel = new ISAP.DirectChannel(nodes[src], nodes[dst], 0, 0);
            nodes[src].channelList.add(channel);
            nodes[dst].channelList.add(channel.getInverse());
            return channel;
        }

        public ISAP.DirectChannel getChannel(int src, int dst) {
            Long id = (((long) src) << 32) | dst;
            ISAP.DirectChannel channel = channelMap.get(id);
            if (channel == null) {
                channel = addChannel(src, dst);
                channelMap.put(id, channel);
            }
            return channel;
        }

        public ISAP(int nodeNum) {
            this.nodeNum = nodeNum;
            deque = new ArrayDeque(nodeNum);
            nodes = new ISAP.Node[nodeNum];
            distanceCnt = new int[nodeNum + 2];
            for (int i = 0; i < nodeNum; i++) {
                ISAP.Node node = new ISAP.Node();
                node.id = i;
                nodes[i] = node;
            }
        }

        public double sendFlow(double flow) {
            bfs();
            double sent = 0;
            while (flow > sent && source.distance < nodeNum) {
                sent += send(source, flow - sent);
            }
            return sent;
        }

        private double send(ISAP.Node node, double flowRemain) {
            if (node == target) {
                return flowRemain;
            }

            double sent = 0;
            int nextDistance = node.distance - 1;
            for (ISAP.Channel channel : node.channelList) {
                double channelRemain = channel.getCapacity() - channel.getFlow();
                ISAP.Node dst = channel.getDst();
                if (channelRemain == 0 || dst.distance != nextDistance) {
                    continue;
                }
                double actuallySend = send(channel.getDst(), Math.min(flowRemain - sent, channelRemain));
                channel.sendFlow(actuallySend);
                sent += actuallySend;
                if (flowRemain == sent) {
                    break;
                }
            }

            if (sent == 0) {
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

            return sent;
        }

        public void setSource(int id) {
            source = nodes[id];
        }

        public void setTarget(int id) {
            target = nodes[id];
        }

        private void bfs() {
            if (bfsFlag) {
                return;
            }
            bfsFlag = true;
            Arrays.fill(distanceCnt, 0);
            deque.clear();

            for (int i = 0; i < nodeNum; i++) {
                nodes[i].distance = nodeNum;
            }

            target.distance = 0;
            deque.addLast(target);

            while (!deque.isEmpty()) {
                ISAP.Node head = deque.removeFirst();
                distanceCnt[head.distance]++;
                for (ISAP.Channel channel : head.channelList) {
                    ISAP.Channel inverse = channel.getInverse();
                    if (inverse.getCapacity() == inverse.getFlow()) {
                        continue;
                    }
                    ISAP.Node dst = channel.getDst();
                    if (dst.distance != nodeNum) {
                        continue;
                    }
                    dst.distance = head.distance + 1;
                    deque.addLast(dst);
                }
            }
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (ISAP.DirectChannel channel : getChannels()) {
                if (channel.getFlow() == 0) {
                    continue;
                }
                builder.append(channel).append('\n');
            }

            for (ISAP.DirectChannel channel : getChannels()) {
                if (channel.getFlow() != 0) {
                    continue;
                }
                builder.append(channel).append('\n');
            }
            return builder.toString();
        }

        public static interface Channel {
            public ISAP.Node getSrc();

            public ISAP.Node getDst();

            public double getCapacity();

            public double getFlow();

            public void sendFlow(double volume);

            public ISAP.Channel getInverse();

        }

        public static class DirectChannel implements ISAP.Channel {
            final ISAP.Node src;
            final ISAP.Node dst;
            final int id;
            double capacity;
            double flow;
            ISAP.Channel inverse;

            public DirectChannel(ISAP.Node src, ISAP.Node dst, int capacity, int id) {
                this.src = src;
                this.dst = dst;
                this.capacity = capacity;
                this.id = id;
                inverse = new ISAP.InverseChannelWrapper(this);
            }

            public void reset(double cap, double flow) {
                this.flow = flow;
                this.capacity = cap;
            }

            public String toString() {
                return String.format("%s--%s/%s-->%s", getSrc(), getFlow(), getCapacity(), getDst());
            }

            public ISAP.Node getSrc() {
                return src;
            }

            public ISAP.Channel getInverse() {
                return inverse;
            }

            public ISAP.Node getDst() {
                return dst;
            }

            public double getCapacity() {
                return capacity;
            }

            public double getFlow() {
                return flow;
            }

            public void sendFlow(double volume) {
                flow += volume;
            }

        }

        public static class InverseChannelWrapper implements ISAP.Channel {
            final ISAP.Channel channel;

            public InverseChannelWrapper(ISAP.Channel channel) {
                this.channel = channel;
            }

            public ISAP.Channel getInverse() {
                return channel;
            }

            public ISAP.Node getSrc() {
                return channel.getDst();
            }

            public ISAP.Node getDst() {
                return channel.getSrc();
            }

            public double getCapacity() {
                return channel.getFlow();
            }

            public double getFlow() {
                return 0;
            }

            public void sendFlow(double volume) {
                channel.sendFlow(-volume);
            }

            public String toString() {
                return String.format("%s--%s/%s-->%s", getSrc(), getFlow(), getCapacity(), getDst());
            }

        }

        public static class Node {
            int id;
            int distance;
            List<ISAP.Channel> channelList = new ArrayList(1);

            public String toString() {
                return "" + id;
            }

        }

    }
}

