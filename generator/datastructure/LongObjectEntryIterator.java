package template.primitve.generated.datastructure;

public interface LongObjectEntryIterator<V> {
    boolean hasNext();

    void next();

    long getEntryKey();

    V getEntryValue();
}
