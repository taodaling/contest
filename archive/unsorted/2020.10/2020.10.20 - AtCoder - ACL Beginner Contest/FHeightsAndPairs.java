package contest;

import template.io.FastInput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Factorial;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.utils.Debug;
import template.utils.PrimitiveBuffers;
import template.utils.SequenceUtils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FHeightsAndPairs {
    Debug debug = new Debug(false);

    int mod = 998244353;
    int limit = (int) 2e5;
    int[] pair = new int[limit];

    public int pair(int n) {
        if (n == 0) {
            return 1;
        }
        if (pair[n] == -1) {
            pair[n] = (int) ((long) (n - 1) * pair(n - 2) % mod);
        }
        return pair[n];
    }

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        SequenceUtils.deepFill(pair, -1);
        int n = in.readInt();
        Map<Integer, Integer> cntMap = new HashMap<>();
        for (int i = 0; i < n * 2; i++) {
            int h = in.readInt();
            cntMap.put(h, cntMap.getOrDefault(h, 0) + 1);
        }
        int inv2 = (mod + 1) / 2;
        IntPoly poly = new IntPolyFFT(mod);
        List<int[]> polys = new ArrayList<>();
        Factorial fact = new Factorial(limit, mod);
        Combination comb = new Combination(fact);
        for (int v : cntMap.values()) {
            int[] p = PrimitiveBuffers.allocIntPow2(v / 2 + 1);
            p[0] = 1;
            for (int i = 1; i <= v / 2; i++) {
                int local = (int) ((long) comb.combination(v, i * 2) * pair(i * 2) % mod);
                p[i] = local;
            }
            polys.add(p);
        }
        int[] prod = poly.dacMul(polys.toArray(new int[0][]));
        debug.debug("prod", prod);
        long sum = 0;
        for (int i = 0; i < prod.length && i <= n; i++) {
            long local = prod[i];
            int remain = 2 * n - i * 2;
            local = local * pair(remain) % mod;
            if (i % 2 == 1) {
                local = -local;
            }
            sum += local;
        }

        int ans = DigitUtils.mod(sum, mod);
        out.println(ans);
    }
}
