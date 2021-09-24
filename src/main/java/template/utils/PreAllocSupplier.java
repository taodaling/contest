package template.utils;

import java.util.function.Supplier;

public class PreAllocSupplier<T> implements Supplier<T> {
    T[] data;
    int offset;

    public PreAllocSupplier() {
    }

    public void init(T[] data) {
        this.data = data;
        offset = 0;
    }

    @Override
    public T get() {
        T ans = data[offset++];
        return ans;
    }
}
