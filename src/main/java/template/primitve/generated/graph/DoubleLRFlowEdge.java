package template.primitve.generated.graph;

public class DoubleLRFlowEdge extends DoubleFlowEdge<DoubleLRFlowEdge> {
    public double low;

    public DoubleLRFlowEdge(int to, double flow, boolean real, double low) {
        super(to, flow, real);
        this.low = low;
    }
}
