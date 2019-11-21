package template.datastructure;

public interface IntEntryIterator {
    boolean hasNext();
    void next();
    int getEntryKey();
    int getEntryValue();
}
