package template.primitve.generated.graph;

public interface LongAugmentCallback {
    /**
     * @return continue augmenting or not
     */
    public boolean callback(long flow, long pathCost);

    public static LongAugmentCallback NIL = (a, b) -> {
        return true;
    };
}