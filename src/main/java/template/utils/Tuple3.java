package template.utils;

import java.util.Objects;

public class Tuple3<A, B, C> extends Pair<A, B> {
    public C c;

    public Tuple3(A a, B b, C c) {
        super(a, b);
        this.c = c;
    }

    @Override
    public String toString() {
        return super.toString() + ",c=" + c;
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + c.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        Tuple3<A, B, C> casted = (Tuple3<A, B, C>) obj;
        return super.equals(obj) && Objects.equals(casted.c, c);
    }

    @Override
    public Tuple3<A, B, C> clone() {
        return (Tuple3<A, B, C>) super.clone();
    }
}
