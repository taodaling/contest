package contest;

import graphs.matchings.MaxGeneralWeightedMatchingV3;
import template.io.FastInput;
import template.io.FastOutput;


public class UOJ81 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        MaxGeneralWeightedMatchingV3 matching = new MaxGeneralWeightedMatchingV3();
        matching.init(n);
        for(int i = 0; i < m; i++){
            matching.add_edge(in.readInt(), in.readInt(), in.readInt());
        }
        long ans = matching.solve().totalWeight;
        out.println(ans);
        for(int i = 1; i <= n; i++){
            out.append(matching.mateOf(i)).append(' ');
        }
    }
}
