package contest;

import graphs.matchings.MaxGeneralWeightedMatchingEVlogV;
import graphs.matchings.MaxGeneralWeightedMatchingV3;
import template.graph.GeneralGraphWeightedMatch;
import template.io.FastInput;
import template.io.FastOutput;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        MaxGeneralWeightedMatchingV3 match = new MaxGeneralWeightedMatchingV3(n);
        match.init(n);
        for (int i = 0; i < m; i++) {
            match.add_edge(in.ri(), in.ri(), in.ri());
        }
        MaxGeneralWeightedMatchingV3.Result result = match.solve();
        out.println(result.totalWeight);
        for (int i = 1; i <= n; i++) {
            int other = match.mateOf(i);
            out.append(other).append(' ');
        }
    }
}
