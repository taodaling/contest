package template.primitve.generated.graph;

import template.graph.DirectedEdge;

import java.util.List;

public interface IntegerMaximumCloseSubGraph {
    /**
     * Get the maximum weighted close sub graph
     * @param g
     * @param weights
     * @param picked
     * @return
     */
    int maximumCloseSubGraph(List<DirectedEdge>[] g, int[] weights, boolean[] picked);
}
