package on2021_07.on2021_07_12_Library_Checker.General_Weighted_Matching;



import template.graph.MaxGeneralWeightedMatchingV3;
import template.io.FastInput;
import template.io.FastOutput;

public class GeneralWeightedMatching {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        MaxGeneralWeightedMatchingV3 wm = new MaxGeneralWeightedMatchingV3(n);
        wm.init(n);
        for(int i = 0; i < m; i++){
            int a = in.ri();
            int b = in.ri();
            long w = in.rl();
            wm.add_edge(a, b, w);
        }
        MaxGeneralWeightedMatchingV3.Result res = wm.solve();
        out.println(res.matches);
        out.println(res.totalWeight);
        for(int i = 0; i < n; i++){
            if(wm.mateOf(i) < i){
                continue;
            }
            out.append(i).append(' ').append(wm.mateOf(i)).println();
        }
    }
}
