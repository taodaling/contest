package template.datastructure;

public interface IntDeque extends IntStack{
    void addFirst(int x);
    int removeFirst();
    int peekFirst();
    IntIterator iterator();
}
