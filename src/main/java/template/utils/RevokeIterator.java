package template.utils;


import java.util.Iterator;

public interface RevokeIterator<E> extends Iterator<E> {
    /**
     * Revoke all the effect taken from last next operation
     */
    void revoke();
}
