package template.primitve.generated.graph;

import template.graph.DirectedEdge;

public class LongWeightDirectedEdge extends DirectedEdge {
    public long weight;

    public LongWeightDirectedEdge(int to, long weight) {
        super(to);
        this.weight = weight;
    }
}
