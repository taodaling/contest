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

    public T turn() {
        return turn(1);
    }

    public T get(int i) {
        return data[(pos + i) % data.length];
    }

    public T get() {
        return get(0);
    }
}
