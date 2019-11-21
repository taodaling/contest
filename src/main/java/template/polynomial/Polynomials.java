package template.polynomial;

import template.math.Power;
import template.datastructure.ByteList;
import template.datastructure.IntList;
import template.math.Modular;
import template.utils.Buffer;

public class Polynomials {
    public static Buffer<IntList> listBuffer = new Buffer<>(IntList::new, list -> list.clear());

    public static int rankOf(IntList p) {
        int[] data = p.getData();
        int r = p.size() - 1;
        while (r >= 0 && data[r] == 0) {
            r--;
        }
        return Math.max(0, r);
    }

    public static void normalize(IntList list) {
        list.retain(rankOf(list) + 1);
    }

    public static void mul(IntList a, IntList b, IntList c, Modular mod) {
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

    public static void plus(IntList a, IntList b, IntList c, Modular mod) {
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

    public static void subtract(IntList a, IntList b, IntList c, Modular mod) {
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

    public static void mul(IntList a, int k, Modular mod) {
        int[] aData = a.getData();
        for (int i = a.size() - 1; i >= 0; i--) {
            aData[i] = mod.mul(aData[i], k);
        }
    }

    public static void dotMul(IntList a, IntList b, IntList c, Modular mod) {
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
     * a = b * c + remainder
     */
    public static void divide(IntList a, IntList b, IntList c, IntList remainder, Power pow) {
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

        int inv = pow.inverse(b.get(rB));
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
     */
    public static void module(long k, IntList p, IntList remainder, Power pow) {
        int rP = rankOf(p);
        if (rP == 0) {
            remainder.clear();
            remainder.add(0);
            return;
        }

        IntList a = listBuffer.alloc();
        IntList c = listBuffer.alloc();

        module(k, a, p, c, remainder, rP, pow);

        listBuffer.release(a);
        listBuffer.release(c);
    }

    private static void module(long k, IntList a, IntList b, IntList c, IntList remainder, int rb, Power pow) {
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
     * Try find x^k % p = remainder
     */
    public static void module(ByteList k, IntList p, IntList remainder, Power pow) {
        int rP = rankOf(p);
        if (rP == 0) {
            remainder.clear();
            remainder.add(0);
            return;
        }

        IntList a = listBuffer.alloc();
        IntList c = listBuffer.alloc();

        module(k, a, p, c, remainder, pow);

        listBuffer.release(a);
        listBuffer.release(c);
    }

    private static void module(ByteList k, IntList a, IntList b, IntList c, IntList remainder, Power pow) {
        Modular mod = pow.getModular();
        remainder.clear();
        remainder.expandWith(1, 1);
        for (int index = 0; index < k.size(); index++) {
            mul(remainder, remainder, a, mod);
            if (k.get(index) == 1) {
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
}
