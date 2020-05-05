package template.polynomial;

import template.math.ExtGCD;
import template.math.Power;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Buffer;

import java.util.BitSet;

public class Polynomials {
    public static Buffer<IntegerList> listBuffer = new Buffer<>(IntegerList::new, list -> list.clear());
    private static ExtGCD extGCD = new ExtGCD();

    public static int rankOf(IntegerList p) {
        int[] data = p.getData();
        int r = p.size() - 1;
        while (r >= 0 && data[r] == 0) {
            r--;
        }
        return Math.max(0, r);
    }

    public static void normalize(IntegerList list) {
        list.retain(rankOf(list) + 1);
    }

    public static void mul(IntegerList a, IntegerList b, IntegerList c, Modular mod) {
        int rA = rankOf(a);
        int rB = rankOf(b);
        c.clear();
        c.expandWith(0, rA + rB + 1);
        int[] aData = a.getData();
        int[] bData = b.getData();
        int[] cData = c.getData();
        for (int i = 0; i <= rA; i++) {
            for (int j = 0; j <= rB; j++) {
                cData[i + j] = mod.plus(cData[i + j], mod.mul(aData[i], bData[j]));
            }
        }
    }

    public static void dacMul(IntegerList[] ps, IntegerList output, Modular mod) {
        IntegerList ans = dacMul(ps, 0, ps.length - 1, mod);
        output.clear();
        output.addAll(ans);
        listBuffer.release(ans);
    }

    private static IntegerList dacMul(IntegerList[] ps, int l, int r, Modular mod) {
        if (l == r) {
            IntegerList alloc = listBuffer.alloc();
            alloc.addAll(ps[l]);
            return alloc;
        }
        int m = (l + r) >> 1;
        IntegerList a = dacMul(ps, l, m, mod);
        IntegerList b = dacMul(ps, m + 1, r, mod);
        IntegerList alloc = listBuffer.alloc();
        mul(a, b, alloc, mod);
        listBuffer.release(a);
        listBuffer.release(b);
        return alloc;
    }

    public static void plus(IntegerList a, IntegerList b, IntegerList c, Modular mod) {
        int rA = rankOf(a);
        int rB = rankOf(b);
        c.clear();
        c.expandWith(0, Math.max(rA, rB) + 1);
        int[] aData = a.getData();
        int[] bData = b.getData();
        int[] cData = c.getData();
        for (int i = 0; i <= rA; i++) {
            cData[i] = aData[i];
        }
        for (int i = 0; i <= rB; i++) {
            cData[i] = mod.plus(cData[i], bData[i]);
        }
    }

    public static void subtract(IntegerList a, IntegerList b, IntegerList c, Modular mod) {
        int rA = rankOf(a);
        int rB = rankOf(b);
        c.clear();
        c.expandWith(0, Math.max(rA, rB) + 1);
        int[] aData = a.getData();
        int[] bData = b.getData();
        int[] cData = c.getData();
        for (int i = 0; i <= rA; i++) {
            cData[i] = aData[i];
        }
        for (int i = 0; i <= rB; i++) {
            cData[i] = mod.subtract(cData[i], bData[i]);
        }
    }

    public static void mul(IntegerList a, int k, Modular mod) {
        int[] aData = a.getData();
        for (int i = a.size() - 1; i >= 0; i--) {
            aData[i] = mod.mul(aData[i], k);
        }
    }

    public static void dotMul(IntegerList a, IntegerList b, IntegerList c, Modular mod) {
        int rA = rankOf(a);
        int rB = rankOf(b);
        int rC = Math.min(rA, rB);
        c.clear();
        c.expandWith(0, rC + 1);
        int[] aData = a.getData();
        int[] bData = b.getData();
        int[] cData = c.getData();
        for (int i = 0; i <= rC; i++) {
            cData[i] = mod.mul(aData[i], bData[i]);
        }
    }

