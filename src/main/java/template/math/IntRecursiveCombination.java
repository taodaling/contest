package template.math;

public class IntRecursiveCombination implements IntCombination {
    private int mod;
    private Power pow;

    public IntRecursiveCombination(Power pow) {
        this.pow = pow;
        this.mod = pow.getMod();
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
        long ans = comb(m - 1, n - 1);
        ans = ans * m % mod * pow.inverse(n) % mod;
        return (int) ans;
    }
}
