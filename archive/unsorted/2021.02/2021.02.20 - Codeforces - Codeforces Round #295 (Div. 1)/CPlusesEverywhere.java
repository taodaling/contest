package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Combination;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerPreSum;

public class CPlusesEverywhere {
    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);
    Combination comb = new Combination((int) 2e5, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] digit = new int[n];
        for (int i = 0; i < n; i++) {
            digit[i] = in.rc() - '0';
        }
        int[] p = new int[n];
        int[] q = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = comb.combination(n - 1 - (i + 1), k - 1);
            q[i] = comb.combination(n - 1 - i, k);
        }
        IntegerPreSum ps = new IntegerPreSum(i -> digit[i], n);
        CachedPow pow10 = new CachedPow(10, mod);
        long sum = 0;
        for (int i = 0; i < n; i++) {
            long contrib = (long) p[i] * pow10.pow(i) % mod * ps.prefix(n - 1 - i - 1) % mod;
            sum += contrib;
        }
        for (int i = 0; i < n; i++) {
            int tail = n - 1 - i;
            long contrib = (long) digit[i] * q[tail] % mod * pow10.pow(n - 1 - i) % mod;
            sum += contrib;
        }
        sum %= mod;
        out.println(sum);
    }
}
