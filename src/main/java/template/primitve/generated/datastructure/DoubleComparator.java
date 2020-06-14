package template.primitve.generated.datastructure;

public interface DoubleComparator {
    public int compare(double a, double b);

    public static final DoubleComparator NATURE_ORDER = (a, b) -> Double.compare(a, b);

    public static final DoubleComparator REVERSE_ORDER = (a, b) -> Double.compare(b, a);
}
