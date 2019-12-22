package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        if (n % 2 == 1) {
            out.println(0);
            return;
        }
        n /= 2;
        long two = count(n, 2);
        long five = count(n, 5);
        out.println(Math.min(two, five));
    }

    public long count(long x, long d) {
        long ans = 0;
        while (x > 0) {
            ans += x / d;
            x /= d;
        }
        return ans;
    }
}
