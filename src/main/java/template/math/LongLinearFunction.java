package template.math;

public class LongLinearFunction {
    public final long a;
    public final long b;

    public LongLinearFunction(long a, long b) {
        this.a = a;
        this.b = b;
    }


    public LongLinearFunction(long b) {
        this(0, b);
    }

    public LongLinearFunction plus(LongLinearFunction x) {
        return new LongLinearFunction(a + x.a, b + x.b);
    }

    public LongLinearFunction sub(LongLinearFunction x) {
        return new LongLinearFunction(a - x.a, b - x.b);
    }

    @Override
    public String toString() {
        return String.format("%dx+%d", a, b);
    }
}
