package template.graph;

public abstract class TwoSat {
    public int elementId(int x) {
        return x << 1;
    }

    public int negateElementId(int x) {
        return (x << 1) | 1;
    }

    public int negate(int x) {
        return x ^ 1;
    }

    protected abstract void addRely(int a, int b);

    public void deduce(int a, int b) {
        addRely(a, b);
        addRely(negate(b), negate(a));
    }

    public void or(int a, int b) {
        deduce(negate(a), b);
    }

    public void isTrue(int a) {
        addRely(negate(a), a);
    }

    public void isFalse(int a) {
        addRely(a, negate(a));
    }

    public void same(int a, int b) {
        deduce(a, b);
        deduce(b, a);
    }

    public void xor(int a, int b) {
        same(a, negate(b));
    }

    public void atLeastOneIsFalse(int a, int b) {
        deduce(a, negate(b));
    }

    public void atLeastOneIsTrue(int a, int b) {
        or(a, b);
    }

    public abstract boolean[] solve();
}
