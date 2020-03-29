package template.math;

import template.math.Composite;

/**
 * Lucas algorithm
 */
public class Lucas {
    private final Composite composite;
    private int modulus;

    public Lucas(Composite composite) {
        this.composite = composite;
        this.modulus = composite.modular.m;
    }

    /**
     * O(\log_p \min(n, m))
     */
    public int composite(long m, long n) {
        if (n == 0) {
            return 1;
        }
        return composite.modular.mul(composite.composite((int) (m % modulus), (int) (n % modulus)),
                        composite(m / modulus, n / modulus));
    }
}
