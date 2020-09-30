package template.primitve.generated.graph;

public class IntegerLRFlowEdge extends IntegerFlowEdge<IntegerLRFlowEdge> {
    public int low;

    public IntegerLRFlowEdge(int to, int flow, boolean real, int low) {
        super(to, flow, real);
        this.low = low;
    }
}
