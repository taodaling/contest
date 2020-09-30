package template.math;

import java.util.Set;
import java.util.function.ToLongBiFunction;

public class LongMultiplicativeFunctionCalculator {
    private final ToLongBiFunction<Long, Long> function;

    public LongMultiplicativeFunctionCalculator(ToLongBiFunction<Long, Long> function) {
        this.function = function;
    }

    public long find(long x) {
        Set<Long> set = LongPollardRho.findAllFactors(x);
        long ans = 1;
        for (long factor : set) {
            long exp = 1;
            while (x % factor == 0) {
                x /= factor;
                exp *= factor;
            }
            ans *= function.applyAsLong(factor, exp);
        }
        return ans;
    }
}
