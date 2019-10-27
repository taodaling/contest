package template;

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

    private Collection<DirectChannel> getChannels() {
        return channelMap.values();
    }

    private DirectChannel addChannel(int src, int dst) {
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

    private double send(Node node, double flowRemain) {
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

    private void bfs() {
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