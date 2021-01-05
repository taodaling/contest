package template.utils;

import java.util.Objects;

public class Pair<A, B> implements Cloneable {
    public A a;
    public B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public String toString() {
        return "a=" + a + ",b=" + b;
    }

    @Override
    public int hashCode() {
        return a.hashCode() * 31 + b.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        Pair<A, B> casted = (Pair<A, B>) obj;
        return Objects.equals(casted.a, a) && Objects.equals(casted.b, b);
    }

    @Override
    public Pair<A, B> clone() {
        try {
            return (Pair<A, B>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
