package template.math;

public class DoubleLinearFunction {
    public final double a;
    public final double b;

    public static final DoubleLinearFunction IDENTITY = new DoubleLinearFunction(1, 0);
    public static final DoubleLinearFunction ONE = new DoubleLinearFunction(0, 1);
    public static final DoubleLinearFunction ZERO = new DoubleLinearFunction(0, 0);


    public DoubleLinearFunction(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public double apply(double x) {
        return a * x + b;
    }

    //a(b) = a.a(b.a x + b.b) + a.b
    public static DoubleLinearFunction merge(DoubleLinearFunction a, DoubleLinearFunction b) {
        return new DoubleLinearFunction(a.a * b.a, a.a * b.b + a.b);
    }

    public static DoubleLinearFunction plus(DoubleLinearFunction a, DoubleLinearFunction b) {
        return new DoubleLinearFunction(a.a + b.a, a.b + b.b);
    }

    public static DoubleLinearFunction plus(DoubleLinearFunction a, double ba, double bb) {
        return new DoubleLinearFunction(a.a + ba, a.b + bb);
    }

    public static DoubleLinearFunction subtract(DoubleLinearFunction a, DoubleLinearFunction b) {
        return new DoubleLinearFunction(a.a - b.a, a.b - b.b);
    }

    public static DoubleLinearFunction subtract(DoubleLinearFunction a, double ba, double bb) {
        return new DoubleLinearFunction(a.a - ba, a.b - bb);
    }

    public static DoubleLinearFunction mul(DoubleLinearFunction a, double b) {
        return new DoubleLinearFunction(a.a * b, a.b * b);
    }

    //ax+b=y => x=(y-b)/a
    public static DoubleLinearFunction inverse(DoubleLinearFunction func) {
        double invA = 1 / func.a;
        return new DoubleLinearFunction(invA, -func.b * invA);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DoubleLinearFunction)) {
            return false;
        }
        DoubleLinearFunction function = (DoubleLinearFunction) obj;
        return function.a == a && function.b == b;
    }


    @Override
    public int hashCode() {
        return Double.hashCode(a) * 31 + Double.hashCode(b);
    }

    @Override
    public String toString() {
        if (b >= 0) {
            return a + "x+" + b;
        }
        return a + "x" + b;
    }
}
