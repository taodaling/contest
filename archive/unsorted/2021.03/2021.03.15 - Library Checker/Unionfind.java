package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

public class Unionfind {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        DSU dsu = new DSU(n);
        dsu.init();
        for(int i = 0; i < q; i++){
            int t = in.ri();
            int u = in.ri();
            int v = in.ri();
            if(t == 0){
                dsu.merge(u, v);
            }else{
                boolean ans = dsu.find(u) == dsu.find(v);
                out.println(ans ? '1' : '0');
            }
        }
    }
}
