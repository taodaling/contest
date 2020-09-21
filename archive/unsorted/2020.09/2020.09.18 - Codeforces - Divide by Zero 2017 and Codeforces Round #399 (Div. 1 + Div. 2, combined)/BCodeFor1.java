package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.Map;

public class BCodeFor1 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readLong();
        long l = in.readLong();
        long r = in.readLong();
        f = new long[64];
        Arrays.fill(f, -1);

        long ans = dfs(0, l, r, 1, f(0));
        out.println(ans);
    }

    long n;
    long[] f;

    public long f(int i) {
        long m = n >> i;
        if (m <= 1) {
            return 1;
        }
        if (f[i] == -1) {
            f[i] = f(i + 1) + f(i + 1) + 1;
        }
        return f[i];
    }

    public long dfs(int i, long ll, long rr, long l, long r) {
        if (rr < l || ll > r) {
            return 0;
        }
        long m = n >> i;
        if (l == r) {
            return m;
        }
        long ans = dfs(i + 1, ll, rr, l, l + f(i + 1) - 1);
        if (ll <= l + f(i + 1) && l + f(i + 1) <= rr) {
            ans += m % 2;
        }
        ans += dfs(i + 1, ll, rr, l + f(i + 1) + 1, r);
        return ans;
    }
}
