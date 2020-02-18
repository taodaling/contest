package template.primitve.generated.graph;

import template.graph.DirectedEdge;

public class DoubleWeightDirectedEdge extends DirectedEdge {
    public double weight;

    public DoubleWeightDirectedEdge(int to, double weight) {
        super(to);
        this.weight = weight;
    }
}
