package on2020_10.on2020_10_22_AtCoder___AtCoder_Regular_Contest_065.D___Connectivity;



import template.datastructure.DSU;
import template.io.FastInput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongHashMap;

import java.io.PrintWriter;

public class DConnectivity {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int k = in.readInt();
        int l = in.readInt();
        DSU a = new DSU(n);
        DSU b = new DSU(n);
        a.init();
        b.init();
        for (int i = 0; i < k; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            a.merge(u, v);
        }
        for(int i = 0; i < l; i++){
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            b.merge(u, v);
        }
        LongHashMap map = new LongHashMap(n, false);
        for(int i = 0; i < n; i++){
            long key = DigitUtils.asLong(a.find(i), b.find(i));
            map.put(key, map.get(key) + 1);
        }
        for(int i = 0; i < n; i++){
            long key = DigitUtils.asLong(a.find(i), b.find(i));
            long ans = map.get(key);
            out.println(ans);
        }
    }
}
