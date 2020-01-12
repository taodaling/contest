package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Composite;
import template.math.Factorial;
import template.math.Modular;
import template.math.Power;
import template.polynomial.FastFourierTransform;
import template.primitve.generated.IntegerList;

import java.util.Arrays;

public class CCookieDistribution {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[k];
        Modular mod = new Modular(1e9 + 7);
        Power power = new Power(mod);
        Composite comp = new Composite(n, mod);
        Factorial fact = new Factorial(n, mod);

        int[][] ps = new int[k][];
        for (int i = 0; i < k; i++) {
            a[i] = in.readInt();
            ps[i] = new int[a[i] + 1];
            for (int j = 0; j <= a[i]; j++) {
                ps[i][j] = mod.mul(comp.composite(n - j, a[i] - j), fact.invFact(j));
                ps[i][j] = mod.mul(ps[i][j], comp.invComposite(n, a[i]));
            }
        }

        int[] p = new int[]{1};
        for (int i = 0; i < k; i++) {
            p = FastFourierTransform.multiplyMod(p, ps[i], mod.getMod());
        }
        int ans = p.length >= n ? p[n] : 0;
        ans = mod.mul(ans, fact.fact(n));
        for(int i = 0; i < k; i++){
            ans = mod.mul(ans, comp.composite(n, a[i]));
        }

        out.println(ans);
    }
}
