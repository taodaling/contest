package template.utils;

import java.util.function.Supplier;

public interface UpdatableSum<S, U> extends Cloneable, Sum<S> {

    void update(U u);

    public static class NIL<U> implements UpdatableSum<NIL<U>, U> {
        public static final NIL INSTANCE = new NIL();
        public static final Supplier<NIL> SUPPLIER = () -> INSTANCE;

        private NIL() {
        }

        @Override
        public void add(NIL<U> right) {
        }

        @Override
        public void update(U u) {
        }

        @Override
        public void copy(NIL<U> right) {
        }

        @Override
        public NIL<U> clone() {
            return this;
        }
    }
}
