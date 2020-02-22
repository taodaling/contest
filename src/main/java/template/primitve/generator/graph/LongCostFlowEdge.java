package template.primitve.generated.graph;

public class LongCostFlowEdge extends LongFlowEdge<LongCostFlowEdge> {
    public long cost;

    public LongCostFlowEdge(int to, long flow, boolean real, long cost) {
        super(to, flow, real);
        this.cost = cost;
    }
}
