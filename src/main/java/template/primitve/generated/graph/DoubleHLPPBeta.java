package template.primitve.generated.graph;

import java.util.List;
import java.util.ListIterator;

public class DoubleHLPPBeta implements DoubleMaximumFlow {
    private List<DoubleFlowEdge>[] g;
    private ListIterator<DoubleFlowEdge>[] iterators;
    private int[] heights;
    private double[] excess;
    private int vertexNumber;
    Node[] nodes;

    public DoubleHLPPBeta(int vertexNumber) {
        iterators = new ListIterator[vertexNumber];
        heights = new int[vertexNumber];
        excess = new double[vertexNumber];
        nodes = new Node[vertexNumber];
        for (int i = 0; i < vertexNumber; i++) {
            nodes[i] = new Node();
            nodes[i].val = i;
        }
    }

    private int sink;
    private int source;

    public double send(double flow) {
        init(flow);
        relabelToFront();
        return excess[sink];
    }

    private void relabel(int root) {
        int minHeight = 2 * vertexNumber;
        for (DoubleFlowEdge c : g[root]) {
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
            DoubleFlowEdge c = iterators[root].next();
            double remain;
            if ((remain = c.rev.flow) > 0 && heights[c.to] + 1 == heights[root]) {
                double sent = Math.min(excess[root], remain);
                excess[root] -= sent;
                excess[c.to] += sent;
                DoubleFlow.send(c, sent);
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

    private void init(double flow) {
        for (int i = 0; i < vertexNumber; i++) {
            nodes[i].next = null;
            heights[i] = 0;
            excess[i] = 0;
        }
        heights[source] = vertexNumber;
        double sent = 0;
        for (DoubleFlowEdge c : g[source]) {
            double newFlow = Math.min(c.rev.flow, flow - sent);
            sent += newFlow;
            DoubleFlow.send(c, newFlow);
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
    public double apply(List<DoubleFlowEdge>[] g, int s, int t, double send) {
        vertexNumber = g.length;
        this.g = g;
        this.source = s;
        this.sink = t;
        return send(send);
    }
}
