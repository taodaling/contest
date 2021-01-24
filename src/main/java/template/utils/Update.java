package template.utils;

import java.util.function.Supplier;

public interface Update<U extends Update<U>> extends Cloneable {
    void update(U u);

    void clear();

    boolean ofBoolean();

    U clone();

    static class NilUpdate implements Update<NilUpdate> {
        public static final NilUpdate INSTANCE = new NilUpdate();
        public static final Supplier<NilUpdate> SUPPLIER = () -> INSTANCE;

        private NilUpdate() {
        }

        @Override
        public void update(NilUpdate nilUpdate) {
        }

        @Override
        public void clear() {
        }

        @Override
        public boolean ofBoolean() {
            return false;
        }

        @Override
        public NilUpdate clone() {
            return this;
        }
    }
}
