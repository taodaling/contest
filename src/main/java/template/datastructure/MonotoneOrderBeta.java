package template.datastructure;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MonotoneOrderBeta<K, V> implements Iterable<Map.Entry<K, V>> {
    TreeMap<K, V> map;
    Comparator<V> vComp;
    boolean preferLarger;

    /**
     * @param keyComp
     * @param valueComp
     * @param inc          whether the curve strictly increasing
     * @param preferLarger if you prefer larger, then (1, 1) and (1, 2), the second will be retain and first will be discard.
     */
    public MonotoneOrderBeta(Comparator<K> keyComp, Comparator<V> valueComp, boolean inc, boolean preferLarger) {
        map = new TreeMap<>(keyComp);
        if (!inc) {
            vComp = valueComp.reversed();
            preferLarger = !preferLarger;
        } else {
            vComp = valueComp;
        }
        this.preferLarger = preferLarger;
    }

    public void add(K k, V v) {
        if (preferLarger) {
            Map.Entry<K, V> floor = map.floorEntry(k);
            if (floor != null && vComp.compare(floor.getValue(), v) >= 0) {
                return;
            }
            while (true) {
                Map.Entry<K, V> ceil = map.ceilingEntry(k);
                if (ceil == null || vComp.compare(v, ceil.getValue()) < 0) {
                    break;
                }
                map.remove(ceil.getKey());
            }
        } else {
            Map.Entry<K, V> ceil = map.ceilingEntry(k);
            if (ceil != null && vComp.compare(v, ceil.getValue()) >= 0) {
                return;
            }
            while (true) {
                Map.Entry<K, V> floor = map.floorEntry(k);
                if (floor == null || vComp.compare(floor.getValue(), v) < 0) {
                    break;
                }
                map.remove(floor.getKey());
            }
        }
        map.put(k, v);
    }

    public V floor(K k) {
        Map.Entry<K, V> entry = map.floorEntry(k);
        return entry == null ? null : entry.getValue();
    }


    public V ceil(K k) {
        Map.Entry<K, V> entry = map.ceilingEntry(k);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return map.entrySet().iterator();
    }

    public Map.Entry<K, V> first() {
        return map.firstEntry();
    }

    public Map.Entry<K, V> last() {
        return map.lastEntry();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
