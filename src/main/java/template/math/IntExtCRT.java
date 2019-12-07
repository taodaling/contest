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
    boolean valid = true;
    static final ExtGCD gcd = new ExtGCD();

    public IntExtCRT() {
        r = 0;
        m = 1;
    }

    public int getValue() {
        return r;
    }

    public boolean isValid() {
        return valid;
    }

    /**
     * Add a new condition: x % m = r
     */
    public boolean add(int r, int m) {
        int m1 = this.m;
        int x1 = this.r;
        int m2 = m;
        int x2 = DigitUtils.mod(r, m);
        int g = (int) gcd.extgcd(m1, m2);
        int a = (int) (gcd.getX() % m2);
        if ((x2 - x1) % g != 0) {
            return valid = false;
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
