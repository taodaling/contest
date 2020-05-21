package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Modular;
import template.rand.Randomized;

import java.util.Arrays;

public class ADoYouWantADate {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] x = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = in.readInt();
        }
        Randomized.shuffle(x);
        Arrays.sort(x);
        Modular mod = new Modular(1e9 + 7);
        CachedPow pow = new CachedPow(2, mod);

        int ans = 0;
        for (int i = 1; i < n; i++) {
            int l = i;
            int r = n - i;
            int len = x[i] - x[i - 1];
            int set = mod.mul(pow.pow(l) - 1, pow.pow(r) - 1);
            ans = mod.plus(ans, mod.mul(len, set));
        }
        out.println(ans);
    }
}
