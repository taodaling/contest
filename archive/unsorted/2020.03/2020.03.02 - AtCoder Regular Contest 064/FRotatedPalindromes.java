package contest;

import com.sun.org.apache.bcel.internal.generic.LDIV;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.DigitUtils;
import template.math.Factorization;
import template.math.Modular;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;

import java.util.Arrays;

public class FRotatedPalindromes {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        Modular mod = new Modular(1e9 + 7);
        //CachedPow cp = new CachedPow(k, mod);
        Power power = new Power(mod);
        int inv2 = (mod.getMod() + 1) / 2;
        IntegerList factorList = Factorization.factorizeNumber(n);
        factorList.sort();
        int[] factors = factorList.toArray();
        int[] cnt = new int[factors.length];
        for (int i = 0; i < factors.length; i++) {
            int d = factors[i];
            int way = power.pow(k, DigitUtils.ceilDiv(d, 2));
            cnt[i] = way;
        }
        for (int i = 0; i < factors.length; i++) {
            for (int j = 0; j < i; j++) {
                if (factors[i] % factors[j] == 0) {
                    cnt[i] = mod.subtract(cnt[i], cnt[j]);
                }
            }
        }
        int ans = 0;
        for (int i = 0; i < factors.length; i++) {
            int c = cnt[i];
            if (factors[i] % 2 == 0) {
                c = mod.mul(c, inv2);
            }
            ans = mod.plus(ans, mod.mul(c, factors[i]));
        }

        debug.debug("factors", factors);
        debug.debug("cnt", cnt);

        out.println(ans);
    }
}
