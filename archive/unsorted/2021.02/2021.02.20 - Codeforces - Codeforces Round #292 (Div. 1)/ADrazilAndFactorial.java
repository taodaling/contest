package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ADrazilAndFactorial {
    public int fact(int n) {
        return n == 0 ? 1 : n * fact(n - 1);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] factor = new int[10];
        int[] primes = new int[]{2, 3, 5, 7};
        for (int i = 0; i < n; i++) {
            int x = in.rc() - '0';
            int t = fact(x);
            for (int p : primes) {
                while (t % p == 0) {
                    t /= p;
                    factor[p]++;
                }
            }
        }
        factor[6] = factor[7];
        factor[4] = factor[5];
        factor[3] -= factor[6];
        factor[2] -= factor[4] * 2 + factor[6];

        for (int i = 7; i >= 0; i--) {
            int req = factor[i] - factor[i + 1];
            while (req > 0) {
                out.append(i);
                req--;
            }
        }
    }
}
