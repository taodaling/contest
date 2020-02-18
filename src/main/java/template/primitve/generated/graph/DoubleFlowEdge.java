package template.primitve.generated.graph;

import template.graph.DirectedEdge;

public class DoubleFlowEdge extends DirectedEdge {
    public double flow;
    public boolean real;
    public DoubleFlowEdge rev;

    public DoubleFlowEdge(int to, double flow, boolean real) {
        super(to);
        this.flow = flow;
        this.real = real;
    }
}
