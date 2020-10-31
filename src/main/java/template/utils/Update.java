package template.utils;

public interface Update<U extends Update<U>> extends Cloneable {
    void update(U u);

    void clear();

    boolean ofBoolean();

    U clone();

    static class NilUpdate implements Update<NilUpdate> {
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
            return new NilUpdate();
        }
    }
}
