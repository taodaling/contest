package template;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BiHashMap<K, V> extends AbstractMap<K, V> {
    private Map<K, V> map = new HashMap<>();
    private BiHashMap<V, K> rev;

    public BiHashMap(BiHashMap<V, K> rev) {
        this.rev = rev;
    }

    public BiHashMap() {
        rev = new BiHashMap<>(this);
    }

    public BiHashMap<V, K> reverse() {
        return rev;
    }

    @Override
    public V put(K key, V value) {
        remove(key);
        rev.remove(value);
        rev.put0(value, key);
        return put0(key, value);
    }

    private V put0(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        V val = remove0(key);
        rev.remove0(val);
        return val;
    }

    private V remove0(Object key) {
        return map.remove(key);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}