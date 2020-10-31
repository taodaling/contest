package contest;

import template.io.FastInput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.LongHashMap;

import java.io.PrintWriter;

public class SubarrayDivisibility {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] p = new int[n];
        in.populate(p);
        for (int i = 0; i < n; i++) {
            p[i] = DigitUtils.mod(p[i], n);
        }
        IntegerHashMap map = new IntegerHashMap(n + 1, false);
        map.put(0, 1);
        int ps = 0;
        long ans = 0;
        for (int i = 0; i < n; i++) {
            ps = DigitUtils.modplus(ps, p[i], n);
            int pre = ps;
            ans += map.get(pre);
            map.put(ps, map.get(ps) + 1);
        }
        out.println(ans);
    }
}
