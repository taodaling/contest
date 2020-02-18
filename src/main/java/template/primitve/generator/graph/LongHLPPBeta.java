package template.primitve.generated.graph;

import java.util.List;
import java.util.ListIterator;

public class LongHLPPBeta implements LongMaximumFlow {
    private List<LongFlowEdge>[] g;
    private ListIterator<LongFlowEdge>[] iterators;
    private int[] heights;
    private long[] excess;
    private int vertexNumber;
    Node[] nodes;

    public LongHLPPBeta(int vertexNumber) {
        iterators = new ListIterator[vertexNumber];
        heights = new int[vertexNumber];
        excess = new long[vertexNumber];
        nodes = new Node[vertexNumber];
        for (int i = 0; i < vertexNumber; i++) {
            nodes[i] = new Node();
            nodes[i].val = i;
        }
    }

    private int sink;
    private int source;

    public long send(long flow) {
        init(flow);
        relabelToFront();
        return excess[sink];
    }

    private void relabel(int root) {
        int minHeight = 2 * vertexNumber;
        for (LongFlowEdge c : g[root]) {
            if (c.rev.flow > 0) {
                minHeight = Math.min(minHeight, heights[c.to]);
            }
        }
        heights[root] = minHeight + 1;
    }

    private void discharge(int root) {
        while (excess[root] > 0) {
            if (!iterators[root].hasNext()) {
                relabel(root);
                iterators[root] = g[root].listIterator();
                continue;
            }
            LongFlowEdge c = iterators[root].next();
            long remain;
            if ((remain = c.rev.flow) > 0 && heights[c.to] + 1 == heights[root]) {
                long sent = Math.min(excess[root], remain);
                excess[root] -= sent;
                excess[c.to] += sent;
                LongFlow.send(g, c, sent);
                if (excess[root] == 0) {
                    iterators[root].previous();
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
        for (int i = 0; i < vertexNumber; i++) {
            nodes[i].next = null;
            heights[i] = 0;
            excess[i] = 0;
        }
        heights[source] = vertexNumber;
        long sent = 0;
        for (LongFlowEdge c : g[source]) {
            long newFlow = Math.min(c.rev.flow, flow - sent);
            sent += newFlow;
            LongFlow.send(g, c, newFlow);
            excess[source] -= newFlow;
            excess[c.to] += newFlow;
        }

        for (int i = 0; i < vertexNumber; i++) {
            iterators[i] = g[i].listIterator();
        }
    }

    private static class Node {
        Node next;
        int val;
    }

    @Override
    public long apply(List<LongFlowEdge>[] g, int s, int t) {
        vertexNumber = g.length;
        this.g = g;
        this.source = s;
        this.sink = t;
        return send((long)2e18);
    }
}
