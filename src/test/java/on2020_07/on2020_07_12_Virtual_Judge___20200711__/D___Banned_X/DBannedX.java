package on2020_07.on2020_07_12_Virtual_Judge___20200711__.D___Banned_X;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Modular;
import template.math.Power;
import template.polynomial.NumberTheoryTransform;
import template.utils.Debug;

import java.util.Arrays;

public class DBannedX {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();

        Modular mod = new Modular(998244353);
        Power pow = new Power(mod);
        Combination comb = new Combination(n, pow);
        int[] occupy = new int[n + 1];
        //S < x
        for (int i = 0; i <= n; i++) {
            int two = x - 1 - i;
            for (int j = 0; j < two; j++) {
                occupy[i] = mod.plus(occupy[i], comb.combination(i, j));
            }
        }
        //S >= x
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= i; j++) {
                int k = i - j;
                int two = x - 1 - j;
                if (two < 0) {
                    continue;
                }
                if (k >= j) {
                    //all two
                    if (j == two) {
                        occupy[i] = mod.plus(occupy[i], 1);
                    }
                } else {
                    if (two >= k) {
                        occupy[i] = mod.plus(occupy[i], comb.combination(j - k, two - k));
                    }
                }
            }
        }
        debug.debug("occupy", occupy);

        int ans = 0;
        for (int i = 0; i <= n; i++) {
            int contrib = occupy[i];
            contrib = mod.mul(contrib, comb.combination(n, i));
            ans = mod.plus(ans, contrib);
        }

        out.println(ans);
    }
}
