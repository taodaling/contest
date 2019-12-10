package template.math;

import template.utils.IntToIntFunction;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.ToIntBiFunction;

public class MultiplicativeFunctionCalculator {
    private final ToIntBiFunction<Integer, Integer> function;
    private PollardRho rho = new PollardRho();

    /**
     * A simple function used to
     *
     * @param function
     */
    public MultiplicativeFunctionCalculator(ToIntBiFunction<Integer, Integer> function) {
        this.function = function;
    }

    public int find(int x) {
        Map<Integer, Integer> map = rho.findAllFactors(x);
        int ans = 1;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            ans *= function.applyAsInt(entry.getKey(), entry.getValue());
        }
        return ans;
    }
}
