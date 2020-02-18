package template.primitve.generated.graph;

import template.graph.DirectedEdge;

public class IntegerFlowEdge extends DirectedEdge {
    public int flow;
    public boolean real;
    public IntegerFlowEdge rev;

    public IntegerFlowEdge(int to, int flow, boolean real) {
        super(to);
        this.flow = flow;
        this.real = real;
    }
}
