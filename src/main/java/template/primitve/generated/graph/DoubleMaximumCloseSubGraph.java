package template.primitve.generated.graph;

import template.graph.DirectedEdge;

import java.util.List;

public interface DoubleMaximumCloseSubGraph {
    /**
     * Get the maximum weighted close sub graph
     * @param g
     * @param weights
     * @param picked
     * @return
     */
    double maximumCloseSubGraph(List<DirectedEdge>[] g, double[] weights, boolean[] picked);
}
