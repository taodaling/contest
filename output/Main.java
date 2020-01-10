import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.util.HashMap;
import java.util.Deque;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.util.Map;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) throws Exception {
        new TaskAdapter().run();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            LOJ102 solver = new LOJ102();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class LOJ102 {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            LongMinCostMaxFlow mcmf = new LongMinCostMaxFlow(n + 1, 1, n);
            for (int i = 0; i < m; i++) {
                int s = in.readInt();
                int t = in.readInt();
                int c = in.readInt();
                int w = in.readInt();
                mcmf.getChannel(s, t, w).modify(c, 0);
            }
            long[] ans = mcmf.send((long) 1e18);
            out.append(ans[0]).append(' ').append(ans[1]).println();
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

    static class LongMinCostMaxFlow {
        LongMinCostMaxFlow.Node[] nodes;
        Deque<LongMinCostMaxFlow.Node> deque;
        LongMinCostMaxFlow.Node source;
        LongMinCostMaxFlow.Node target;
        int nodeNum;
        final static long INF = (long) 1e18;
        Map<LongMinCostMaxFlow.ID, LongMinCostMaxFlow.DirectFeeChannel> channelMap = new HashMap();
        LongMinCostMaxFlow.ID id = new LongMinCostMaxFlow.ID(0, 0, 0);

        public LongMinCostMaxFlow(int nodeNum, int s, int t) {
            this.nodeNum = nodeNum;
            nodes = new LongMinCostMaxFlow.Node[nodeNum];
            for (int i = 0; i < nodeNum; i++) {
                nodes[i] = new LongMinCostMaxFlow.Node(i);
            }
            deque = new ArrayDeque(nodeNum);
            setSource(s);
            setTarget(t);
        }

        private void setSource(int id) {
            source = nodes[id];
        }

        private void setTarget(int id) {
            target = nodes[id];
        }

        public LongMinCostMaxFlow.DirectFeeChannel getChannel(int src, int dst, long fee) {
            id.src = src;
            id.dst = dst;
            id.fee = fee;
            LongMinCostMaxFlow.DirectFeeChannel channel = channelMap.get(id);
            if (channel == null) {
                channel = addChannel(src, dst, fee);
                channelMap.put(new LongMinCostMaxFlow.ID(src, dst, fee), channel);
            }
            return channel;
        }

        private LongMinCostMaxFlow.DirectFeeChannel addChannel(int src, int dst, long fee) {
            LongMinCostMaxFlow.DirectFeeChannel dfc = new LongMinCostMaxFlow.DirectFeeChannel(nodes[src], nodes[dst], fee);
            nodes[src].channelList.add(dfc);
            nodes[dst].channelList.add(dfc.inverse());
            return dfc;
        }

        public long[] send(long flow) {
            long totalFee = 0;
            long totalFlow = 0;

            while (flow > 0) {
                spfa();

                if (target.distance == INF) {
                    break;
                }


                long feeSum = target.distance;
                long minFlow = flow;

                LongMinCostMaxFlow.Node trace = target;
                while (trace != source) {
                    LongMinCostMaxFlow.FeeChannel last = trace.last;
                    minFlow = Math.min(minFlow, last.getCapacity() - last.getFlow());
                    trace = last.getSrc();
                }

                flow -= minFlow;

                trace = target;
                while (trace != source) {
                    LongMinCostMaxFlow.FeeChannel last = trace.last;
                    last.sendFlow(minFlow);
                    trace = last.getSrc();
                }

                totalFee += feeSum * minFlow;
                totalFlow += minFlow;
            }

            return new long[]{totalFlow, totalFee};
        }

        private void spfa() {
            for (int i = 0; i < nodeNum; i++) {
                nodes[i].distance = INF;
                nodes[i].inque = false;
                nodes[i].last = null;
            }

            deque.addLast(source);
            source.distance = 0;
            source.inque = true;
            long sumOfDistance = 0;

            while (!deque.isEmpty()) {
                LongMinCostMaxFlow.Node head = deque.removeFirst();
                if (head.distance * (deque.size() + 1) > sumOfDistance) {
                    deque.addLast(head);
                    continue;
                }
                sumOfDistance -= head.distance;
                head.inque = false;
                for (LongMinCostMaxFlow.FeeChannel channel : head.channelList) {
                    if (channel.getFlow() == channel.getCapacity()) {
                        continue;
                    }
                    LongMinCostMaxFlow.Node dst = channel.getDst();
                    long oldDist = dst.distance;
                    long newDist = head.distance + channel.getFee();
                    if (oldDist <= newDist) {
                        continue;
                    }
                    dst.distance = newDist;
                    dst.last = channel;
                    if (dst.inque) {
                        sumOfDistance -= oldDist;
                        sumOfDistance += newDist;
                        continue;
                    }
                    if (!deque.isEmpty() && deque.peekFirst().distance < dst.distance) {
                        deque.addFirst(dst);
                    } else {
                        deque.addLast(dst);
                    }
                    dst.inque = true;
                    sumOfDistance += newDist;
                }
            }
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (LongMinCostMaxFlow.DirectFeeChannel channel : channelMap.values()) {
                if (channel.getFlow() > 0) {
                    builder.append(channel).append('\n');
                }
            }
            return builder.toString();
        }

        private static class ID {
            int src;
            int dst;
            long fee;

            ID(int src, int dst, long fee) {
                this.src = src;
                this.dst = dst;
                this.fee = fee;
            }

            public int hashCode() {
                return (int) ((src * 31L + dst) * 31 + Long.hashCode(fee));
            }

            public boolean equals(Object obj) {
                LongMinCostMaxFlow.ID other = (LongMinCostMaxFlow.ID) obj;
                return src == other.src &&
                        dst == other.dst &&
                        fee == other.fee;
            }

        }

        public static interface FeeChannel {
            public LongMinCostMaxFlow.Node getSrc();

            public LongMinCostMaxFlow.Node getDst();

            public long getCapacity();

            public long getFlow();

            public void sendFlow(long volume);

            public long getFee();

        }

        public static class DirectFeeChannel implements LongMinCostMaxFlow.FeeChannel {
            final LongMinCostMaxFlow.Node src;
            final LongMinCostMaxFlow.Node dst;
            long capacity;
            long flow;
            LongMinCostMaxFlow.FeeChannel inverse;
            final long fee;

            public long getFee() {
                return fee;
            }

            public DirectFeeChannel(LongMinCostMaxFlow.Node src, LongMinCostMaxFlow.Node dst, long fee) {
                this.src = src;
                this.dst = dst;
                this.fee = fee;
                inverse = new LongMinCostMaxFlow.InverseFeeChannelWrapper(this);
            }

            public String toString() {
                return String.format("%s--%s/%s-->%s", getSrc(), getFlow(), getCapacity(), getDst());
            }

            public LongMinCostMaxFlow.Node getSrc() {
                return src;
            }

            public LongMinCostMaxFlow.FeeChannel inverse() {
                return inverse;
            }

            public LongMinCostMaxFlow.Node getDst() {
                return dst;
            }

            public long getCapacity() {
                return capacity;
            }

            public long getFlow() {
                return flow;
            }

            public void sendFlow(long volume) {
                flow += volume;
            }

            public void modify(long cap, long flow) {
                this.capacity += cap;
                this.flow += flow;
            }

        }

        public static class InverseFeeChannelWrapper implements LongMinCostMaxFlow.FeeChannel {
            final LongMinCostMaxFlow.FeeChannel inner;

            public InverseFeeChannelWrapper(LongMinCostMaxFlow.FeeChannel inner) {
                this.inner = inner;
            }

            public long getFee() {
                return -inner.getFee();
            }

            public LongMinCostMaxFlow.Node getSrc() {
                return inner.getDst();
            }

            public LongMinCostMaxFlow.Node getDst() {
                return inner.getSrc();
            }

            public long getCapacity() {
                return inner.getFlow();
            }

            public long getFlow() {
                return 0;
            }

            public void sendFlow(long volume) {
                inner.sendFlow(-volume);
            }

            public String toString() {
                return String.format("%s--%s/%s-->%s", getSrc(), getFlow(), getCapacity(), getDst());
            }

        }

        public static class Node {
            final int id;
            long distance;
            boolean inque;
            LongMinCostMaxFlow.FeeChannel last;
            List<LongMinCostMaxFlow.FeeChannel> channelList = new ArrayList(1);

            public Node(int id) {
                this.id = id;
            }

            public String toString() {
                return "" + id;
            }

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

        public FastOutput println() {
            cache.append('\n');
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
}

