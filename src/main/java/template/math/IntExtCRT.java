package template.math;

public class IntExtCRT {
    /**
     * remainder
     */
    int r;
    /**
     * modulus
     */
    int m;
    static final IntExtGCDObject gcd = new IntExtGCDObject();

    public IntExtCRT() {
        r = 0;
        m = 1;
    }

    public int getValue() {
        return r;
    }

    public int getModular() {
        return m;
    }


    /**
     * Add a new condition: x % m = r
     */
    public boolean add(int r, int m) {
        int m1 = this.m;
        int x1 = this.r;
        int m2 = m;
        int x2 = DigitUtils.mod(r, m);
        int g = gcd.extgcd(m1, m2);
        int a = (gcd.getX() % m2);
        if ((x2 - x1) % g != 0) {
            return false;
        }
        this.m = m1 / g * m2;
        this.r = DigitUtils.mod((long) a * ((x2 - x1) / g) % this.m * m1 % this.m + x1, this.m);
        return true;
    }

    @Override
    public String toString() {
        return String.format("%d mod %d", r, m);
    }
}
