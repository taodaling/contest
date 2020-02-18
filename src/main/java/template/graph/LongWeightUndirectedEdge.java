package template.graph;

public class LongWeightUndirectedEdge extends LongWeightDirectedEdge{
    public LongWeightUndirectedEdge rev;

    public LongWeightUndirectedEdge(int to, long weight) {
        super(to, weight);
    }
}
