package template.math;

/**
 * Lucas algorithm
 */
public class Lucas {
    private final Combination combination;
    private int modulus;

    public Lucas(Combination combination) {
        this.combination = combination;
        this.modulus = combination.modular.m;
    }

    /**
     * O(\log_p \min(n, m))
     */
    public int composite(long m, long n) {
        if (n == 0) {
            return 1;
        }
        return combination.modular.mul(combination.combination((int) (m % modulus), (int) (n % modulus)),
                        composite(m / modulus, n / modulus));
    }
}
