package template.primitve.generated.graph;

public class DoubleLRCostFlowEdge<T extends DoubleLRCostFlowEdge<T>> extends DoubleCostFlowEdge<T> implements
        DoubleLowBound {
    public DoubleLRCostFlowEdge(int to, double flow, boolean real, double cost, double low) {
        super(to, flow, real, cost);
        this.low = low;
    }

    public double low;

    @Override
    public double low() {
        return low;
    }
}
