package template.graph;

import java.util.*;

public class LongMinCostMaxFlow {
    Node[] nodes;
    Deque<Node> deque;
    Node source;
    Node target;
    int nodeNum;
    final static long INF = (long) 1e18;

    private static class ID {
        int src;
        int dst;
        long fee;

        ID(int src, int dst, long fee) {
            this.src = src;
            this.dst = dst;
            this.fee = fee;
        }

        @Override
        public int hashCode() {
            return (int) ((src * 31L + dst) * 31 + Long.hashCode(fee));
        }

        @Override
        public boolean equals(Object obj) {
            ID other = (ID) obj;
            return src == other.src &&
                    dst == other.dst &&
                    fee == other.fee;
        }
    }

    Map<ID, DirectFeeChannel> channelMap = new HashMap();

    public LongMinCostMaxFlow(int nodeNum, int s, int t) {
        this.nodeNum = nodeNum;
        nodes = new Node[nodeNum];
        for (int i = 0; i < nodeNum; i++) {
            nodes[i] = new Node(i);
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

    ID id = new ID(0, 0, 0);

    public DirectFeeChannel getChannel(int src, int dst, long fee) {
        id.src = src;
        id.dst = dst;
        id.fee = fee;
        DirectFeeChannel channel = channelMap.get(id);
        if (channel == null) {
            channel = addChannel(src, dst, fee);
            channelMap.put(new ID(src, dst, fee), channel);
        }
        return channel;
    }

    private DirectFeeChannel addChannel(int src, int dst, long fee) {
        DirectFeeChannel dfc = new DirectFeeChannel(nodes[src], nodes[dst], fee);
        nodes[src].channelList.add(dfc);
        nodes[dst].channelList.add(dfc.inverse());
        return dfc;
    }

    /**
     * reuslt[0] store how much flow could be sent and result[1] represents the fee
     */
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

            Node trace = target;
            while (trace != source) {
                FeeChannel last = trace.last;
                minFlow = Math.min(minFlow, last.getCapacity() - last.getFlow());
                trace = last.getSrc();
            }

            flow -= minFlow;

            trace = target;
            while (trace != source) {
                FeeChannel last = trace.last;
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
            Node head = deque.removeFirst();
            if (head.distance * (deque.size() + 1) > sumOfDistance) {
                deque.addLast(head);
                continue;
            }
            sumOfDistance -= head.distance;
            head.inque = false;
            for (FeeChannel channel : head.channelList) {
                if (channel.getFlow() == channel.getCapacity()) {
                    continue;
                }
                Node dst = channel.getDst();
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

    public static interface FeeChannel {
        public Node getSrc();

        public Node getDst();

        public long getCapacity();

        public long getFlow();

        public void sendFlow(long volume);

        public FeeChannel inverse();

        public long getFee();
    }

    public static class DirectFeeChannel implements FeeChannel {
        final Node src;
        final Node dst;
        long capacity;
        long flow;
        FeeChannel inverse;
        final long fee;

        @Override
        public long getFee() {
            return fee;
        }

        public DirectFeeChannel(Node src, Node dst, long fee) {
            this.src = src;
            this.dst = dst;
            this.fee = fee;
            inverse = new InverseFeeChannelWrapper(this);
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
        public FeeChannel inverse() {
            return inverse;
        }

        @Override
        public Node getDst() {
            return dst;
        }

        @Override
        public long getCapacity() {
            return capacity;
        }

        @Override
        public long getFlow() {
            return flow;
        }

        @Override
        public void sendFlow(long volume) {
            flow += volume;
        }

        public void reset(long cap, long flow) {
            this.capacity = cap;
            this.flow = flow;
        }

        public void modify(long cap, long flow) {
            this.capacity += cap;
            this.flow += flow;
        }
    }

    public static class InverseFeeChannelWrapper implements FeeChannel {
        final FeeChannel inner;

        public InverseFeeChannelWrapper(FeeChannel inner) {
            this.inner = inner;
        }

        @Override
        public long getFee() {
            return -inner.getFee();
        }

        @Override
        public FeeChannel inverse() {
            return inner;
        }


        @Override
        public Node getSrc() {
            return inner.getDst();
        }

        @Override
        public Node getDst() {
            return inner.getSrc();
        }

        @Override
        public long getCapacity() {
            return inner.getFlow();
        }

        @Override
        public long getFlow() {
            return 0;
        }

        @Override
        public void sendFlow(long volume) {
            inner.sendFlow(-volume);
        }


        @Override
        public String toString() {
            return String.format("%s--%s/%s-->%s", getSrc(), getFlow(), getCapacity(), getDst());
        }
    }

    public static class Node {
        final int id;
        long distance;
        boolean inque;
        FeeChannel last;
        List<FeeChannel> channelList = new ArrayList(1);

        public Node(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "" + id;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (DirectFeeChannel channel : channelMap.values()) {
            if (channel.getFlow() > 0) {
                builder.append(channel).append('\n');
            }
        }
        return builder.toString();
    }
}
