package template.primitve.generated.graph;

import template.graph.DirectedEdge;

public class IntegerWeightDirectedEdge extends DirectedEdge {
    public int weight;

    public IntegerWeightDirectedEdge(int to, int weight) {
        super(to);
        this.weight = weight;
    }
}
