package template.math;

import java.math.BigInteger;

public class BigIntLinearFunctionCut {
    private BigInteger l;
    private BigInteger r;

    public BigIntLinearFunctionCut(BigInteger l, BigInteger r) {
        this.l = l;
        this.r = r;
    }

    public boolean valid() {
        return r.compareTo(l) > 0;
    }

    public BigInteger getL() {
        return l;
    }

    public BigInteger getR() {
        return r;
    }

    /**
     * kx >= y
     */
    public void greaterThanOrEqual(BigInteger k, BigInteger y) {
        if (k.signum() < 0) {
            lessThanOrEqual(k.negate(), y.negate());
            return;
        }
        if (k.signum() == 0) {
            if (y.signum() > 0) {
                l = BigInteger.valueOf(0);
                r = BigInteger.valueOf(-1);
            }
            return;
        }
        l = max(l, y.divide(k));
    }

    private static BigInteger max(BigInteger a, BigInteger b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    private static BigInteger min(BigInteger a, BigInteger b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    /**
     * kx + b >= y
     */
    public void greaterThanOrEqual(BigInteger k, BigInteger b, BigInteger y) {
        greaterThanOrEqual(k, y.subtract(b));
    }

    /**
     * kx <= y
     */
    public void lessThanOrEqual(BigInteger k, BigInteger y) {
        if (k.signum() < 0) {
            greaterThanOrEqual(k.negate(), y.negate());
            return;
        }
        if (k.signum() == 0) {
            if (y.signum() < 0) {
                l = BigInteger.valueOf(0);
                r = BigInteger.valueOf(-1);
            }
            return;
        }
        r = min(r, y.divide(k));
    }

    /**
     * l <= kx <= r
     */
    public void between(BigInteger k, BigInteger l, BigInteger r) {
        if (k.signum() < 0) {
            between(k.negate(), r.negate(), l.negate());
            return;
        }
        greaterThanOrEqual(k, l);
        lessThanOrEqual(k, r);
    }

    /**
     * l <= kx + b <= r
     */
    public void between(BigInteger k, BigInteger b, BigInteger l, BigInteger r) {
        greaterThanOrEqual(k, b, l);
        lessThanOrEqual(k, b, r);
    }

    /**
     * kx + b <= y
     */
    public void lessThanOrEqual(BigInteger k, BigInteger b, BigInteger y) {
        lessThanOrEqual(k, y.subtract(b));
    }

    public void greaterThan(BigInteger k, BigInteger y) {
        greaterThanOrEqual(k, y.add(BigInteger.valueOf(1)));
    }

    public void greaterThan(BigInteger k, BigInteger b, BigInteger y) {
        greaterThan(k, y.subtract(b));
    }


    public void lessThan(BigInteger k, BigInteger y) {
        lessThanOrEqual(k, y.subtract(BigInteger.valueOf(1)));
    }

    public void lessThan(BigInteger k, BigInteger b, BigInteger y) {
        lessThan(k, y.subtract(b));
    }

    public BigInteger length() {
        return !valid() ? BigInteger.valueOf(0) : r.subtract(l).add(BigInteger.valueOf(1));
    }

    @Override
    public String toString() {
        return "[" + l + "," + r + "]";
    }
}
