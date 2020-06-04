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

    public int apply(int x, Modular mod) {
        return mod.valueOf((long) a * x + b);
    }

    //a(b) = a.a(b.a x + b.b) + a.b
    public static LinearFunction merge(LinearFunction a, LinearFunction b, Modular mod) {
        return new LinearFunction(mod.valueOf((long) a.a * b.a), mod.valueOf((long) a.a * b.b + a.b));
    }

    public static LinearFunction plus(LinearFunction a, LinearFunction b, Modular mod) {
        return new LinearFunction(mod.plus(a.a, b.a), mod.plus(a.b, b.b));
    }

    public static LinearFunction plus(LinearFunction a, int ba, int bb, Modular mod) {
        return new LinearFunction(mod.plus(a.a, ba), mod.plus(a.b, bb));
    }

    public static LinearFunction subtract(LinearFunction a, LinearFunction b, Modular mod) {
        return new LinearFunction(mod.subtract(a.a, b.a), mod.subtract(a.b, b.b));
    }

    public static LinearFunction subtract(LinearFunction a, int ba, int bb, Modular mod) {
        return new LinearFunction(mod.subtract(a.a, ba), mod.subtract(a.b, bb));
    }

    public static LinearFunction mul(LinearFunction a, int b, Modular mod) {
        return new LinearFunction(mod.mul(a.a, b), mod.mul(a.b, b));
    }

    //ax+b=y => x=(y-b)/a
    public static LinearFunction inverse(LinearFunction func, Modular mod, Power pow) {
        int invA = pow.inverseExtGCD(func.a);
        return new LinearFunction(invA, mod.valueOf((long) -func.b * invA));
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