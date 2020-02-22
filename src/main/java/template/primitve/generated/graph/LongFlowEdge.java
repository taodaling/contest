package template.primitve.generated.graph;

import template.graph.DirectedEdge;

public class LongFlowEdge<T extends LongFlowEdge> extends DirectedEdge {
    public long flow;
    public boolean real;
    public T rev;

    public LongFlowEdge(int to, long flow, boolean real) {
        super(to);
        this.flow = flow;
        this.real = real;
    }
}
