package template.math;

public class MultiplicativeFunction {
    private static MultiplicativeFunctionCalculator euler = new MultiplicativeFunctionCalculator((a, b) -> b - b / a);

    public static int eulerFunction(int x) {
        return euler.find(x);
    }
}
