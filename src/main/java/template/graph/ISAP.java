package template.graph;

import template.primitve.generated.IntegerList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ISAP {
    Node[] nodes;
    int[] distanceCnt;
    Node source;
    Node target;
    int nodeNum;
    Map<Long, DirectDoubleChannel> channelMap;
    Deque<Node> deque;
    private double totalFlow = 0;

    public IntegerList getComponentS() {
        IntegerList result = new IntegerList(nodeNum);
        for (int i = 0; i < nodeNum; i++) {
            nodes[i].visited = false;
        }
        deque.addLast(source);
        source.visited = true;
        while (!deque.isEmpty()) {
            Node head = deque.removeFirst();
            result.add(head.id);
            for (DoubleChannel channel : head.channelList) {
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

    private Collection<DirectDoubleChannel> getChannels() {
        return channelMap.values();
    }

    private DirectDoubleChannel addChannel(int src, int dst) {
        DirectDoubleChannel channel = new DirectDoubleChannel(nodes[src], nodes[dst], 0, 0);
        nodes[src].channelList.add(channel);
        nodes[dst].channelList.add(channel.getInverse());
        return channel;
    }

    public DirectDoubleChannel getChannel(int src, int dst) {
        Long id = (((long) src) << 32) | dst;
        DirectDoubleChannel channel = channelMap.get(id);
        if (channel == null) {
            channel = addChannel(src, dst);
            channelMap.put(id, channel);
        }
        return channel;
    }

    public ISAP(int nodeNum) {
        channelMap = new HashMap<>(nodeNum);
        this.nodeNum = nodeNum;
        deque = new ArrayDeque(nodeNum);
        nodes = new Node[nodeNum];
        distanceCnt = new int[nodeNum + 2];
        for (int i = 0; i < nodeNum; i++) {
            Node node = new Node();
            node.id = i;
            nodes[i] = node;
        }
    }

    public double totalFlow() {
        return totalFlow;
    }

    public double sendFlow(double flow) {
        bfs();
        double sent = 0;
        while (flow > sent && source.distance < nodeNum) {
            sent += send(source, flow - sent);
        }
        totalFlow += flow;
        return sent;
    }

    private double send(Node node, double flowRemain) {
        if (node == target) {
            return flowRemain;
        }

        double sent = 0;
        int nextDistance = node.distance - 1;
        for (DoubleChannel channel : node.channelList) {
            double channelRemain = channel.getCapacity() - channel.getFlow();
            Node dst = channel.getDst();
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
        Arrays.fill(distanceCnt, 0);
        deque.clear();

        for (int i = 0; i < nodeNum; i++) {
            nodes[i].distance = nodeNum;
        }

        target.distance = 0;
        deque.addLast(target);

        while (!deque.isEmpty()) {
            Node head = deque.removeFirst();
            distanceCnt[head.distance]++;
            for (DoubleChannel channel : head.channelList) {
                DoubleChannel inverse = channel.getInverse();
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

    public static interface DoubleChannel {
        public Node getSrc();

        public Node getDst();

        public double getCapacity();

        public double getFlow();

        public void sendFlow(double volume);

        public DoubleChannel getInverse();
    }

    public static class DirectDoubleChannel implements DoubleChannel {
        final Node src;
        final Node dst;
        final int id;
        double capacity;
        double flow;
        DoubleChannel inverse;

        public DirectDoubleChannel(Node src, Node dst, int capacity, int id) {
            this.src = src;
            this.dst = dst;
            this.capacity = capacity;
            this.id = id;
            inverse = new InverseDoubleChannelWrapper(this);
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
        public DoubleChannel getInverse() {
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

    public static class InverseDoubleChannelWrapper implements DoubleChannel {
        final DoubleChannel channel;

        public InverseDoubleChannelWrapper(DoubleChannel channel) {
            this.channel = channel;
        }

        @Override
        public DoubleChannel getInverse() {
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
        List<DoubleChannel> channelList = new ArrayList(1);

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "" + id;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (DirectDoubleChannel channel : getChannels()) {
            if (channel.getFlow() == 0) {
                continue;
            }
            builder.append(channel).append('\n');
        }

        for (DirectDoubleChannel channel : getChannels()) {
            if (channel.getFlow() != 0) {
                continue;
            }
            builder.append(channel).append('\n');
        }
        return builder.toString();
    }
}