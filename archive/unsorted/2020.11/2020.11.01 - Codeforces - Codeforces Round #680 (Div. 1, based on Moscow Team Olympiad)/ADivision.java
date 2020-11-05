package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.math.LongPollardRho;
import template.primitve.generated.datastructure.IntegerArrayList;

public class ADivision {
    public int log(long n, int x) {
        int ans = 0;
        while (n % x == 0) {
            n /= x;
            ans++;
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long p = in.readLong();
        int q = in.readInt();
        IntegerArrayList list = Factorization.factorizeNumberPrime(q);
        long ans = 0;
        for (int x : list.toArray()) {
            int logp = log(p, x);
            int logq = log(q, x);
            long cur = p;
            while (logp >= logq) {
                logp--;
                cur /= x;
            }
            ans = Math.max(ans, cur);
        }
        out.println(ans);
    }
}
