package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;

public class ADreamoonAndSums {
    int mod = (int)1e9 + 7;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.rl();
        long b = in.rl();
        long ans = b * (IntMath.sumOfInterval(1, a) % mod) % mod * (IntMath.sumOfInterval(1, b - 1) % mod) % mod;
        ans += a * (IntMath.sumOfInterval(1, b - 1) % mod) % mod;
        ans %= mod;
        out.println(ans);
    }
}
