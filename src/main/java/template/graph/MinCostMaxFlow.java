package template.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinCostMaxFlow {
    Node[] nodes;
    Deque<Node> deque;
    Node source;
    Node target;
    int nodeNum;
    final static double INF = 1e50;

    static class ID {
        int src;
        int dst;
        double fee;

        ID(int src, int dst, double fee) {
            this.src = src;
            this.dst = dst;
            this.fee = fee;
        }

        @Override
        public int hashCode() {
            return (int) ((src * 31L + dst) * 31 + Double.doubleToLongBits(fee));
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

    public MinCostMaxFlow(int nodeNum) {
        this.nodeNum = nodeNum;
        nodes = new Node[nodeNum + 1];
        for (int i = 1; i <= nodeNum; i++) {
            nodes[i] = new Node(i);
        }
        deque = new ArrayDeque(nodeNum);
    }

    public void setSource(int id) {
        source = nodes[id];
    }

    public void setTarget(int id) {
        target = nodes[id];
    }

    ID id = new ID(0, 0, 0);

    public DirectFeeChannel getChannel(int src, int dst, double fee) {
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

    private DirectFeeChannel addChannel(int src, int dst, double fee) {
        DirectFeeChannel dfc = new DirectFeeChannel(nodes[src], nodes[dst], fee);
        nodes[src].channelList.add(dfc);
        nodes[dst].channelList.add(dfc.inverse());
        return dfc;
    }

    /**
     * reuslt[0] store how much flow could be sent and result[1] represents the fee
     */
    public double[] send(double flow) {
        double totalFee = 0;
        double totalFlow = 0;

        while (flow > 0) {
            spfa();

            if (target.distance == INF) {
                break;
            }


            double feeSum = target.distance;
            double minFlow = flow;

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

        return new double[]{totalFlow, totalFee};
    }

    private void spfa() {
        for (int i = 1; i <= nodeNum; i++) {
            nodes[i].distance = INF;
            nodes[i].inque = false;
            nodes[i].last = null;
        }

        deque.addLast(source);
        source.distance = 0;
        source.inque = true;
        double sumOfDistance = 0;

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
                double oldDist = dst.distance;
                double newDist = head.distance + channel.getFee();
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

        public double getCapacity();

        public double getFlow();

        public void sendFlow(double volume);

        public FeeChannel inverse();

        public double getFee();
    }

    public static class DirectFeeChannel implements FeeChannel {
        final Node src;
        final Node dst;
        double capacity;
        double flow;
        FeeChannel inverse;
        final double fee;

        @Override
        public double getFee() {
            return fee;
        }

        public DirectFeeChannel(Node src, Node dst, double fee) {
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

        public void reset(double cap, double flow) {
            this.capacity = cap;
            this.flow = flow;
        }

        public void modify(double cap, double flow) {
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
        public double getFee() {
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
        public double getCapacity() {
            return inner.getFlow();
        }

        @Override
        public double getFlow() {
            return 0;
        }

        @Override
        public void sendFlow(double volume) {
            inner.sendFlow(-volume);
        }


        @Override
        public String toString() {
            return String.format("%s--%s/%s-->%s", getSrc(), getFlow(), getCapacity(), getDst());
        }
    }

    public static class Node {
        final int id;
        double distance;
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