package on2021_04.on2021_04_11_Codeforces___Codeforces_Round__213__Div__1_.D__Ghd0;




import template.io.FastInput;
import template.io.FastOutput;
import template.math.DivisionPrefixSum;
import template.math.GCDs;
import template.math.LongFactorization;
import template.math.OrderedDivisorIterator;
import template.rand.RandomWrapper;

import java.util.Arrays;

public class DGhd {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] a = in.rl(n);
        if (n <= 2) {
            long max = Arrays.stream(a).max().orElse(-1);
            out.println(max);
            return;
        }

        long ans = 0;
        for (int i = 0; i < 20; i++) {
            long x = RandomWrapper.INSTANCE.range(a);
            long cand = search((n + 1) / 2, a, x);
            ans = Math.max(ans, cand);
        }

        out.println(ans);
    }

    OrderedDivisorIterator visitor = new OrderedDivisorIterator(true);
    DivisionPrefixSum ps = new DivisionPrefixSum(0, Long::sum);


    public long search(int atLeast, long[] a, long g) {
        LongFactorization factorization = new LongFactorization(g);
        visitor.init(factorization);
        ps.init(factorization);
        for (long x : a) {
            ps.add(g / GCDs.gcd(g, x), 1);
        }
        while (visitor.hasNext()) {
            long x = visitor.next();
            if (ps.prefixSum(x) >= atLeast) {
                return g / x;
            }
        }
        return 1;
    }
}
