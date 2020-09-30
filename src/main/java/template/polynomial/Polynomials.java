package template.polynomial;

import template.math.DigitUtils;
import template.math.IntExtGCDObject;
import template.math.InverseNumber;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Buffer;

import java.util.BitSet;

public class Polynomials {
    public static Buffer<IntegerArrayList> listBuffer = new Buffer<>(IntegerArrayList::new, list -> list.clear());
    private static IntExtGCDObject extGCD = new IntExtGCDObject();


    public static int rankOf(IntegerArrayList p) {
        int[] data = p.getData();
        int r = p.size() - 1;
        while (r >= 0 && data[r] == 0) {
            r--;
        }
        return Math.max(0, r);
    }

    public static void normalize(IntegerArrayList list) {
        list.retain(rankOf(list) + 1);
    }

    /**
     * return list % x^n
     */
    public static void module(IntegerArrayList list, int n) {
        list.remove(n, list.size() - 1);
        normalize(list);
    }

    public static void mul(IntegerArrayList a, IntegerArrayList b, IntegerArrayList c, int mod) {
        int rA = rankOf(a);
        int rB = rankOf(b);
        c.clear();
        c.expandWith(0, rA + rB + 1);
        int[] aData = a.getData();
        int[] bData = b.getData();
        int[] cData = c.getData();
        for (int i = 0; i <= rA; i++) {
            for (int j = 0; j <= rB; j++) {
                cData[i + j] = (int) ((cData[i + j] + (long) aData[i] * bData[j]) % mod);
            }
        }
    }

    public static void dacMul(IntegerArrayList[] ps, IntegerArrayList output, int mod) {
        IntegerArrayList ans = dacMul(ps, 0, ps.length - 1, mod);
        output.clear();
        output.addAll(ans);
        listBuffer.release(ans);
    }

    private static IntegerArrayList dacMul(IntegerArrayList[] ps, int l, int r, int mod) {
        if (l == r) {
            IntegerArrayList alloc = listBuffer.alloc();
            alloc.addAll(ps[l]);
            return alloc;
        }
        int m = DigitUtils.floorAverage(l, r);
        IntegerArrayList a = dacMul(ps, l, m, mod);
        IntegerArrayList b = dacMul(ps, m + 1, r, mod);
        IntegerArrayList alloc = listBuffer.alloc();
        mul(a, b, alloc, mod);
        listBuffer.release(a);
        listBuffer.release(b);
        return alloc;
    }

