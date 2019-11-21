package contest;

import template.FastInput;
import template.FastOutput;
import template.IntList;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long gcd = n;
        Gcd g = new Gcd();
        for (long i = 2; i * i <= n; i++) {
            if (n % i != 0) {
                continue;
            }
            gcd = g.gcd(gcd, i);
            gcd = g.gcd(gcd, n / i);
        }

        out.println(gcd);
    }
}
