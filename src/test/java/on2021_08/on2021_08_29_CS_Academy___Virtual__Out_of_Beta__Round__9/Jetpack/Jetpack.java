package on2021_08.on2021_08_29_CS_Academy___Virtual__Out_of_Beta__Round__9.Jetpack;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.utils.PrimitiveBuffers;

public class Jetpack {
    int mod = (int) 1e9 + 7;
    Combination comb = new Combination((int) 2e5, mod);

    public int bracket(int n) {
        return comb.combination(2 * n, n) - comb.combination(2 * n, n + 1);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] p = PrimitiveBuffers.allocIntPow2(n + 1);
        p[1] = 1;
        for (int i = 2; i <= n && i <= k * 2; i += 2) {
            p[i] = bracket((i - 2) / 2);
        }
        int[] bot = PrimitiveBuffers.allocIntPow2(p);
        for(int i = 0; i < bot.length; i++){
            bot[i] = DigitUtils.negate(bot[i], mod);
        }
        bot[0] = DigitUtils.modplus(bot[0], 1, mod);
        IntPoly poly = new IntPolyFFT(mod);
        int[] inv = poly.inverse(bot, n + 1);
        int[] conv = poly.convolution(p, inv);
        int ans = conv.length <= n ? 0 : conv[n];
        out.println(ans);
    }
}
