package template.graph;

import java.util.List;
import java.util.stream.Collectors;

public class LongMinimumCloseSubGraph {
    private LongISAP isap;
    private int n;
    private long sumOfPositive;

    public LongMinimumCloseSubGraph(long[] weight) {
        this.n = weight.length;
        isap = new LongISAP(n + 2);
        int idOfSrc = n;
        int idOfDst = n + 1;
        isap.setSource(idOfSrc);
        isap.setTarget(idOfDst);

        for (int i = 0; i < n; i++) {
            if (weight[i] > 0) {
                isap.getChannel(idOfSrc, i).reset(weight[i], 0);
                sumOfPositive += weight[i];
            }
            if (weight[i] < 0) {
                isap.getChannel(i, idOfDst).reset(-weight[i], 0);
            }
        }
    }

    public void addDependency(int from, int to) {
        if (!(from >= 0 && from < n && to >= 0 && to < n)) {
            throw new IllegalArgumentException();
        }
        isap.getChannel(from, to).reset((long)1e18, 0);
    }

    private boolean solved;
    private long minCut;
    private List<Integer> subGraph;

    public long solve() {
        if (!solved) {
            minCut = isap.send((long)1e18);
            solved = true;
        }
        return sumOfPositive - minCut;
    }

    public List<Integer> getSubGraph() {
        if (subGraph == null) {
            solve();
            subGraph = isap.getComponentS().stream().map(x -> x.getId()).collect(Collectors.toList());
        }
        return subGraph;
    }
}
