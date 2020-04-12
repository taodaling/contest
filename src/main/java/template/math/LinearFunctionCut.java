package template.math;

public class LinearFunctionCut {
    private long l;
    private long r;

    public LinearFunctionCut(long l, long r) {
        this.l = l;
        this.r = r;
    }

    public LinearFunctionCut() {
        this(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public boolean valid() {
        return r >= l;
    }

    public long getL() {
        return l;
    }

    public long getR() {
        return r;
    }

    public void reset(long l, long r) {
        this.l = l;
        this.r = r;
    }

    public void reset() {
        reset(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    /**
     * kx >= y
     */
    public void greaterThanOrEqual(long k, long y) {
        if (k < 0) {
            lessThanOrEqual(-k, -y);
            return;
        }
        if (k == 0) {
            if (y > 0) {
                l = 0;
                r = -1;
            }
            return;
        }
        l = Math.max(l, DigitUtils.ceilDiv(y, k));
    }

    /**
     * kx + b >= y
     */
    public void greaterThanOrEqual(long k, long b, long y) {
        greaterThanOrEqual(k, y - b);
    }

    /**
     * kx <= y
     */
    public void lessThanOrEqual(long k, long y) {
        if (k < 0) {
            greaterThanOrEqual(-k, -y);
            return;
        }
        if (k == 0) {
            if (y < 0) {
                l = 0;
                r = -1;
            }
            return;
        }
        r = Math.min(r, DigitUtils.floorDiv(y, k));
    }

    /**
     * l <= kx <= r
     */
    public void between(long k, long l, long r) {
        if (k < 0) {
            between(-k, -r, -l);
            return;
        }
        greaterThanOrEqual(k, l);
        lessThanOrEqual(k, r);
    }

    /**
     * l <= kx + b <= r
     */
    public void between(long k, long b, long l, long r) {
        greaterThanOrEqual(k, b, l);
        lessThanOrEqual(k, b, r);
    }

    /**
     * kx + b <= y
     */
    public void lessThanOrEqual(long k, long b, long y) {
        lessThanOrEqual(k, y - b);
    }

    public void greaterThan(long k, long y) {
        greaterThanOrEqual(k, y + 1);
    }

    public void greaterThan(long k, long b, long y) {
        greaterThan(k, y - b);
    }


    public void lessThan(long k, long y) {
        lessThanOrEqual(k, y - 1);
    }

    public void lessThan(long k, long b, long y) {
        lessThan(k, y - b);
    }

    public long length() {
        return r < l ? 0 : r - l + 1;
    }

    @Override
    public String toString() {
        return "[" + l + "," + r + "]";
    }
}
