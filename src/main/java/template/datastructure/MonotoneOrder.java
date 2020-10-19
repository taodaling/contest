package template.datastructure;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;


public class MonotoneOrder<K, V> {
    private TreeMap<K, V> map;
    private Comparator<V> vComp;
    private Comparator<K> kComp;
    private V min;

    public MonotoneOrder(Comparator<K> kComp, Comparator<V> vComp, V min) {
        map = new TreeMap<>(kComp);
        this.vComp = vComp;
        this.kComp = kComp;
        this.min = min;
    }

    /**
     * For any x >= k, f(x) >= v
     *
     * @param k
     * @param v
     */
    public void add(K k, V v) {
        Map.Entry<K, V> floor = map.floorEntry(k);
        if (floor != null) {
            if (vComp.compare(floor.getValue(), v) >= 0) {
                return;
            }
            if (kComp.compare(floor.getKey(), k) == 0) {
                map.remove(floor.getKey());
            }
        }
        while (true) {
            Map.Entry<K, V> ceil = map.ceilingEntry(k);
            if (ceil == null || vComp.compare(ceil.getValue(), v) > 0) {
                break;
            }
            map.remove(ceil.getKey());
        }
        map.put(k, v);
    }

    public Map.Entry<K, V> floor(K k) {
        return map.floorEntry(k);
    }

    public Map.Entry<K, V> ceil(K k) {
        return map.ceilingEntry(k);
    }

    public V atLeast(K k) {
        Map.Entry<K, V> f = floor(k);
        if (f == null) {
            return min;
        }
        return f.getValue();
    }
}
