package template.primitve.generated.graph;

public class IntegerLRFlowEdge<T extends IntegerLRFlowEdge<T>> extends IntegerFlowEdge<T> implements IntegerLowBound {
    public int low;

    public IntegerLRFlowEdge(int to, int flow, boolean real, int low) {
        super(to, flow, real);
        this.low = low;
    }

    @Override
    public int low() {
        return low;
    }
}
