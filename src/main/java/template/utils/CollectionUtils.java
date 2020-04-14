package template.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

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

    public static <K, V, T extends Map<K, V>> Map<K, V> mergeMapHeuristically(T a, T b, V def, BiFunction<V, V, V> bf) {
        if (a.size() < b.size()) {
            T tmp = a;
            a = b;
            b = tmp;
        }
        for (Map.Entry<K, V> kv : b.entrySet()) {
            a.put(kv.getKey(), bf.apply(a.getOrDefault(kv.getKey(), def), kv.getValue()));
        }
        return a;
    }

    public static <K, T extends Map<K, Integer>> Map<K, Integer> mergeCountMapHeuristically(T a, T b) {
        return mergeMapHeuristically(a, b, 0, (x, y) -> x + y);
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

    public static <T> Set<T> intersect(Set<T> a, Set<T> b) {
        if (a.size() > b.size()) {
            Set<T> tmp = a;
            a = b;
            b = tmp;
        }
        Set<T> ans = new HashSet<>(a.size());
        for (T val : a) {
            if (b.contains(val)) {
                ans.add(val);
            }
        }
        return ans;
    }
}
