package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.PrimitiveRoot;
import template.polynomial.FastFourierTransform;

public class CProductModuloEditorial {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int p = 200003;
        Modular mod = new Modular(p);
        Modular powMod = mod.getModularForPowerComputation();
        int n = in.readInt();
        int[] a = new int[n];
        int root = new PrimitiveRoot(p).findMinPrimitiveRoot();
        int[] invPow = new int[p];
        int[] pow = new int[p];
        for (int i = 0, val = 1; i < p - 1; i++, val = mod.mul(val, root)) {
            invPow[val] = i;
            pow[i] = val;
        }
        int proper = Log2.ceilLog(p - 1 + p - 1 + 1);
        double[][] poly = new double[2][1 << proper];
        for (int i = 0; i < n; i++) {
            if (a[i] == 0) {
                continue;
            }
            poly[0][invPow[a[i]]]++;
        }
        FastFourierTransform.dft(poly, proper);
        FastFourierTransform.dotMul(poly, poly, poly, proper);
        FastFourierTransform.idft(poly, proper);
        long sum = 0;
        for (int i = 0; i < 1 << proper; i++) {
            int val = pow[powMod.valueOf(i)];
            sum += Math.round(poly[0][i]) * val;
        }

        for (int x : a) {
            sum -= mod.mul(x, x);
        }

        sum /= 2;
        out.println(sum);
    }
}
