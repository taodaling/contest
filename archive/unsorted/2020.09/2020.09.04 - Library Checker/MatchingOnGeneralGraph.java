package contest;

import template.graph.GeneralGraphMatch;
import template.io.FastInput;
import template.io.FastOutput;

public class MatchingOnGeneralGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        GeneralGraphMatch gm = new GeneralGraphMatch(n);
        for(int i = 0; i < m; i++){
            gm.addEdge(in.readInt(), in.readInt());
        }
        int ans = gm.maxMatch();
        out.println(ans);
        for(int i = 0; i < n; i++){
            int mate = gm.mateOf(i);
            if(mate < i){
                continue;
            }
            out.append(i).append(' ').append(mate).println();
        }
    }
}
