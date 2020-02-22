package template.primitve.generated.graph;

import java.util.List;

public interface IntegerMaximumFlow {
    int apply(List<IntegerFlowEdge>[] g, int s, int t, int send);
}
