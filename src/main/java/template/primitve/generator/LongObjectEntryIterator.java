package template.primitve.generator;

public interface LongObjectEntryIterator<V> {
    boolean hasNext();

    void next();

    long getEntryKey();

    V getEntryValue();
}
