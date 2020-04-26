package template.math;

public class IntegerModValueUtils {
    public static int pow(IntegerModValue x, IntegerModValue exp, int mod) {
        if (mod == 1) {
            return 0;
        }
        int val = x.get(mod);
        if (val == 1) {
            return 1;
        }
        if (val == 0) {
            if (exp.isZero()) {
                return DigitUtils.mod(1, mod);
            } else {
                return 0;
            }
        }
        int euler = CachedEulerFunction.get(mod);
        int e = exp.limit(euler);
        if (e == euler) {
            e = exp.get(euler) + euler;
        }
        return DigitUtils.modPow(val, e, mod);
    }

    public static IntegerModValue wrapExponentValue(IntegerModValue x, IntegerModValue exp) {
        return new IntegerModValue() {
            @Override
            public int get(int mod) {
                return pow(x, exp, mod);
            }

            @Override
            public boolean isZero() {
                return x.isZero() && !exp.isZero();
            }

            @Override
            public int limit(int limit) {
                int v1 = x.limit(limit);
                int v2 = exp.limit(limit);
                return (int) DigitUtils.limitPow(v1, v2, limit);
            }
        };
    }
}
