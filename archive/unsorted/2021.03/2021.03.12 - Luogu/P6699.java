package contest;

import template.graph.GeneralGraphWeightedMatch;
import template.graph.MaxGeneralWeightedMatchingV3;
import template.io.FastInput;
import template.io.FastOutput;

public class P6699 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        MaxGeneralWeightedMatchingV3 wm = new MaxGeneralWeightedMatchingV3(n);
        for(int i = 0; i < m; i++){
            wm.add_edge(in.ri() - 1, in.ri() - 1, in.ri());
        }
        out.println(wm.solve().totalWeight);
        for(int i = 0; i < n; i++){
            out.append(wm.mateOf(i) + 1).append(' ');
        }
    }
}
