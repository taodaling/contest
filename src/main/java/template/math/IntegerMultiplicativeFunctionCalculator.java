package template.math;

import java.util.Map;
import java.util.Set;
import java.util.function.ToIntBiFunction;

public class IntegerMultiplicativeFunctionCalculator {
    private final ToIntBiFunction<Integer, Integer> function;
    private PollardRho rho = new PollardRho();

    /**
     * A simple function used to
     *
     * @param function
     */
    public IntegerMultiplicativeFunctionCalculator(ToIntBiFunction<Integer, Integer> function) {
        this.function = function;
    }

    public int find(int x) {
        Set<Integer> set = rho.findAllFactors(x);
        int ans = 1;
        for (int factor : set) {
            int exp = 1;
            while (x / exp % factor == 0) {
                exp *= factor;
            }
            ans *= function.applyAsInt(factor, exp);
        }
        return ans;
    }
}
