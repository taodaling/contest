package template.primitve.generated.graph;

import java.util.List;

public interface LongMinimumCostFlow {
    /**
     * result[0] means the flow, result[1] means the total cost
     */
    long[] apply(List<LongCostFlowEdge>[] net, int s, int t);
}