    /**
     * a = b * c + remainder, the first number of b should be relative prime with mod
     */
    public static void divide(IntegerList a, IntegerList b, IntegerList c, IntegerList remainder, Power pow) {
        Modular mod = pow.getModular();
        int rA = rankOf(a);
        int rB = rankOf(b);

        if (rA < rB) {
            c.clear();
            c.add(0);
            remainder.clear();
            remainder.addAll(a);
            return;
        }

        int rC = Math.max(0, rA - rB);
        c.clear();
        c.expandWith(0, rC + 1);
        remainder.clear();
        remainder.addAll(a);

        int[] bData = b.getData();
        int[] cData = c.getData();
        int[] rData = remainder.getData();

        if (extGCD.extgcd(b.get(rB), mod.getMod()) != 1) {
            throw new IllegalArgumentException();
        }
        int inv = mod.valueOf(extGCD.getX());
        for (int i = rA, j = rC; i >= rB; i--, j--) {
            if (rData[i] == 0) {
                continue;
            }
            int factor = mod.mul(-rData[i], inv);
            cData[j] = mod.valueOf(-factor);
            for (int k = rB; k >= 0; k--) {
                rData[k + j] = mod.plus(rData[k + j], mod.mul(factor, bData[k]));
            }
        }

        normalize(remainder);
    }

    /**
     * Try find x^k % p = remainder
     * <br>
     * This brute force run in O(n^2log_2k) while n is the length of p.
     */
    public static void module(long k, IntegerList p, IntegerList remainder, Power pow) {
        int rP = rankOf(p);
        if (rP == 0) {
            remainder.clear();
            remainder.add(0);
            return;
        }

        IntegerList a = listBuffer.alloc();
        IntegerList c = listBuffer.alloc();

        module(k, a, p, c, remainder, rP, pow);

        listBuffer.release(a);
        listBuffer.release(c);
    }

    private static void module(long k, IntegerList a, IntegerList b, IntegerList c, IntegerList remainder, int rb, Power pow) {
        Modular mod = pow.getModular();
        if (k < rb) {
            remainder.clear();
            remainder.expandWith(0, (int) k + 1);
            remainder.set((int) k, 1);
            return;
        }
        module(k / 2, a, b, c, remainder, rb, pow);
        mul(remainder, remainder, a, mod);
        if (k % 2 == 1) {
            int ra = rankOf(a);
            a.expandWith(0, ra + 2);
            int[] aData = a.getData();
            for (int i = ra; i >= 0; i--) {
                aData[i + 1] = aData[i];
            }
            aData[0] = 0;
        }
        divide(a, b, c, remainder, pow);
    }

    /**
     * Try find x^k % p = remainder, the first number of b should be relative prime with mod
     */
    public static void module(BitSet k, IntegerList p, IntegerList remainder, Power pow) {
        int rP = rankOf(p);
        if (rP == 0) {
            remainder.clear();
            remainder.add(0);
            return;
        }

        IntegerList a = listBuffer.alloc();
        IntegerList c = listBuffer.alloc();

        module(k, a, p, c, remainder, pow);

        listBuffer.release(a);
        listBuffer.release(c);
    }

    private static void module(BitSet k, IntegerList a, IntegerList b, IntegerList c, IntegerList remainder, Power pow) {
        Modular mod = pow.getModular();
        remainder.clear();
        remainder.expandWith(1, 1);
        for (int index = 0; index < k.size(); index++) {
            mul(remainder, remainder, a, mod);
            if (k.get(index)) {
                int ra = rankOf(a);
                a.expandWith(0, ra + 2);
                int[] aData = a.getData();
                for (int i = ra; i >= 0; i--) {
                    aData[i + 1] = aData[i];
                }
                aData[0] = 0;
            }
            divide(a, b, c, remainder, pow);
        }
    }

    /**
     * return polynomial g while p * g = 1 (mod x^{2^m}).
     * <br>
     * You are supposed to guarantee the lengths of all arrays are greater than or equal to 2^{m + 1)}
     */
    public static void inverse(int[] p, int[] inv, int m, Modular mod) {
        if (m == 0) {
            inv[0] = new Power(mod).inverseExtGCD(p[0]);
            return;
        }
        inverse(p, inv, m - 1, mod);
        int n = 1 << m;
        for (int i = 1 << (m - 1); i < n; i++) {
            inv[i] = 0;
        }
        int[] ac = FastFourierTransform.multiplyMod(p, n, inv, n, mod.getMod());
        int[] acc = FastFourierTransform.multiplyMod(ac, n, inv, n, mod.getMod());
        for (int i = 0; i < n; i++) {
            inv[i] = mod.subtract(mod.mul(inv[i], 2), acc[i]);
        }
    }
}
