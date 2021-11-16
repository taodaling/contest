package on2021_11.on2021_11_09_AtCoder___AtCoder_Beginner_Contest_226.E___Just_one;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class EJustOne {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        DSUExt dsu = new DSUExt(n);
        dsu.init();
        for(int i = 0; i < m; i++){
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            dsu.merge(a, b);
            dsu.E[dsu.find(a)]++;
        }
        int mod = 998244353;
        long ans = 1;
        for(int i = 0; i < n; i++){
            if(dsu.find(i) == i){
                if(dsu.size[i] != dsu.E[i]){
                    out.println(0);
                    return;
                }
                ans = ans * 2 % mod;
            }
        }
        out.println(ans);
    }
}

class DSUExt extends DSU {
    int[] E;

    public DSUExt(int n) {
        super(n);
        E = new int[n];
    }

    @Override
    public void init(int n) {
        super.init(n);
        Arrays.fill(E, 0);
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        E[a] += E[b];
    }
}
