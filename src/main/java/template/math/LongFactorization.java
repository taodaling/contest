package template.math;

import java.util.Arrays;

public class LongFactorization {
    public long g;
    public long[] primes;
    int numberOfFactors = -1;

    public LongFactorization(long g) {
        init(g);
    }

    public void init(long g) {
        assert 0 < g && g <= 1e18;
        this.g = g;
        primes = LongPollardRho.findAllFactors(g).stream().mapToLong(Long::valueOf).toArray();
        numberOfFactors = -1;
    }

    public int numberOfFactors() {
        if (numberOfFactors == -1) {
            int ans = 1;
            for (long p : primes) {
                long now = g;
                int pow = 0;
                while (now % p == 0) {
                    pow++;
                    now /= p;
                }
                ans *= pow + 1;
            }
            numberOfFactors = ans;
        }
        return numberOfFactors;
    }

    @Override
    public String toString() {
        return g + "=>" + Arrays.toString(primes);
    }
}
