package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongMultiplicativeFunctionCalculator;

public class ETheHolmesChildren {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long k = in.readLong();

        long threshold = 160;
        if (k > threshold) {
            out.println(1);
            return;
        }

        long ans = solve(n, k);
        out.println(ans % (1000000007   ));
    }

    LongMultiplicativeFunctionCalculator calculator = new LongMultiplicativeFunctionCalculator((a, b) -> {
        return b - b / a;
    });

    public long solve(long n, long k) {
        if (k == 1) {
            return calculator.find(n);
        }
        if (k % 2 == 0) {
            return solve(n, k - 1);
        }
        long ret = solve(n, k - 1);
        if (ret <= 1) {
            return 1;
        }
        return calculator.find(ret);
    }
}
