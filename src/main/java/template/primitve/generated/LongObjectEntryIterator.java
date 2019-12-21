package template.primitve.generated;

public interface LongObjectEntryIterator<V> {
    boolean hasNext();

    void next();

    long getEntryKey();

    V getEntryValue();
}
