package template.primitve.generated.graph;

import java.util.List;

public interface DoubleMinimumCostFlow {
    /**
     * result[0] means the flow, result[1] means the total cost
     */
    double[] apply(List<DoubleCostFlowEdge>[] net, int s, int t);
}
