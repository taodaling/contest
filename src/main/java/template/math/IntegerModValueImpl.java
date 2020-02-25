package template.math;

public class IntegerModValueImpl implements IntegerModValue {
    private int x;

    public IntegerModValueImpl(int x) {
        this.x = x;
    }

    @Override
    public int get(int mod) {
        return DigitUtils.mod(x, mod);
    }

    @Override
    public boolean isZero() {
        return x == 0;
    }

    @Override
    public int limit(int limit) {
        return Math.min(x, limit);
    }
}
