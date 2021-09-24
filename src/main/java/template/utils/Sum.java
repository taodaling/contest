package template.utils;

public interface Sum<S> {
    void add(S right);

    /**
     * copy s.data
     *
     * @param right
     */
    void copy(S right);

    S clone();
}
