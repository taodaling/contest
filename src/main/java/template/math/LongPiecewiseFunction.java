package template.math;

import java.util.Iterator;
import java.util.function.Consumer;

public class LongPiecewiseFunction {
    public long l;
    public long r;
    public long a;
    public long b;

    public static interface Merger {
        LongPiecewiseFunction merge(long a1, long b1, long a2, long b2);
    }

    public static void merge(Iterator<LongPiecewiseFunction> a, Iterator<LongPiecewiseFunction> b,
                             Merger merger, Consumer<LongPiecewiseFunction> consumer) {
        LongPiecewiseFunction first = null;
        LongPiecewiseFunction second = null;
        while (true) {
            if (first == null) {
                if (!a.hasNext()) {
                    first = a.next();
                } else {
                    break;
                }
            }
            if (second == null) {
                if (!b.hasNext()) {
                    second = b.next();
                } else {
                    break;
                }
            }
            long left = Math.max(first.l, second.l);
            long right = Math.min(first.r, second.r);


        }
    }
}
