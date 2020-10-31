package template.primitve.generated.graph;

public class DoubleCostFlowEdge<T extends DoubleCostFlowEdge<T>> extends DoubleFlowEdge<T> {
    public double cost;

    public DoubleCostFlowEdge(int to, double flow, boolean real, double cost) {
        super(to, flow, real);
        this.cost = cost;
    }
}
