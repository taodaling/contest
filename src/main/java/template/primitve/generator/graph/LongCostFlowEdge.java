package template.primitve.generated.graph;

public class LongCostFlowEdge<T extends LongCostFlowEdge<T>> extends LongFlowEdge<T> {
    public long cost;

    public LongCostFlowEdge(int to, long flow, boolean real, long cost) {
        super(to, flow, real);
        this.cost = cost;
    }
}
