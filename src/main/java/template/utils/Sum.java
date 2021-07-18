package template.utils;

public interface Sum<S> {
    void add(S s);

    /**
     * copy s.data
     *
     * @param s
     */
    void copy(S s);

    S clone();
}
