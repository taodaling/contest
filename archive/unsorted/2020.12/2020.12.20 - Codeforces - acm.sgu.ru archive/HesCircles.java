package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.math.GCDs;

import java.math.BigInteger;

public class HesCircles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        BigInteger sum = BigInteger.ZERO;
        BigInteger one = BigInteger.valueOf(1);
        int[] gcds = new int[n + 1];
        for (int i = 0; i < n; i++) {
            gcds[GCDs.gcd(i, n)]++;
        }
        for (int i = 1; i <= n; i++) {
            if (gcds[i] == 0) {
                continue;
            }
            sum = sum.add(BigInteger.valueOf(gcds[i]).shiftLeft(i));
        }
        BigInteger ans = sum.divide(BigInteger.valueOf(n));
        out.println(ans);
    }
}
