package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Power;

public class EulerianSubgraphs {
    int mod = (int)1e9 + 7;
    Power pow = new Power(mod);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        DSU dsu = new DSU(n);
        dsu.init();
        int redundant = 0;
        for(int i = 0; i < m; i++){
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            if(dsu.find(a) != dsu.find(b)){
                dsu.merge(a, b);
            }else{
                redundant++;
            }
        }
        int ans = pow.pow(2, redundant);
        out.println(ans);
    }
}
