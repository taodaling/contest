package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongPollardRho;
import template.problem.PartitionNumber;

import java.math.BigInteger;
import java.util.Set;

public class AbelianGroups {
    BigInteger[][] pn = new BigInteger[50][50];

    public BigInteger pn(int n, int i) {
        if (i == 0) {
            return BigInteger.valueOf(n == 0 ? 1 : 0);
        }
        if (pn[n][i] == null) {
            BigInteger ans = BigInteger.valueOf(0);
            for (int j = 0; j <= n; j += i) {
                ans = ans.add(pn(n - j, i - 1));
            }
            pn[n][i] = ans;
        }
        return pn[n][i];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        Set<Long> set = LongPollardRho.findAllFactors(n);
        BigInteger prod = BigInteger.valueOf(1);
        for (long x : set) {
            int pow = 0;
            while (n % x == 0) {
                pow++;
                n /= x;
            }
            prod = prod.multiply(pn(pow, pow));
        }
        out.println(prod);
    }
}
