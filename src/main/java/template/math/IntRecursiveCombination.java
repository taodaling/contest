package template.math;

public class IntRecursiveCombination implements IntCombination {
    private int mod;
    private InverseNumber inv;

    public IntRecursiveCombination(InverseNumber inv, int mod) {
        this.inv = inv;
        this.mod = mod;
    }

    @Override
    public int combination(int m, int n) {
        if (m < n || n < 0) {
            return 0;
        }
        if (m - n < n) {
            n = m - n;
        }
        return comb(m, n);
    }

    private int comb(int m, int n) {
        if (n == 0) {
            return 1 % mod;
        }
        long ans = 1;
        while (n != 0) {
            ans = ans * m % mod * inv.inverse(n) % mod;
            m--;
            n--;
        }
        return (int) ans;
    }
}
