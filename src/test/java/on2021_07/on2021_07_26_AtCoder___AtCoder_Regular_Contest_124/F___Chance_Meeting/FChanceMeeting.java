package on2021_07.on2021_07_26_AtCoder___AtCoder_Regular_Contest_124.F___Chance_Meeting;



import template.graph.LcaOnTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;

public class FChanceMeeting {
    int mod = 998244353;
    Combination comb = new Combination((int) 1e6, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.ri();
        int w = in.ri();
        h--;
        w--;
        int[] g = new int[w + 1];
        for (int i = 0; i <= w; i++) {
            int step = i * 2 + h;
            long contrib = (long) comb.combination(step, h) *
                    comb.combination(i * 2, i) % mod;
            g[i] = (int) contrib;
        }
        int[] catalan = new int[w + 1];
        for (int i = 0; i <= w; i++) {
            catalan[i] = comb.combination(2 * i, i) - comb.combination(2 * i, i + 1);
            if (catalan[i] < 0) {
                catalan[i] += mod;
            }
        }
        IntPoly poly = new IntPolyNTT(mod);
        int[] conv = poly.convolution(g, catalan);
        long[] f = new long[w + 1];
        for (int i = 0; i <= w; i++) {
            long sub = i - 1 < 0 || i - 1 >= f.length ? 0 : conv[i - 1];
            f[i] = (long) g[i] - sub * 2;
            f[i] %= mod;
        }

        long ans = 0;
        for (int i = 0; i <= w; i++) {
            long contrib = f[i] * f[w - i] % mod;
            ans += contrib;
        }
        ans = ans % mod * comb.combination(2 * h, h) % mod;
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
