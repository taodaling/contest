package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;

public class PrimeGame {
    int limit = (int) 1e6;
    int[] primeCnt = new int[limit + 1];
    EulerSieve sieve = new EulerSieve(limit);

    {
        for (int i = 1; i <= limit; i++) {
            if (sieve.isPrime(i)) {
                primeCnt[i] = 1;
            }
            primeCnt[i] += primeCnt[i - 1];
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.ri();
        int y = in.ri();
        if (primeCnt[x] <= y) {
            out.println("Chef");
        } else {
            out.println("Divyam");
        }
    }
}
