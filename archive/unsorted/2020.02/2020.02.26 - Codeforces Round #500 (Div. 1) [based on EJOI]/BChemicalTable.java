package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerHashSet;

public class BChemicalTable {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int q = in.readInt();
        DSU dsu = new DSU(n + m);
        for (int i = 0; i < q; i++) {
            int r = in.readInt() - 1;
            int c = in.readInt() - 1;
            dsu.merge(r, n + c);
        }
        int set = 0;
        for(int i = 0; i < n + m; i++){
            if(dsu.find(i) == i){
                set++;
            }
        }
        out.println(set - 1);
    }
}
