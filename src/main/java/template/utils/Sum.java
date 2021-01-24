package template.utils;

import java.util.function.Supplier;

public interface Sum<S, U> extends Cloneable {
    void add(S s);

    void update(U u);

    /**
     * copy s.data
     *
     * @param s
     */
    void copy(S s);

    S clone();

    static class NilSum<U> implements Sum<NilSum<U>, U> {
        public static final NilSum INSTANCE = new NilSum();
        public static final Supplier<NilSum> SUPPLIER = () -> INSTANCE;

        private NilSum() {
        }

        @Override
        public void add(NilSum<U> uNilSum) {
        }

        @Override
        public void update(U u) {
        }

        @Override
        public void copy(NilSum<U> uNilSum) {
        }

        @Override
        public NilSum<U> clone() {
            return this;
        }
    }
}
