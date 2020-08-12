package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.math.BigInteger;

public class APlusAndSquareRoot {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] ans = new long[n + 1];
        long x = 2;
        for (int i = 1; i <= n; i++) {
            long k = (long)i * (i + 1) * (i + 1) - x;
            ans[i] = k;
            out.println(k);
            x = i;
        }

        //check(n, ans);
        //check(n, new long[]{0, 14, 16, 46});
    }

    public void check(int n, long[] ans) {
        long cur = 2;
        int k = 1;
        while (k <= n) {
            long step = ans[k];
            cur += step * k;
            long sqrt = Math.round(Math.sqrt(cur));
            if (sqrt * sqrt != cur || sqrt % (k + 1) != 0) {
                throw new RuntimeException();
            }
            cur = sqrt;
            k++;
        }
    }
}
