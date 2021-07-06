package on2021_07.on2021_07_07_Library_Checker.Matching_on_General_Graph;



import template.graph.RandomGeneralGraphMatching;
import template.io.FastInput;
import template.io.FastOutput;

public class MatchingOnGeneralGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        RandomGeneralGraphMatching gm = new RandomGeneralGraphMatching(n);
        for (int i = 0; i < m; i++) {
            gm.add(in.readInt(), in.readInt());
        }
        int ans = gm.maxMatch();
        out.println(ans);
        for (int i = 0; i < n; i++) {
            int mate = gm.mate(i);
            if (mate < i) {
                continue;
            }
            out.append(i).append(' ').append(mate).println();
        }
    }
}
