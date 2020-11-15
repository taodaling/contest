package template.math;

/**
 * Find all inverse number
 */
public class ModPrimeInverseNumber implements InverseNumber {
    int[] inv;

    public ModPrimeInverseNumber(int[] inv, int limit, int mod) {
        this.inv = inv;
        inv[1] = 1;
        int p = mod;
        for (int i = 2; i <= limit; i++) {
            int k = p / i;
            int r = p - k * i;
            inv[i] = DigitUtils.mod((long) -k * inv[r], mod);
        }
    }

    public ModPrimeInverseNumber(int limit, int mod) {
        this(new int[limit + 1], limit, mod);
    }

    public int inverse(int x) {
        return inv[x];
    }
}
