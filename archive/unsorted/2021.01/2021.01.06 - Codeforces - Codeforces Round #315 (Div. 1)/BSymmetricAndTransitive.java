package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;

public class BSymmetricAndTransitive {
    int mod = (int) 1e9 + 7;
    Combination comb = new Combination((int) 1e5, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] g = new long[n];
        g[0] = 1;
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= i; j++) {
                g[i] += comb.combination(i - 1, j - 1) * g[i - j] % mod;
            }
            g[i] %= mod;
        }
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += g[i] * comb.combination(n, i) % mod;
        }
        sum %= mod;
        out.println(sum);
    }
}
