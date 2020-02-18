package template.graph;

import template.primitve.generated.datastructure.IntegerList;

import java.util.*;

public class LongISAP {
    Node[] nodes;
    int[] distanceCnt;
    Node source;
    Node target;
    int nodeNum;
    Map<Long, DirectLongChannel> channelMap;
    Deque<Node> deque;

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
            for (LongChannel channel : head.channelList) {
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

    private Collection<DirectLongChannel> getChannels() {
        return channelMap.values();
    }

    private DirectLongChannel addChannel(int src, int dst) {
        DirectLongChannel channel = new DirectLongChannel(nodes[src], nodes[dst], 0, 0);
        nodes[src].channelList.add(channel);
        nodes[dst].channelList.add(channel.getInverse());
        return channel;
    }

    public DirectLongChannel getChannel(int src, int dst) {
        Long id = (((long) src) << 32) | dst;
        DirectLongChannel channel = channelMap.get(id);
        if (channel == null) {
            channel = addChannel(src, dst);
            channelMap.put(id, channel);
        }
        return channel;
    }

    public LongISAP(int nodeNum) {
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

    long totalFlow;

    public long send(long flow) {
        long sent = 0;
        bfs();
        while (flow > sent && source.distance < nodeNum) {
            sent += send(source, flow - sent);
        }
        totalFlow += sent;
        return sent;
    }

    public long totalFlow() {
        return totalFlow;
    }

    private long send(Node node, long flowRemain) {
        if (node == target) {
            return flowRemain;
        }

        long sent = 0;
        int nextDistance = node.distance - 1;
        for (LongChannel channel : node.channelList) {
            long channelRemain = channel.getCapacity() - channel.getFlow();
            Node dst = channel.getDst();
            if (channelRemain == 0 || dst.distance != nextDistance) {
                continue;
            }
            long actuallySend = send(channel.getDst(), Math.min(flowRemain - sent, channelRemain));
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
            for (LongChannel channel : head.channelList) {
                LongChannel inverse = channel.getInverse();
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

    public static interface LongChannel {
        public Node getSrc();

        public Node getDst();

        public long getCapacity();

        public long getFlow();

        public void sendFlow(long volume);

        public LongChannel getInverse();
    }

    public static class DirectLongChannel implements LongChannel {
        final Node src;
        final Node dst;
        final int id;
        long capacity;
        long flow;
        LongChannel inverse;

        public DirectLongChannel(Node src, Node dst, int capacity, int id) {
            this.src = src;
            this.dst = dst;
            this.capacity = capacity;
            this.id = id;
            inverse = new InverseLongChannelWrapper(this);
        }

        public void reset(long cap, long flow) {
            this.flow = flow;
            this.capacity = cap;
        }

        public void modify(long cap, long flow) {
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
        public LongChannel getInverse() {
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


    }

    public static class InverseLongChannelWrapper implements LongChannel {
        final LongChannel channel;

        public InverseLongChannelWrapper(LongChannel channel) {
            this.channel = channel;
        }

        @Override
        public LongChannel getInverse() {
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
        public long getCapacity() {
            return channel.getFlow();
        }

        @Override
        public long getFlow() {
            return 0;
        }

        @Override
        public void sendFlow(long volume) {
            channel.sendFlow(-volume);
        }


        @Override
        public String toString() {
            return String.format("%s--%s/%s-->%s", getSrc(), getFlow(), getCapacity(), getDst());
        }
    }

    private static class Node {
        int id;
        int distance;
        boolean visited;
        List<LongChannel> channelList = new ArrayList(1);

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
        for (DirectLongChannel channel : getChannels()) {
            if (channel.getFlow() == 0) {
                continue;
            }
            builder.append(channel).append('\n');
        }

        for (DirectLongChannel channel : getChannels()) {
            if (channel.getFlow() != 0) {
                continue;
            }
            builder.append(channel).append('\n');
        }
        return builder.toString();
    }
}
