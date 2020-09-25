package on2020_09.on2020_09_24_Codeforces___VK_Cup_2018___Round_1.E__Perpetual_Subtraction;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Factorial;
import template.math.Modular;
import template.math.Power;
import template.polynomial.NumberTheoryTransform;
import template.polynomial.Polynomials;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

public class EPerpetualSubtraction {
    int mod = 998244353;
    Modular modular = new Modular(mod);
    Power pow = new Power(modular);
    Debug debug = new Debug(true);
    NumberTheoryTransform ntt = new NumberTheoryTransform(modular.getMod());

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long m = in.readLong();

        int[] p = new int[n + 1];
        in.populate(p);

        Factorial factorial = new Factorial(n + 1, mod);
        int[] p0 = p;
        int[] p1 = new int[n + 1];
        int[] p2 = new int[n + 1];
        int[] p3 = new int[n + 1];

        {
            IntegerArrayList a = Polynomials.listBuffer.alloc();
            IntegerArrayList b = Polynomials.listBuffer.alloc();
            IntegerArrayList c = Polynomials.listBuffer.alloc();
            a.expandWith(0, n + 1);
            b.expandWith(0, n + 1);

            for (int i = 0; i <= n; i++) {
                a.set(i, (int) ((long) factorial.fact(i) * p0[i] % mod));
                b.set(i, factorial.invFact(i));
            }

            ntt.deltaNTT(a, b, c);
            c.expandWith(0, n + 1);
            for (int i = 0; i <= n; i++) {
                p1[i] = (int) ((long) c.get(i) * factorial.invFact(i) % mod);
            }

            Polynomials.listBuffer.release(a);
            Polynomials.listBuffer.release(b);
            Polynomials.listBuffer.release(c);
        }

        for (int i = 0; i <= n; i++) {
            p2[i] = (int) ((long) pow.inversePower(i + 1, m) * p1[i] % mod);
        }

        {
            IntegerArrayList a = Polynomials.listBuffer.alloc();
            IntegerArrayList b = Polynomials.listBuffer.alloc();
            IntegerArrayList c = Polynomials.listBuffer.alloc();
            a.expandWith(0, n + 1);
            b.expandWith(0, n + 1);

            for (int i = 0; i <= n; i++) {
                a.set(i, (int) ((long) factorial.fact(i) * p2[i] % mod));
                b.set(i, DigitUtils.mod((i % 2 == 0 ? 1 : -1) * factorial.invFact(i), mod));
            }

            ntt.deltaNTT(a, b, c);
            c.expandWith(0, n + 1);
            for (int i = 0; i <= n; i++) {
                p3[i] = (int) ((long) c.get(i) * factorial.invFact(i) % mod);
            }

            Polynomials.listBuffer.release(a);
            Polynomials.listBuffer.release(b);
            Polynomials.listBuffer.release(c);
        }

        for (int i = 0; i <= n; i++) {
            out.println(p3[i]);
        }

        debug.debug("p0", p0);
        debug.debug("p1", p1);
        debug.debug("p2", p2);
        debug.debug("p3", p3);
    }
}
