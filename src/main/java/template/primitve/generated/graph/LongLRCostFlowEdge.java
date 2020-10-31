package template.primitve.generated.graph;

public class LongLRCostFlowEdge<T extends LongLRCostFlowEdge<T>> extends LongCostFlowEdge<T> implements
        LongLowBound {
    public LongLRCostFlowEdge(int to, long flow, boolean real, long cost, long low) {
        super(to, flow, real, cost);
        this.low = low;
    }

    public long low;

    @Override
    public long low() {
        return low;
    }
}
