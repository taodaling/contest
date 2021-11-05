package template.math;

/**
 * Used to find k while x^k = y % p
 */
public class GenericModLog {
    private CoprimeModLog log;
    private int mul = 1;
    private int div = 1;
    private int k = 0;
    private int a;
    private int original;
    private int mod;
    private static IntExtGCDObject extGCD = new IntExtGCDObject();

    public GenericModLog(int a, int p) {
        a = DigitUtils.mod(a, p);
        original = p;
        int g;
        while ((g = GCDs.gcd(a, p)) != 1) {
            mul = (int) ((long) mul * a / g % original);
            div *= g;
            p /= g;
            k++;
        }
        this.a = a;
        this.mod = p;
        log = new CoprimeModLog(a, mod);
        extGCD.extgcd(mul, p);
        mul = DigitUtils.mod(extGCD.getX(), mod);
    }

    public int log(int y) {
        return log(y, 0);
    }

    /**
     * return log_a y \mod p with O(\sqrt{p}) time complexity or -1 if doesn't exit
     */
    public int log(int y, int atLeast) {
        y = DigitUtils.mod(y, original);
        long prod = DigitUtils.mod(1, original);
        for (int i = 0; i < k; i++) {
            if (prod == y && i >= atLeast) {
                return i;
            }
            prod = prod * a % original;
        }
        if (y % div != 0) {
            return -1;
        }

        y = (int) ((long) y / div * mul % mod);
        int ans = log.log(y, atLeast - k);
        if (ans >= 0) {
            ans += k;
        }
        return ans;
    }
}
