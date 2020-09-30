package template.algo;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.IntFunction;

/**
 * longest strictly increment subsequence
 */
public class LIS {
    public static <T> int[] lis(IntFunction<T> func, int n, Comparator<T> comp) {
        if (n == 0) {
            return new int[0];
        }
        TreeMap<T, Integer> map = new TreeMap<>(comp);
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) {
            T e = func.apply(i);
            Map.Entry<T, Integer> ceil = map.ceilingEntry(e);
            if (ceil != null) {
                map.remove(ceil.getKey());
            }
            Map.Entry<T, Integer> floor = map.floorEntry(e);
            prev[i] = floor == null ? -1 : floor.getValue();
            map.put(e, i);
        }
        int len = map.size();
        int[] ans = new int[len];
        int wpos = len - 1;
        int start = map.lastEntry().getValue();
        while (start != -1) {
            ans[wpos--] = start;
            start = prev[start];
        }
        return ans;
    }

    public static <T> int lisLength(IntFunction<T> func, int n, Comparator<T> comp) {
        TreeSet<T> set = new TreeSet<>(comp);
        for (int i = 0; i < n; i++) {
            T e = func.apply(i);
            T ceil = set.ceiling(e);
            if (ceil != null) {
                set.remove(ceil);
            }
            set.add(e);
        }
        return set.size();
    }
}
