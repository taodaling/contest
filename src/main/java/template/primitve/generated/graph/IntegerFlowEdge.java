package template.primitve.generated.graph;

import template.graph.DirectedEdge;

public class IntegerFlowEdge<T extends IntegerFlowEdge> extends DirectedEdge {
    public int flow;
    public boolean real;
    public T rev;

    public IntegerFlowEdge(int to, int flow, boolean real) {
        super(to);
        this.flow = flow;
        this.real = real;
    }
}
