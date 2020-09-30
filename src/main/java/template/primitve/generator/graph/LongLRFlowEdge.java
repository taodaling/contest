package template.primitve.generated.graph;

public class LongLRFlowEdge extends LongFlowEdge<LongLRFlowEdge> {
    public long low;

    public LongLRFlowEdge(int to, long flow, boolean real, long low) {
        super(to, flow, real);
        this.low = low;
    }
}