    public static void plus(IntegerArrayList a, IntegerArrayList b, IntegerArrayList c, int mod) {
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
            cData[i] = DigitUtils.modplus(cData[i], bData[i], mod);
        }
    }

    public static void subtract(IntegerArrayList a, IntegerArrayList b, IntegerArrayList c, int mod) {
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
            cData[i] = DigitUtils.modsub(cData[i], bData[i], mod);
        }
    }

    public static void mul(IntegerArrayList a, int k, int mod) {
        int[] aData = a.getData();
        for (int i = a.size() - 1; i >= 0; i--) {
            aData[i] = (int) ((aData[i] * (long) k) % mod);
        }
    }

    public static void dotMul(IntegerArrayList a, IntegerArrayList b, IntegerArrayList c, int mod) {
        int rA = rankOf(a);
        int rB = rankOf(b);
        int rC = Math.min(rA, rB);
        c.clear();
        c.expandWith(0, rC + 1);
        int[] aData = a.getData();
        int[] bData = b.getData();
        int[] cData = c.getData();
        for (int i = 0; i <= rC; i++) {
            cData[i] = (int) ((long) aData[i] * bData[i] % mod);
        }
    }

    /**
     * a = b * c + remainder, the first number of b should be relative prime with mod
     */
    public static void divide(IntegerArrayList a, IntegerArrayList b, IntegerArrayList c, IntegerArrayList remainder, Power pow) {
        int mod = pow.getMod();
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

        if (extGCD.extgcd(b.get(rB), mod) != 1) {
            throw new IllegalArgumentException();
        }
        long inv = DigitUtils.mod(extGCD.getX(), mod);
        for (int i = rA, j = rC; i >= rB; i--, j--) {
            if (rData[i] == 0) {
                continue;
            }
            int factor = DigitUtils.mod(-rData[i] * inv, mod);
            cData[j] = DigitUtils.negate(factor, mod);
            for (int k = rB; k >= 0; k--) {
                rData[k + j] = (int) ((rData[k + j] + (long) factor * bData[k]) % mod);
            }
        }

        normalize(remainder);
    }

    /**
     * Try find x^k % p = remainder
     * <br>
     * This brute force run in O(n^2log_2k) while n is the length of p.
     */
    public static void module(long k, IntegerArrayList p, IntegerArrayList remainder, Power pow) {
        int rP = rankOf(p);
        if (rP == 0) {
            remainder.clear();
            remainder.add(0);
            return;
        }

        IntegerArrayList a = listBuffer.alloc();
        IntegerArrayList c = listBuffer.alloc();

        module(k, a, p, c, remainder, rP, pow);

        listBuffer.release(a);
        listBuffer.release(c);
    }

    private static void module(long k, IntegerArrayList a, IntegerArrayList b, IntegerArrayList c, IntegerArrayList remainder, int rb, Power pow) {
        int mod = pow.getMod();
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
    public static void module(BitSet k, IntegerArrayList p, IntegerArrayList remainder, Power pow) {
        int rP = rankOf(p);
        if (rP == 0) {
            remainder.clear();
            remainder.add(0);
            return;
        }

        IntegerArrayList a = listBuffer.alloc();
        IntegerArrayList c = listBuffer.alloc();

        module(k, a, p, c, remainder, pow);

        listBuffer.release(a);
        listBuffer.release(c);
    }

    private static void module(BitSet k, IntegerArrayList a, IntegerArrayList b, IntegerArrayList c, IntegerArrayList remainder, Power pow) {
        int mod = pow.getMod();
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
    public static void inverse(int[] p, int[] inv, int m, int mod) {
        if (m == 0) {
            inv[0] = new Power(mod).inverseExtGCD(p[0]);
            return;
        }
        inverse(p, inv, m - 1, mod);
        int n = 1 << m;
        for (int i = 1 << (m - 1); i < n; i++) {
            inv[i] = 0;
        }
        int[] ac = FastFourierTransform.multiplyMod(p, n, inv, n, mod);
        int[] acc = FastFourierTransform.multiplyMod(ac, n, inv, n, mod);
        for (int i = 0; i < n; i++) {
            int val = inv[i];
            val = DigitUtils.modplus(val, val, mod);
            val = DigitUtils.modsub(val, acc[i], mod);
            inv[i] = val;
        }
    }

    public static void differentialInPlace(IntegerArrayList p, int mod) {
        normalize(p);
        int n = p.size();
        int[] a = p.getData();
        for (int i = 1; i < n; i++) {
            a[i - 1] = (int) ((long) a[i] * i % mod);
        }
        a[n - 1] = 0;
        normalize(p);
    }

    public static void integralInPlace(IntegerArrayList p, int mod, InverseNumber inv) {
        normalize(p);
        int n = p.size();
        p.push(0);
        int[] a = p.getData();
        for (int i = n; i >= 1; i--) {
            a[i] = (int) ((long) a[i - 1] * inv.inverse(i) % mod);
        }
        a[0] = 0;
        normalize(p);
    }

    public static void differential(IntegerArrayList p, IntegerArrayList output, int mod) {
        normalize(p);
        int n = p.size();
        output.clear();
        output.expandWith(0, Math.max(0, n - 1));
        int[] a = p.getData();
        int[] b = output.getData();
        for (int i = 1; i < n; i++) {
            b[i - 1] = (int) ((long) a[i] * i % mod);
        }
    }

    public static void integral(IntegerArrayList p, IntegerArrayList output, int mod, InverseNumber inv) {
        normalize(p);
        int n = p.size();
        output.clear();
        output.expandWith(0, n + 1);
        int[] a = p.getData();
        int[] b = output.getData();
        for (int i = 0; i < n; i++) {
            b[i + 1] = (int) ((long) inv.inverse(i + 1) * a[i] % mod);
        }
    }

    public static void modmul(IntegerArrayList a, IntegerArrayList b, IntegerArrayList ans, int mod, int n) {
        mul(a, b, ans, mod);
        module(ans, n);
    }

    public static void modpow(IntegerArrayList x, IntegerArrayList ans, long k, int mod, int n) {
        IntegerArrayList ret = modpow(x, k, mod, n);
        ans.clear();
        ans.addAll(ret);
        listBuffer.release(ret);
    }

    private static IntegerArrayList modpow(IntegerArrayList x, long k, int mod, int n) {
        if (k == 0) {
            IntegerArrayList ans = listBuffer.alloc();
            ans.add(1 % mod);
            return ans;
        }
        IntegerArrayList ans = modpow(x, k / 2, mod, n);
        IntegerArrayList newAns = listBuffer.alloc();
        modmul(ans, ans, newAns, mod, n);
        if (k % 2 == 1) {
            modmul(x, newAns, ans, mod, n);
            IntegerArrayList tmp = newAns;
            newAns = ans;
            ans = tmp;
        }
        listBuffer.release(ans);
        return newAns;
    }
}
