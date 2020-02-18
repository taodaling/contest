package template.primitve.generated.datastructure;

public interface IntegerComparator {
    public int compare(int a, int b);

    public static final IntegerComparator NATURE_ORDER = (a, b) -> Integer.compare(a, b);
}
