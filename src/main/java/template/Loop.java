package template;

public class Loop<T> {
    T[] data;
    int pos;

    public Loop(T... data) {
        this.data = data;
    }

    public T turn(int i) {
        pos += i;
        return get(0);
    }

    public T get(int i) {
        return data[(pos + i) % data.length];
    }
}
