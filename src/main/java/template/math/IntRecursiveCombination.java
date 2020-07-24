package template.math;

public class IntRecursiveCombination implements IntCombination {
    private Modular mod;
    private Power pow;

    public IntRecursiveCombination(Power pow) {
        this.pow = pow;
        this.mod = pow.getModular();
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
            return mod.valueOf(1);
        }
        int ans = comb(m - 1, n - 1);
        ans = mod.mul(ans, m);
        ans = mod.mul(ans, pow.inverse(n));
        return ans;
    }
}
