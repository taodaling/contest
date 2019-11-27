package template.graph;

import template.datastructure.IntDeque;
import template.datastructure.IntList;
import template.datastructure.LongObjectHashMap;
import template.math.DigitUtils;
import template.utils.RevokeIterator;

import java.util.Arrays;
import java.util.Iterator;

public class DinicBeta {
    private MultiWayDeque<Channel> edges;
    private RevokeIterator<Channel>[] iterators;
    private LongObjectHashMap<ChannelImpl> map;
    private IntDeque deque;
    private int[] dists;
    private int vertexNumber;
    private double totalFlow;

    public DinicBeta(int vertexNumber, int expectedChannelNumber,
                     int source, int sink) {
        edges = new MultiWayDeque<>(vertexNumber, expectedChannelNumber * 2);
        iterators = new RevokeIterator[vertexNumber];
        map = new LongObjectHashMap<>(expectedChannelNumber, true);
        this.sink = sink;
        this.source = source;
        deque = new IntDeque(vertexNumber);
        dists = new int[vertexNumber];
        this.vertexNumber = vertexNumber;
    }

    private int sink;
    private int source;

    public IntList getComponentS() {
        Arrays.fill(dists, 0);
        IntList list = new IntList(vertexNumber);
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
                if (dists[dst] != 0) {
                    continue;
                }
                dists[dst] = 1;
                deque.addLast(dst);
            }
        }
        return list;
    }

    public double send(double flow) {
        double totalSent = 0;
        while (flow > totalSent) {
            bfs();
            if (dists[source] == Integer.MAX_VALUE) {
                break;
            }
            for (int i = 0; i < iterators.length; i++) {
                iterators[i] = edges.iterator(i);
            }
            totalSent += send(source, flow - totalSent);
        }
        totalFlow += totalSent;
        return totalSent;
    }

    public double totalFlow(){
        return totalFlow;
    }

    public double getFlowBetween(int a, int b){
        ChannelImpl channel = getChannel(a, b);
        return channel.getFlow();
    }

    private void bfs() {
        Arrays.fill(dists, Integer.MAX_VALUE);
        dists[sink] = 0;
        deque.clear();
        deque.addLast(sink);
        while (!deque.isEmpty()) {
            int head = deque.removeFirst();
            for (Iterator<Channel> iterator = edges.iterator(head);
                 iterator.hasNext(); ) {
                Channel channel = iterator.next().inverse();
                if (channel.getCap() == channel.getFlow()) {
                    continue;
                }
                int src = channel.getSrc();
                if (dists[src] <= dists[head] + 1) {
                    continue;
                }
                dists[src] = dists[head] + 1;
                deque.addLast(src);
            }
        }
    }

    private double send(int root, double flow) {
        if (root == sink) {
            return flow;
        }
        double totalSent = 0;
        for (; iterators[root].hasNext(); ) {
            Channel channel = iterators[root].next();
            int dst = channel.getDst();
            if (channel.getCap() == channel.getFlow() ||
                    dists[dst] + 1 != dists[root]) {
                continue;
            }
            double sent = send(dst, Math.min(flow - totalSent, channel.getCap() - channel.getFlow()));
            totalSent += sent;
            channel.send(sent);
            if (totalSent == flow) {
                if (channel.getCap() > channel.getFlow()) {
                    iterators[root].revoke();
                }
                break;
            }
        }

        return totalSent;
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

    public void expand(int a, int b, double cap) {
        ChannelImpl channel = getChannel(a, b);
        channel.expand(cap);
    }

    private static interface Channel {
        Channel inverse();

        double getFlow();

        double getCap();

        void send(double f);

        int getSrc();

        int getDst();
    }

    private static class ChannelImpl implements Channel {
        private double flow;
        private double cap;
        private InverseChannelWrapper inverse = new InverseChannelWrapper(this);
        private int src;
        private int dst;

        private ChannelImpl(int src, int dst) {
            this.src = src;
            this.dst = dst;
        }

        public double getFlow() {
            return flow;
        }

        public double getCap() {
            return cap;
        }

        @Override
        public Channel inverse() {
            return inverse;
        }

        @Override
        public void send(double f) {
            flow += f;
        }

        public void expand(double c) {
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
            return String.format("%d-%.2f/%.2f->%d", getSrc(), getFlow(), getCap(), getDst());
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
        public double getFlow() {
            return 0;
        }

        @Override
        public double getCap() {
            return channel.getFlow();
        }

        @Override
        public void send(double f) {
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
            return String.format("%d-%.2f/%.2f->%d", getSrc(), getFlow(), getCap(), getDst());
        }
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
