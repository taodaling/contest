package template.utils;

public interface AlgebraController<T, U> {
    T alloc();

    default void release(T t) {
    }

    default T plus(T a, T b) {
        throw new UnsupportedOperationException();
    }

    default void modify(T x, U u) {
        throw new UnsupportedOperationException();
    }

    default boolean ofBoolean(T x) {
        return false;
    }

    default T replace(T a, T b) {
        release(b);
        return a;
    }

    default T replace(T a, T b0, T b1) {
        release(b0);
        release(b1);
        return a;
    }

    default T replace(T a, T... b) {
        for (T x : b) {
            release(x);
        }
        return a;
    }

    T clone(T t);

    default Class<T> classOf() {
        throw new UnsupportedOperationException();
    }
}
