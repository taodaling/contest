package template.primitve.generated;

public interface DoubleComparator {
    public int compare(double a, double b);

    public static final DoubleComparator NATURE_ORDER = (a, b) -> Double.compare(a, b);
}
