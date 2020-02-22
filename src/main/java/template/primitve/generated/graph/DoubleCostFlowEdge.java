package template.primitve.generated.graph;

public class DoubleCostFlowEdge extends DoubleFlowEdge<DoubleCostFlowEdge> {
    public double cost;

    public DoubleCostFlowEdge(int to, double flow, boolean real, double cost) {
        super(to, flow, real);
        this.cost = cost;
    }
}
