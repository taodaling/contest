package template.primitve.generated.graph;

import java.util.List;

public interface IntegerMaximumCloseSubGraph {
    /**
     * Get the maximum weighted close sub graph
     * for edge e in g, w(e) means you can spent w(e) to rent e.to or just buy it, set inf to forbidden the rent operation
     */
    int maximumCloseSubGraph(List<IntegerWeightDirectedEdge>[] g, int[] weights, boolean[] picked);
}
