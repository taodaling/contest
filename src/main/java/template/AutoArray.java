package template;

public class AutoArray<T> {
    T[] data;
    int size;

    public AutoArray(T[] data) {
        this.data = data;
    }

    public T[] toArray() {
        return data;
    }

    public int size() {
        return size;
    }

    public void push(T v) {
        data[size++] = v;
    }

    public T pop() {
        return data[--size];
    }
}
