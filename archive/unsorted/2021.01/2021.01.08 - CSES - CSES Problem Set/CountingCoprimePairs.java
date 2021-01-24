package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class CountingCoprimePairs {
    public long choose2(long n) {
        return n * (n - 1) / 2;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int limit = (int) 1e6;
        int[] c = new int[limit + 1];
        for (int i = 0; i < n; i++) {
            c[in.ri()]++;
        }
        long[] multiple = new long[limit + 1];
        for (int i = 1; i <= limit; i++) {
            for (int j = i; j <= limit; j += i) {
                multiple[i] += c[j];
            }
        }
        long[] gcd = new long[limit + 1];
        for (int i = limit; i >= 1; i--) {
            gcd[i] = choose2(multiple[i]);
            for (int j = i + i; j <= limit; j += i) {
                gcd[i] -= gcd[j];
            }
        }
        out.println(gcd[1]);
    }
}
