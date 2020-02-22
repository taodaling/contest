package template.primitve.generated.graph;

public class IntegerCostFlowEdge extends IntegerFlowEdge<IntegerCostFlowEdge> {
    public int cost;

    public IntegerCostFlowEdge(int to, int flow, boolean real, int cost) {
        super(to, flow, real);
        this.cost = cost;
    }
}
