package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.math.GCDs;

public class AIntegerProduct {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        long mod = (long) 1e18;
        for (int i = 0; i < n; i++) {
            a[i] = Math.round(in.readDouble() * 1e9);
        }
        long[] factors = new long[19 * 19];
        for (int i = 0; i <= 18; i++) {
            for (int j = 0; j <= 18; j++) {
                factors[i * 19 + j] = pow(2, i) * pow(5, j);
            }
        }
        int[] cnts = new int[factors.length];
        long ans = 0;
        for (int i = 0; i < n; i++) {
            long g = GCDs.gcd(mod, a[i]);
            long req = mod / g;
            for (int j = 0; j < factors.length; j++) {
                if (factors[j] == req) {
                    ans += cnts[j];
                }
                if (a[i] % factors[j] == 0) {
                    cnts[j]++;
                }
            }
        }

        out.println(ans);
    }

    public long pow(long x, int n) {
        long ans = 1;
        while (n-- > 0) {
            ans *= x;
        }
        return ans;
    }
}
