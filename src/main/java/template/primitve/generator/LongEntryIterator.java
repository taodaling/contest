package template.primitve.generator;

public interface LongEntryIterator {
    boolean hasNext();

    void next();

    long getEntryKey();

    long getEntryValue();
}
