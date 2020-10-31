package on2020_10.on2020_10_24_CSES___CSES_Problem_Set.Subarray_Sums_I;



import template.io.FastInput;
import template.primitve.generated.datastructure.LongHashMap;

import java.io.PrintWriter;

public class SubarraySumsI {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        long x = in.readLong();
        long[] p = new long[n];
        in.populate(p);
        LongHashMap map = new LongHashMap(n + 1, false);
        map.put(0, 1);
        long ps = 0;
        long ans = 0;
        for (int i = 0; i < n; i++) {
            ps += p[i];
            long pre = ps - x;
            ans += map.get(pre);
            map.put(ps, map.get(ps) + 1);
        }
        out.println(ans);
    }
}
