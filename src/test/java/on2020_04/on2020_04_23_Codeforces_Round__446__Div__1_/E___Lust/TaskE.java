package on2020_04.on2020_04_23_Codeforces_Round__446__Div__1_.E___Lust;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.InverseNumber;
import template.math.Modular;
import template.math.Power;
import template.polynomial.Polynomials;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;

public class TaskE {
    int p = (int) (1e9 + 7);
    Modular mod = new Modular(p);
    Power power = new Power(mod);
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        IntegerList[] ps = new IntegerList[n];
        for (int i = 0; i < n; i++) {
            ps[i] = new IntegerList();
            ps[i].add(a[i]);
            ps[i].add(-1);
        }

        IntegerList c = new IntegerList();
        Polynomials.dacMul(ps, c, mod);

        c.expandWith(0, n + 1);
        int exp = 0;

        int top = 1;
        int bot = 1;
        int invN = power.inverseByFermat(n);
        for (int i = 0; i <= n && i <= m; i++) {
            int j = m - i;
            if (i > 0) {
                top = mod.mul(top, j + 1);
                bot = mod.mul(bot, invN);
            }
            int ci = c.get(i);
            int local = mod.mul(top, bot);
            local = mod.mul(local, ci);
            exp = mod.plus(exp, local);
        }

        int prod = 1;
        for (int i = 0; i < n; i++) {
            prod = mod.mul(prod, a[i]);
        }

        debug.debug("prod", prod);
        debug.debug("exp", exp);
        debug.debug("c", c);
        int ans = mod.subtract(prod, exp);
        out.println(ans);
    }
}
