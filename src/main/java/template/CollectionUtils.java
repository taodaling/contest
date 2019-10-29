package template;

import java.util.Collection;
import java.util.Set;

public class CollectionUtils {
    public static <E, T extends Collection<E>> T mergeHeuristically(T a, T b) {
        if (a.size() >= b.size()) {
            a.addAll(b);
            return a;
        } else {
            b.addAll(a);
            return b;
        }
    }

    public static <T> boolean IsIntersectionEmpty(Set<T> a, Set<T> b) {
        if (a.size() > b.size()) {
            Set<T> tmp = a;
            a = b;
            b = tmp;
        }
        for (T val : a) {
            if (b.contains(val)) {
                return false;
            }
        }
        return true;
    }
}