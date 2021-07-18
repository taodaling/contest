package template.utils;

import java.util.function.Supplier;

public interface Update<U extends Update<U>> extends Cloneable {
    void update(U u);

    void clear();

    boolean ofBoolean();

    U clone();

    static class NIL implements Update<NIL> {
        public static final NIL INSTANCE = new NIL();
        public static final Supplier<NIL> SUPPLIER = () -> INSTANCE;

        private NIL() {
        }

        @Override
        public void update(NIL nilUpdate) {
        }

        @Override
        public void clear() {
        }

        @Override
        public boolean ofBoolean() {
            return false;
        }

        @Override
        public NIL clone() {
            return this;
        }
    }
}
