package contest;

import template.io.FastInput;
import template.math.CachedPow;
import template.polynomial.FastFourierTransform;
import template.polynomial.NumberTheoryTransform;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

import java.io.PrintWriter;

public class P6800ChirpZTransform {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int c = in.readInt();
        int m = in.readInt();

        int mod = (int) (998244353);
        CachedPow pow = new CachedPow(c, mod);
        int[] a = new int[m + n];
        int[] b = new int[n];
        in.populate(b);
        for (int i = 0; i < n; i++) {
            b[i] = (int) ((long) b[i] * pow.inverse(choose2(i)) % mod);
        }
        for (int i = 0; i < m + n; i++) {
            a[i] = pow.pow(choose2(i));
        }
        int[] mul = FastFourierTransform.deltaFFT(a, a.length, b, b.length, mod);
        for (int i = 0; i < m; i++) {
            long x = (long) mul[i] * pow.inverse(choose2(i)) % mod;
            out.print(x);
            out.append(' ');
        }
    }

    public long choose2(long n) {
        return n * (n - 1) / 2;
    }

}
