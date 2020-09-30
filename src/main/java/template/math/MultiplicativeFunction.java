package template.math;

public class MultiplicativeFunction {
    private static IntegerMultiplicativeFunctionCalculator euler = new IntegerMultiplicativeFunctionCalculator((a, b) -> b - b / a);

    public static int eulerFunction(int x) {
        return euler.find(x);
    }
}
