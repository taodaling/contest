package template.primitve.generated.graph;

import java.util.List;

public interface IntegerMinimumCostFlow {
    /**
     * result[0] means the flow, result[1] means the total cost
     */
    int[] apply(List<IntegerCostFlowEdge>[] net, int s, int t);
}
