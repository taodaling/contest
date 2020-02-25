package template.math;

import template.primitve.generated.datastructure.IntegerFunction;

/**
 * Calculate x1 ^ x2 ^ x3 ^ x4 ^ x5 ^ ... ^ xn while xi >= 1
 */
public class IntegerModPowerLink {
    private IntegerFunction f;
    // private static int limit = 1 << 16;

    public IntegerModPowerLink(IntegerFunction f) {
        this.f = f;
    }

    private int test(int l, int r, int limit) {
        int val = f.apply(l);
        if (l == r) {
            return val;
        }
        if (val == 1) {
            return val;
        }
        int prev = test(l + 1, r, limit);
        return prev < limit ? DigitUtils.limitPow(val, prev, limit) : limit;
    }

    public int query(int l, int r, int m) {
        int val = f.apply(l);
        if (l == r) {
            return DigitUtils.mod(val, m);
        }
        if (m == 1) {
            return 0;
        }
        int expMod = CachedEulerFunction.get(m);
        int t = test(l + 1, Math.min(l + 5, r), expMod);
        if (t < expMod) {
            return DigitUtils.modPow(val, t, m);
        }
        int exp = query(l + 1, r, expMod);
        exp += expMod;
        return DigitUtils.modPow(val, exp, m);
    }

//    private int test(int l, int r) {
//        int val = f.apply(l);
//        if (l == r) {
//            return val;
//        }
//        if (val == 1) {
//            return val;
//        }
//        int prev = test(l + 1, r);
//        return prev < limit ? DigitUtils.limitPow(val, prev, limit) : limit;
//    }
//
//    /**
//     * return f(l) ^ f(l + 1) ^ ... ^ f(r)
//     */
//    public int query(int l, int r, int m) {
//        int val = f.apply(l);
//        if (l == r) {
//            return DigitUtils.mod(val, m);
//        }
//        if (val == 1) {
//            return DigitUtils.mod(val, m);
//        }
//        if (m == 1) {
//            return 0;
//        }
//        int t = test(l + 1, Math.min(l + 5, r));
//        if (t < limit) {
//            return DigitUtils.modPow(val, t, m);
//        }
//        return query(l, r, m, 0);
//    }
//
//    private int query(int l, int r, int b, int sub) {
//        int val = f.apply(l);
//        int g = GCDs.gcd(val, b);
//        if (g == 1) {
//            int expMod = CachedEulerFunction.get(b);
//            int ans = query(l + 1, r, expMod);
//            ans = DigitUtils.mod(ans - sub, expMod);
//            return DigitUtils.modPow(val, ans, b);
//        }
//        int ans = query(l, r, b / g, sub + 1);
//        ans = DigitUtils.mod((long) ans * (val / g), b / g);
//        return ans * g;
//    }
}
