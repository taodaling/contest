package on2020_01.on2020_01_11_Luogo_Online_Judge.P3386__________;



import template.graph.BipartiteMatching;
import template.graph.KMAlgo;
import template.io.FastInput;
import template.io.FastOutput;

public class P3386 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int e = in.readInt();
        BipartiteMatching algo = new BipartiteMatching(n, m, e);
        for(int i = 0; i < e; i++){
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            if(u >= n || v >= m){
                continue;
            }
            algo.addEdge(u, v);
        }
        int ans = algo.maxMatch();
        out.println(ans);
    }
}
