package on2020_01.on2020_01_11_POJ___USACO_2005_November_Gold.Asteroids;



import template.graph.BipartiteMatching;
import template.graph.KMAlgo;
import template.io.FastInput;
import template.io.FastOutput;

public class Asteroids {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        KMAlgo algo = new KMAlgo(n, n);
        for (int i = 0; i < k; i++) {
            int x = in.readInt() - 1;
            int y = in.readInt() - 1;
            algo.addEdge(x, y, true);
        }
        int ans = 0;
        for(int i = 0; i < n; i++){
            if(algo.matchLeft(i)){
                ans++;
            }
        }
        out.println(ans);
    }
}
