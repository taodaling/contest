package template.primitve.generated.graph;

public class DoubleLRFlowEdge<T extends DoubleLRFlowEdge<T>> extends DoubleFlowEdge<T> implements DoubleLowBound {
    public double low;

    public DoubleLRFlowEdge(int to, double flow, boolean real, double low) {
        super(to, flow, real);
        this.low = low;
    }

    @Override
    public double low() {
        return low;
    }
}
