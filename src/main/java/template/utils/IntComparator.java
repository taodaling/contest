package template.utils;

public interface IntComparator {
    public int compare(int a, int b);

    public static final IntComparator NATURE_ORDER = (a, b) -> Integer.compare(a, b);
}
