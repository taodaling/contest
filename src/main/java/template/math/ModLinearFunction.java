package template.math;

/**
 * y=ax+b
 */
public class ModLinearFunction {
    public final int a;
    public final int b;

    public static final ModLinearFunction IDENTITY = new ModLinearFunction(1, 0);
    public static final ModLinearFunction ONE = new ModLinearFunction(0, 1);
    public static final ModLinearFunction ZERO = new ModLinearFunction(0, 0);


    public ModLinearFunction(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int apply(int x, int mod) {
        return DigitUtils.mod((long) a * x + b, mod);
    }

    //a(b) = a.a(b.a x + b.b) + a.b
    public static ModLinearFunction merge(ModLinearFunction a, ModLinearFunction b, int mod) {
        return new ModLinearFunction(DigitUtils.mod((long) a.a * b.a, mod), DigitUtils.mod((long) a.a * b.b + a.b, mod));
    }

    public static ModLinearFunction plus(ModLinearFunction a, ModLinearFunction b, int mod) {
        return new ModLinearFunction(DigitUtils.modplus(a.a, b.a, mod), DigitUtils.modplus(a.b, b.b, mod));
    }

    public static ModLinearFunction plus(ModLinearFunction a, int ba, int bb, int mod) {
        return new ModLinearFunction(DigitUtils.modplus(a.a, ba, mod), DigitUtils.modplus(a.b, bb, mod));
    }

    public static ModLinearFunction subtract(ModLinearFunction a, ModLinearFunction b, int mod) {
        return new ModLinearFunction(DigitUtils.modsub(a.a, b.a, mod), DigitUtils.modsub(a.b, b.b, mod));
    }

    public static ModLinearFunction subtract(ModLinearFunction a, int ba, int bb, int mod) {
        return new ModLinearFunction(DigitUtils.modsub(a.a, ba, mod), DigitUtils.modsub(a.b, bb, mod));
    }

    public static ModLinearFunction mul(ModLinearFunction a, int b, int mod) {
        return new ModLinearFunction(DigitUtils.mod((long) a.a * b, mod), DigitUtils.mod((long) a.b * b, mod));
    }

    //ax+b=y => x=(y-b)/a
    public static ModLinearFunction inverse(ModLinearFunction func, int mod, Power pow) {
        int invA = pow.inverseExtGCD(func.a);
        return new ModLinearFunction(invA, DigitUtils.mod((long) -func.b * invA, mod));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ModLinearFunction)) {
            return false;
        }
        ModLinearFunction function = (ModLinearFunction) obj;
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