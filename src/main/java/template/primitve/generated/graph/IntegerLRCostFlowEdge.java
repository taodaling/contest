package template.primitve.generated.graph;

public class IntegerLRCostFlowEdge<T extends IntegerLRCostFlowEdge<T>> extends IntegerCostFlowEdge<T> implements
        IntegerLowBound {
    public IntegerLRCostFlowEdge(int to, int flow, boolean real, int cost, int low) {
        super(to, flow, real, cost);
        this.low = low;
    }

    public int low;

    @Override
    public int low() {
        return low;
    }
}
