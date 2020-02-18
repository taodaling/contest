package template.primitve.generated.graph;

import template.graph.DirectedEdge;

public class LongFlowEdge extends DirectedEdge {
    public long flow;
    public boolean real;
    public LongFlowEdge rev;

    public LongFlowEdge(int to, long flow, boolean real) {
        super(to);
        this.flow = flow;
        this.real = real;
    }
}
