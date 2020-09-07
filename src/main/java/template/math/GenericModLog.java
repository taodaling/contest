package template.math;

/**
 * Used to find k while x^k = y % p
 */
public class GenericModLog {
    private RelativePrimeModLog log;
    private int mul = 1;
    private int div = 1;
    private int k = 0;
    private int a;
    private Modular original;
    private Modular modular;
    private static IntExtGCDObject extGCD = new IntExtGCDObject();

    public GenericModLog(int a, int p) {
        a = DigitUtils.mod(a, p);
        original = new Modular(p);
        int g;
        while ((g = GCDs.gcd(a, p)) != 1) {
            mul = original.mul(mul, a / g);
            div *= g;
            p /= g;
            k++;
        }
        this.a = a;
        this.modular = new Modular(p);
        log = new RelativePrimeModLog(a, modular);
        extGCD.extgcd(mul, p);
        mul = modular.valueOf(extGCD.getX());
    }

    /**
     * return log_a y \mod p with O(\sqrt{p}) time complexity or -1 if doesn't exit
     */
    public int log(int y) {
        y = original.valueOf(y);
        int prod = original.valueOf(1);
        for (int i = 0; i < k; i++) {
            if (prod == y) {
                return i;
            }
            prod = original.mul(prod, a);
        }
        if (y % div != 0) {
            return -1;
        }

        y = modular.mul(y / div, mul);
        int ans = log.log(y);
        if (ans >= 0) {
            ans += k;
        }
        return ans;
    }
}
