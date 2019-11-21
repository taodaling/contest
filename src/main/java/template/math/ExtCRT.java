package template.math;

import java.math.BigInteger;

/**
 * Extend chinese remainder theory
 */
public class ExtCRT {
    /**
     * remainder
     */
    long r;
    /**
     * modulus
     */
    long m;
    ExtGCD gcd = new ExtGCD();

    public ExtCRT() {
        r = 0;
        m = 1;
    }

    /**
     * Add a new condition: x % m = r
     */
    public boolean add(long r, long m) {
        long m1 = this.m;
        long x1 = this.r;
        long m2 = m;
        long x2 = ((r % m) + m) % m;
        long g = gcd.extgcd(m1, m2);
        long a = gcd.getX();
        if ((x2 - x1) % g != 0) {
            return false;
        }
        this.m = m1 / g * m2;
        this.r = BigInteger.valueOf(a).multiply(BigInteger.valueOf((x2 - x1) / g)).multiply(BigInteger.valueOf(m1))
                        .add(BigInteger.valueOf(x1)).mod(BigInteger.valueOf(this.m)).longValue();
        return true;
    }
}
