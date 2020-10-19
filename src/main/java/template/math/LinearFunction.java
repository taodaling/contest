package template.math;

/**
 * y=ax+b
 */
public class LinearFunction {
    public final int a;
    public final int b;

    public static final LinearFunction IDENTITY = new LinearFunction(1, 0);
    public static final LinearFunction ONE = new LinearFunction(0, 1);
    public static final LinearFunction ZERO = new LinearFunction(0, 0);


    public LinearFunction(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int apply(int x, int mod) {
        return DigitUtils.mod((long) a * x + b, mod);
    }

    //a(b) = a.a(b.a x + b.b) + a.b
    public static LinearFunction merge(LinearFunction a, LinearFunction b, int mod) {
        return new LinearFunction(DigitUtils.mod((long) a.a * b.a, mod), DigitUtils.mod((long) a.a * b.b + a.b, mod));
    }

    public static LinearFunction plus(LinearFunction a, LinearFunction b, int mod) {
        return new LinearFunction(DigitUtils.modplus(a.a, b.a, mod), DigitUtils.modplus(a.b, b.b, mod));
    }

    public static LinearFunction plus(LinearFunction a, int ba, int bb, int mod) {
        return new LinearFunction(DigitUtils.modplus(a.a, ba, mod), DigitUtils.modplus(a.b, bb, mod));
    }

    public static LinearFunction subtract(LinearFunction a, LinearFunction b, int mod) {
        return new LinearFunction(DigitUtils.modsub(a.a, b.a, mod), DigitUtils.modsub(a.b, b.b, mod));
    }

    public static LinearFunction subtract(LinearFunction a, int ba, int bb, int mod) {
        return new LinearFunction(DigitUtils.modsub(a.a, ba, mod), DigitUtils.modsub(a.b, bb, mod));
    }

    public static LinearFunction mul(LinearFunction a, int b, int mod) {
        return new LinearFunction(DigitUtils.mod((long) a.a * b, mod), DigitUtils.mod((long) a.b * b, mod));
    }

    //ax+b=y => x=(y-b)/a
    public static LinearFunction inverse(LinearFunction func, int mod, Power pow) {
        int invA = pow.inverseExtGCD(func.a);
        return new LinearFunction(invA, DigitUtils.mod((long) -func.b * invA, mod));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LinearFunction)) {
            return false;
        }
        LinearFunction function = (LinearFunction) obj;
        return function.a == a && function.b == b;
    }


    @Override
    public int hashCode() {
        return a * 31 + b;
    }

    @Override
    public String toString() {
        if (b >= 0) {
            return a + "x+" + b;
        }
        return a + "x" + b;
    }
}