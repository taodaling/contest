package template.datastructure;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MultiSet<T> {
    private TreeMap<T, Integer> map;
    private int size;

    public MultiSet(Comparator<T> comp) {
        this.map = new TreeMap<>(comp);
    }

    public MultiSet() {
        this.map = new TreeMap<>();
    }

    public int size() {
        return size;
    }

    public int distinct() {
        return map.size();
    }

    public T first() {
        return map.firstKey();
    }

    public T last() {
        return map.lastKey();
    }

    public T pollFirst() {
        Map.Entry<T, Integer> first = map.firstEntry();
        update(first.getKey(), first.getValue() - 1);
        return first.getKey();
    }

    public T pollLast() {
        Map.Entry<T, Integer> last = map.lastEntry();
        update(last.getKey(), last.getValue() - 1);
        return last.getKey();
    }

    public void add(T key) {
        update(key, map.getOrDefault(key, 0) + 1);
    }

    public void remove(T key) {
        update(key, map.getOrDefault(key, 0) - 1);
    }

    public T ceil(T x) {
        return map.ceilingKey(x);
    }

    public T floor(T x) {
        return map.floorKey(x);
    }

    private void update(T key, int cnt) {
        if (cnt == 0) {
            map.remove(key);
        } else if (cnt > 0) {
            map.put(key, cnt);
        }
    }
}
