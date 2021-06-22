package on2021_06.on2021_06_20_Codeforces___Codeforces_LATOKEN_Round_1__Div__1___Div__2_.C__Little_Alawn_s_Puzzle;



import template.datastructure.DSU;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Power;

import java.util.List;

public class CLittleAlawnsPuzzle {
    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int[] b = in.ri(n);
        for (int i = 0; i < n; i++) {
            a[i]--;
            b[i]--;
        }
        List<Integer>[] g = Graph.createGraph(n);
        for (int i = 0; i < n; i++) {
            g[a[i]].add(i);
            g[b[i]].add(i);
        }
        DSU dsu = new DSU(n);
        dsu.init();
        for (int i = 0; i < n; i++) {
            dsu.merge(g[i].get(0), g[i].get(1));
        }
        int scc = 0;
        for(int i = 0; i < n; i++){
            if(dsu.find(i) == i){
                scc++;
            }
        }
        out.println(pow.pow(2, scc));
    }
}
