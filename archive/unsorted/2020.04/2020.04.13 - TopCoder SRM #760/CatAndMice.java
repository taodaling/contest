package contest;

import template.math.EulerSieve;
import template.math.MultiplicativeFunction;
import template.math.MultiplicativeFunctionSieve;

public class CatAndMice {
    public long countDirections(int N, int C) {
        if (C == N) {
            return 8;
        }
        long ans = 0;
        MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve(N, false, true, false);
        for (int y = 1; y <= N; y++) {
            if (N / y == C) {
                ans += sieve.euler[y];
            }
        }
        return ans * 8;
    }
}
