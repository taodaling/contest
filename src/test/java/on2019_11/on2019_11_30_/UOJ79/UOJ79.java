package on2019_11.on2019_11_30_.UOJ79;



import graphs.matchings.MaxGeneralMatchingEsqrtV;
import graphs.matchings.MaxGeneralMatchingRandomized;
import graphs.matchings.MaxGeneralMatchingV3;
import template.graph.EdmondBlossom;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class UOJ79 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        EdmondBlossom blossom = new EdmondBlossom(n);
        for (int i = 0; i < m; i++) {
            blossom.addEdge(in.readInt(), in.readInt());
        }
        out.println(blossom.maxMatch());
        for(int i = 1; i <= n; i++){
            out.append(blossom.mateOf(i)).append(' ');
        }
    }
}
