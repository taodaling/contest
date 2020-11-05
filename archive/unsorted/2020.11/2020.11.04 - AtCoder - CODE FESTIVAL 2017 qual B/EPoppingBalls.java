package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Factorial;

public class EPoppingBalls {
    int mod = (int) 1e9 + 7;
    Factorial fact = new Factorial(10000, mod);
    Combination comb = new Combination(fact);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();

        //use all at once
        //x1 + x2 = a
        long sum1 = 0;
        sum1 += comb.combination(a + 1, 1);

        //not all at first
        long sum2 = 0;
        for (int i = 1; i < b; i++) {
            for (int j = 1; j + i <= b; j++) {
                int remain = a - (b + b - i - i - j);
                long contrib = (long) comb.combination(b - 1, i - 1)
                        * comb.combination(b - i - 1, j - 1) % mod
                        //x1 + x2 + x3 = remain
                        * comb.combination(remain + 2, 2) % mod;
                sum2 += contrib;
            }
        }
        sum2 %= mod;


        long ans = sum1 + sum2;
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
