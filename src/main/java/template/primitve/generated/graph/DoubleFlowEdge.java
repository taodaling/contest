package template.primitve.generated.graph;

import template.graph.DirectedEdge;

public class DoubleFlowEdge<T extends DoubleFlowEdge> extends DirectedEdge {
    public double flow;
    public boolean real;
    public T rev;

    public DoubleFlowEdge(int to, double flow, boolean real) {
        super(to);
        this.flow = flow;
        this.real = real;
    }
}
