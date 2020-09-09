package template.primitve.generated.graph;

public interface DoubleAugmentCallback {
    public void callback(double flow, double pathCost);

    public static DoubleAugmentCallback NIL = (a, b) -> {
    };
}