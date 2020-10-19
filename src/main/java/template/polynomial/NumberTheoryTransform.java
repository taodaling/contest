package template.polynomial;

import template.binary.Log2;
import template.math.DigitUtils;
import template.math.InverseNumber;
import template.math.Power;
import template.math.PrimitiveRoot;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.PrimitiveBuffers;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.BitSet;

public class NumberTheoryTransform {
    public static void ntt(int[] p, boolean inv, int mod, int g, Power power) {
        int m = Log2.ceilLog(p.length);
        int n = 1 << m;

        int shift = 32 - Integer.numberOfTrailingZeros(n);
        for (int i = 1; i < n; i++) {
            int j = Integer.reverse(i << shift);
            if (i < j) {
                int temp = p[i];
                p[i] = p[j];
                p[j] = temp;
            }
        }

        for (int d = 0; d < m; d++) {
            int w1 = power.pow(g, (mod - 1) >> 1 + d);
            int s = 1 << d;
            int s2 = s << 1;
            for (int i = 0; i < n; i += s2) {
                int w = 1;
                for (int j = 0; j < s; j++) {
                    int a = i + j;
                    int b = a + s;
                    int t = (int) ((long) w * p[b] % mod);
                    p[b] = DigitUtils.modsub(p[a], t, mod);
                    p[a] = DigitUtils.modplus(p[a], t, mod);
                    w = (int) ((long) w * w1 % mod);
                }
            }
        }

        if (inv) {
            long invN = power.inverse(n);
            for (int i = 0, j = 0; i <= j; i++, j = n - i) {
                int a = p[j];
                p[j] = (int) (p[i] * invN % mod);
                if (i != j) {
                    p[i] = (int) (a * invN % mod);
                }
            }
        }
    }
}
