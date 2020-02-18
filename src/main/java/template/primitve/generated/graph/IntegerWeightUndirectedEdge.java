package template.primitve.generated.graph;

public class IntegerWeightUndirectedEdge extends IntegerWeightDirectedEdge{
    public IntegerWeightUndirectedEdge rev;

    public IntegerWeightUndirectedEdge(int to, int weight) {
        super(to, weight);
    }
}
