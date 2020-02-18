package template.primitve.generated.datastructure;

public interface DoubleObjectEntryIterator<V> {
    boolean hasNext();

    void next();

    double getEntryKey();

    V getEntryValue();
}
