package template.primitve.generated.graph;

public interface DoubleAugmentCallback {
    /**
     * @return continue augmenting or not
     */
    public boolean callback(double flow, double pathCost);

    public static DoubleAugmentCallback NIL = (a, b) -> {
        return true;
    };
}