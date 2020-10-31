package template.primitve.generated.graph;

public class LongLRFlowEdge<T extends LongLRFlowEdge<T>> extends LongFlowEdge<T> implements LongLowBound {
    public long low;

    public LongLRFlowEdge(int to, long flow, boolean real, long low) {
        super(to, flow, real);
        this.low = low;
    }

    @Override
    public long low() {
        return low;
    }
}
