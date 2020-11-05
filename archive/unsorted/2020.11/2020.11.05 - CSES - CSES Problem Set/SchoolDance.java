package contest;

import template.graph.BipartiteMatch;
import template.graph.DinicBipartiteMatch;
import template.io.FastInput;
import template.io.FastOutput;

public class SchoolDance {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        DinicBipartiteMatch bm = new DinicBipartiteMatch(n, m);
        for(int i = 0; i < k; i++){
            bm.addEdge(in.readInt() - 1, in.readInt() - 1);
        }
        int ans = bm.solve();
        out.println(ans);
        for(int i = 0; i < n; i++){
            if(bm.mateOfLeft(i) == -1){
                continue;
            }
            out.append(i + 1).append(' ').append(bm.mateOfLeft(i) + 1).println();
        }
    }
}
