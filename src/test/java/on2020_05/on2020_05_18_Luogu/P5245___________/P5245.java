package on2020_05.on2020_05_18_Luogu.P5245___________;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.InverseNumber;
import template.math.ModPrimeInverseNumber;
import template.math.Modular;
import template.polynomial.NumberTheoryTransform;
import template.polynomial.Polynomials;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;

public class P5245 {
    Modular mod = new Modular(998244353);
    InverseNumber inv = new ModPrimeInverseNumber((int) 2e5, mod);
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = new char[(int) 1e5];
        int m = in.readString(s, 0);
        int k = 0;
        for (int i = 0; i < m; i++) {
            k = mod.valueOf((long) k * 10 + s[i] - '0');
        }

        IntegerList a = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            a.push(in.readInt());
        }

        debug.debug("k", k);
        debug.debug("a", a);

        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        IntegerList ans = new IntegerList(n);
        ntt.powmod(a, ans, k, n);
        a = ans;

//
//
//        IntegerList lna = new IntegerList();
//        ntt.ln(a, lna, n, inv);
//        debug.debug("lna", lna);
//        Polynomials.mul(lna, k, mod);
//        debug.debug("k * lna", lna);
//        ntt.exp(lna, a, n, inv);
//
//        debug.debug("b", a);

        a.expandWith(0, n);
        for (int i = 0; i < n; i++) {
            out.append(a.get(i)).append(' ');
        }
    }
}
