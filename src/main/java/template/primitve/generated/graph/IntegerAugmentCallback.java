package template.primitve.generated.graph;

public interface IntegerAugmentCallback {
    public void callback(int flow, int pathCost);

    public static IntegerAugmentCallback NIL = (a, b) -> {
    };
}