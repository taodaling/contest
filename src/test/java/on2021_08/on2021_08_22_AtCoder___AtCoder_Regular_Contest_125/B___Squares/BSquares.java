package on2021_08.on2021_08_22_AtCoder___AtCoder_Regular_Contest_125.B___Squares;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class BSquares {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        long ans = 0;
        for (int i = 1; (long) i * i <= n; i++) {
            long z = DigitUtils.floorDiv(n - (long) i * i, 2L * i);
            //d + z <= n
            z = Math.min(z, n - i);
            if (z >= 0) {
                ans += z + 1;
                ans %= mod;
            }
        }
        out.println(ans);
    }
}
