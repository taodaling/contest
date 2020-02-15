package template.math;

import java.util.Arrays;
import java.util.function.IntConsumer;


public class EratosthenesSieve {
    /**
     * Find all primes in [2, n] and consume them.
     * The time complexity is O(n log log n) and the space complexity is O(sqrt(n))
     */
    public static void sieve(IntConsumer consumer, int n) {
        if (n <= 1) {
            return;
        }
        int block = DigitUtils.roundToInt(Math.sqrt(n));
        boolean[] isComp = new boolean[block + 1];
        int[] primes = new int[block + 1];
        int primeCnt = 0;
        for (int i = 2; i <= block; i++) {
            if (isComp[i]) {
                continue;
            }
            primes[primeCnt++] = i;
            for (int j = i + i; j <= block; j += i) {
                isComp[j] = true;
            }
        }
        for (int i = 0; i < primeCnt; i++) {
            int p = primes[i];
            consumer.accept(p);
        }
        for (int l = block + 1; l <= n; l += block) {
            int r = Math.min(l + block - 1, n);
            Arrays.fill(isComp, false);
            for (int i = 0; i < primeCnt; i++) {
                int p = primes[i];
                if (r < p * p) {
                    break;
                }
                int top = Math.max(0, l - p * p);
                int bot = p;
                for (int j = (top + bot - 1) / bot * p + p * p; j <= r; j += p) {
                    isComp[j - l] = true;
                }
            }
            for (int j = l; j <= r; j++) {
                if (!isComp[j - l]) {
                    consumer.accept(j);
                }
            }
        }
    }
}