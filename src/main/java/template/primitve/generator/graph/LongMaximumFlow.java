package template.primitve.generated.graph;

import java.util.List;

public interface LongMaximumFlow {
    long apply(List<LongFlowEdge>[] g, int s, int t, long send);
}
