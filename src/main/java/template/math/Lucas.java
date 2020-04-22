package template.math;

/**
 * Lucas algorithm
 */
public class Lucas implements IntCombination, LongCombination {
    private final IntCombination primeCombination;
    private int mod;


    public Lucas(IntCombination primeCombination, int mod) {
        this.primeCombination = primeCombination;
        this.mod = mod;
    }

    @Override
    public int combination(int m, int n) {
        return combination((long) m, (long) n);
    }



    /**
     * O(\log_p \min(n, m))
     */
    public int combination(long m, long n) {
        if (n == 0) {
            return 1 % mod;
        }
        return (int) ((long) primeCombination.combination((int) (m % mod), (int) (n % mod)) *
                combination(m / mod, n / mod) % mod);
    }
}
