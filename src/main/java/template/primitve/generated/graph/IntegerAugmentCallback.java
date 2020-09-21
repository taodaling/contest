package template.primitve.generated.graph;

public interface IntegerAugmentCallback {
    /**
     * @return continue augmenting or not
     */
    public boolean callback(int flow, int pathCost);

    public static IntegerAugmentCallback NIL = (a, b) -> {
        return true;
    };
}