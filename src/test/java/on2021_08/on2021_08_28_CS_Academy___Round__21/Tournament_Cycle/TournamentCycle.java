package on2021_08.on2021_08_28_CS_Academy___Round__21.Tournament_Cycle;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.FastPow2;
import template.utils.Debug;

public class TournamentCycle {
    int mod = (int) 1e9 + 7;
    FastPow2 p2 = new FastPow2(2, mod);
    int L = (int) 5e3;
    Combination comb = new Combination(L, mod);

    public int choose2(int n) {
        return n * (n - 1) / 2;
    }

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        long[] g = new long[n + 1];
        g[0] = 1;
        for (int i = 1; i <= n; i++) {
            g[i] = p2.pow(choose2(i));
            for (int j = 1; j < i; j++) {
                g[i] -= g[j] * comb.combination(i, j) % mod * p2.pow(choose2(i - j)) % mod;
            }
            g[i] = DigitUtils.mod(g[i], mod);
        }
        long[] f = new long[n + 1];
        f[0] = 1;
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= i && j < k; j++){
                f[i] += f[i - j] * g[j] % mod * comb.combination(i, j) % mod;
            }
            f[i] = DigitUtils.mod(f[i], mod);
        }
        debug.debug("g", g);
        debug.debug("f", f);
        long ans = p2.pow(choose2(n)) - f[n];
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
