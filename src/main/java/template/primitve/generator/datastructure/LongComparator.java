package template.primitve.generated.datastructure;

public interface LongComparator {
    public int compare(long a, long b);

    public static final LongComparator NATURE_ORDER = (a, b) -> Long.compare(a, b);
}
