package template.utils;

public interface Sum<S, U> extends Cloneable {
    void add(S s);

    void update(U u);

    /**
     * copy s.data
     * @param s
     */
    void copy(S s);

    S clone();

    static class NilSum<U> implements Sum<NilSum<U>, U> {
        private static NilSum SINGLETON = new NilSum();

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
