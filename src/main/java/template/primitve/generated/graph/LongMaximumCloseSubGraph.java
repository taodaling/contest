package template.primitve.generated.graph;

import template.graph.DirectedEdge;

import java.util.List;

public interface LongMaximumCloseSubGraph {
    /**
     * Get the maximum weighted close sub graph
     * @param g
     * @param weights
     * @param picked
     * @return
     */
    long maximumCloseSubGraph(List<DirectedEdge>[] g, long[] weights, boolean[] picked);
}
