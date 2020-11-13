package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class SumOfDivisors {
    int mod = (int) 1e9 + 7;
    int inv2 = (mod + 1) / 2;

    public long sum(long l, long r) {
        return ((r - l + 1) % mod) * ((l + r) % mod) % mod * inv2 % mod;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long ans = 0;
        for (long l = 1, r; l <= n; l = r + 1) {
            r = n / (n / l);
            ans += sum(l, r) * (n / l) % mod;
        }
        ans %= mod;
        out.println(ans);
    }
}
