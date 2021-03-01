package template.math;

/**
 * Extend chinese remainder theory
 */
public class ExtCRT {
    /**
     * remainder
     */
    public long r;
    /**
     * modulus
     */
    public long m;
    static final LongExtGCDObject gcd = new LongExtGCDObject();


    public ExtCRT() {
        r = 0;
        m = 1;
    }

    public void set(long r, long m) {
        this.r = r;
        this.m = m;
    }

    public void clear(){
        set(0, 1);
    }

    public long getValue() {
        return r;
    }

    /**
     * Add a new condition: x % m = r
     */
    public boolean add(long r, long m) {
        long m1 = this.m;
        long x1 = this.r;
        long m2 = m;
        long x2 = DigitUtils.mod(r, m);
        long g = gcd.extgcd(m1, m2);
        long a = gcd.getX();
        if ((x2 - x1) % g != 0) {
            return false;
        }
        this.m = m1 / g * m2;
        ILongModular modular = ILongModular.getInstance(this.m);
        this.r = modular.plus(modular.mul(modular.mul(modular.valueOf(a), modular.valueOf((x2 - x1) / g)), m1), x1);
//        this.r = BigInteger.valueOf(a).multiply(BigInteger.valueOf((x2 - x1) / g)).multiply(BigInteger.valueOf(m1))
//                .add(BigInteger.valueOf(x1)).mod(BigInteger.valueOf(this.m)).longValue();
        return true;
    }

    @Override
    public String toString() {
        return String.format("%d mod %d", r, m);
    }
}
