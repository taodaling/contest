package template.graph;

import template.datastructure.*;
import template.math.DigitUtils;
import template.primitve.generated.IntegerDequeImpl;
import template.primitve.generated.IntegerList;
import template.primitve.generated.LongObjectHashMap;
import template.utils.RevokeIterator;

import java.util.Arrays;
import java.util.Iterator;

public class LongHLPPBeta {
    private MultiWayStack<Channel> edges;
    private RevokeIterator<Channel>[] iterators;
    private LongObjectHashMap<ChannelImpl> map;
    private int[] heights;
    private long[] excess;
    private IntegerDequeImpl deque;
    private int vertexNumber;
    private long totalFlow;
    Node[] nodes;

    public LongHLPPBeta(int vertexNumber, int expectedChannelNumber,
                        int source, int sink) {
        edges = new MultiWayStack<>(vertexNumber, expectedChannelNumber * 2);
        iterators = new RevokeIterator[vertexNumber];
        map = new LongObjectHashMap<>(expectedChannelNumber, true);
        this.sink = sink;
        this.source = source;
        deque = new IntegerDequeImpl(vertexNumber);
        heights = new int[vertexNumber];
        excess = new long[vertexNumber];
        nodes = new Node[vertexNumber];
        for (int i = 0; i < vertexNumber; i++) {
            nodes[i] = new Node();
            nodes[i].val = i;
        }
        this.vertexNumber = vertexNumber;
    }

    private int sink;
    private int source;

    public IntegerList getComponentS() {
        Arrays.fill(heights, 0);
        IntegerList list = new IntegerList(vertexNumber);
        deque.addLast(source);
        while (!deque.isEmpty()) {
            int head = deque.removeFirst();
            list.add(head);
            for (Iterator<Channel> iterator = edges.iterator(head); iterator.hasNext(); ) {
                Channel channel = iterator.next();
                if (channel.getFlow() == channel.getCap()) {
                    continue;
                }
                int dst = channel.getDst();
                if (heights[dst] != 0) {
                    continue;
                }
                heights[dst] = 1;
                deque.addLast(dst);
            }
        }
        return list;
    }

    public long send(long flow) {
        init(flow);
        relabelToFront();
        totalFlow += excess[sink];
        return excess[sink];
    }

    private void relabel(int root) {
        int minHeight = 2 * vertexNumber;
        for (Iterator<Channel> iterator = edges.iterator(root); iterator.hasNext(); ) {
            Channel c = iterator.next();
            if (c.getCap() > c.getFlow()) {
                minHeight = Math.min(minHeight, heights[c.getDst()]);
            }
        }
        heights[root] = minHeight + 1;
    }

    private void discharge(int root) {
        while (excess[root] > 0) {
            if (!iterators[root].hasNext()) {
                relabel(root);
                iterators[root] = edges.iterator(root);
                continue;
            }
            Channel c = iterators[root].next();
            if (c.getCap() > c.getFlow() && heights[c.getDst()] + 1 == heights[root]) {
                long sent = Math.min(excess[root], c.getCap() - c.getFlow());
                excess[root] -= sent;
                excess[c.getDst()] += sent;
                c.send(sent);
                if (excess[root] == 0) {
                    iterators[root].revoke();
                    return;
                }
            }
        }
    }

    private void relabelToFront() {
        Node head = null;
        for (int i = 0; i < vertexNumber; i++) {
            if (i == source || i == sink) {
                continue;
            }
            nodes[i].next = head;
            head = nodes[i];
        }

        Node trace = head;
        Node pre = null;
        while (trace != null) {
            int i = trace.val;
            int oldHeight = heights[i];
            discharge(i);
            if (heights[i] != oldHeight) {
                if (pre != null) {
                    pre.next = trace.next;
                    trace.next = head;
                    head = trace;
                }
                trace = head;
            }
            pre = trace;
            trace = trace.next;
        }
    }

    private void init(long flow) {
        Arrays.fill(heights, 0);
        heights[source] = vertexNumber;

        long sent = 0;
        excess[source] = 0;
        excess[sink] = 0;
        for (Iterator<Channel> iterator = edges.iterator(source); iterator.hasNext(); ) {
            Channel c = iterator.next();
            long newFlow = Math.min(c.getCap() - c.getFlow(), flow - sent);
            sent += newFlow;
            c.send(newFlow);
            excess[source] -= newFlow;
            excess[c.getDst()] += newFlow;
        }

        for (int i = 0; i < vertexNumber; i++) {
            iterators[i] = edges.iterator(i);
        }
    }

    public long totalFlow() {
        return totalFlow;
    }

    private ChannelImpl getChannel(int a, int b) {
        long id = DigitUtils.asLong(a, b);
        ChannelImpl channel = map.get(id);
        if (channel == null) {
            channel = new ChannelImpl(a, b);
            edges.addLast(a, channel);
            edges.addLast(b, channel.inverse());
            map.put(id, channel);
        }
        return channel;
    }

    public void expand(int a, int b, long cap) {
        ChannelImpl channel = getChannel(a, b);
        channel.expand(cap);
    }

    public long getFlowBetween(int a, int b) {
        ChannelImpl channel = getChannel(a, b);
        return channel.getFlow();
    }

    private static interface Channel {
        Channel inverse();

        long getFlow();

        long getCap();

        void send(long f);

        int getSrc();

        int getDst();
    }

    private static class ChannelImpl implements Channel {
        private long flow;
        private long cap;
        private InverseChannelWrapper inverse = new InverseChannelWrapper(this);
        private int src;
        private int dst;

        private ChannelImpl(int src, int dst) {
            this.src = src;
            this.dst = dst;
        }

        public long getFlow() {
            return flow;
        }

        public long getCap() {
            return cap;
        }

        @Override
        public Channel inverse() {
            return inverse;
        }

        @Override
        public void send(long f) {
            flow += f;
        }

        public void expand(long c) {
            cap += c;
        }

        public int getSrc() {
            return src;
        }

        public int getDst() {
            return dst;
        }

        @Override
        public String toString() {
            return String.format("%d-%d/%d->%d", getSrc(), getFlow(), getCap(), getDst());
        }
    }

    private static class InverseChannelWrapper implements Channel {
        private Channel channel;

        private InverseChannelWrapper(Channel channel) {
            this.channel = channel;
        }

        @Override
        public Channel inverse() {
            return channel;
        }

        @Override
        public long getFlow() {
            return 0;
        }

        @Override
        public long getCap() {
            return channel.getFlow();
        }

        @Override
        public void send(long f) {
            channel.send(-f);
        }

        @Override
        public int getSrc() {
            return channel.getDst();
        }

        @Override
        public int getDst() {
            return channel.getSrc();
        }

        @Override
        public String toString() {
            return String.format("%d-%d/%d->%d", getSrc(), getFlow(), getCap(), getDst());
        }
    }

    private static class Node {
        Node next;
        int val;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < vertexNumber; i++) {
            for (Iterator<Channel> iterator = edges.iterator(i); iterator.hasNext(); ) {
                Channel channel = iterator.next();
                builder.append(channel).append('\n');
            }
        }
        return builder.toString();
    }
}
