package on2020_06.on2020_06_10_Codeforces___Codeforces_Round__432__Div__1__based_on_IndiaHacks_Final_Round_2017_.F__Rainbow_Balls;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.InverseNumber;
import template.math.Modular;
import template.math.Power;
import template.utils.CompareUtils;
import template.utils.Debug;

import java.util.Arrays;

public class FRainbowBalls {
    Modular mod = new Modular(1e9 + 7);
    Power pow = new Power(mod);
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int max = Arrays.stream(a).max().getAsInt();
        int m = Arrays.stream(a).sum();
        int[] f = new int[max + 1];
        f[0] = 0;
        for (int i = 1; i <= max; i++) {
            f[i] = f[i - 1];
            f[i] = mod.plus(f[i], pow.inverseExtGCD(m - i + 1));
        }
        for (int i = 0; i <= max; i++) {
            f[i] = mod.mul(f[i], m - 1);
            f[i] = mod.mul(f[i], m - i);
        }

        int ans = 0;
        for (int x : a) {
            ans = mod.plus(ans, f[x]);
        }

        debug.debug("f", f);

        out.println(ans);
    }
}
