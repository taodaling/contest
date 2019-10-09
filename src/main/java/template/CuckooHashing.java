package template;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class CuckooHashing<K, V> extends AbstractMap<K, V> {
    private Object[] key1;
    private Object[] val1;
    private Object[] key2;
    private Object[] val2;
    private int[] visit1;
    private int[] visit2;
    private int n;
    private int m;
    private int mask;
    private int threshold;
    private HashFunction h1;
    private HashFunction h2;
    private Deque<Object> keyQueue;
    private Deque<Object> valQueue;
    private Random random = new Random();
    private double eplison;
    private int modVersion;
    private Set<K> keySet = new AbstractSet<K>() {
        @Override
        public Iterator<K> iterator() {
            return new Iterator<K>() {
                int side;
                int iter;
                int version = modVersion;
                Object next;

                @Override
                public boolean hasNext() {
                    if (modVersion != version) {
                        throw new ConcurrentModificationException();
                    }
                    if (next != null) {
                        return true;
                    }
                    if (side == 0) {
                        for (; iter < m; iter++) {
                            if (key1[iter] != null) {
                                next = key1[iter];
                                return true;
                            }
                        }
                        side++;
                        iter = 0;
                    }
                    if (side == 1) {
                        for (; iter < m; iter++) {
                            if (key2[iter] != null) {
                                next = key2[iter];
                                return true;
                            }
                        }
                    }
                    return false;
                }

                @Override
                public K next() {
                    if (modVersion != version) {
                        throw new ConcurrentModificationException();
                    }
                    K ret = (K) next;
                    next = null;
                    return ret;
                }
            };
        }

        @Override
        public int size() {
            return CuckooHashing.this.size();
        }
    };
    private Set<V> valueSet = new AbstractSet<V>() {
        @Override
        public Iterator<V> iterator() {
            return new Iterator<V>() {
                int side;
                int iter;
                int version = modVersion;
                Object next;

                @Override
                public boolean hasNext() {
                    if (modVersion != version) {
                        throw new ConcurrentModificationException();
                    }
                    if (next != null) {
                        return true;
                    }
                    if (side == 0) {
                        for (; iter < m; iter++) {
                            if (key1[iter] != null) {
                                next = val1[iter];
                                return true;
                            }
                        }
                        side++;
                        iter = 0;
                    }
                    if (side == 1) {
                        for (; iter < m; iter++) {
                            if (key2[iter] != null) {
                                next = val2[iter];
                                return true;
                            }
                        }
                    }
                    return false;
                }

                @Override
                public V next() {
                    if (modVersion != version) {
                        throw new ConcurrentModificationException();
                    }
                    V ret = (V) next;
                    next = null;
                    return ret;
                }
            };
        }

        @Override
        public int size() {
            return CuckooHashing.this.size();
        }
    };
    private Set<Entry<K, V>> entrySet = new AbstractSet<Entry<K, V>>() {
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new Iterator<Entry<K, V>>() {
                private Iterator<K> iterator = keySet.iterator();

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public Entry<K, V> next() {
                    K key = iterator.next();
                    return new SimpleImmutableEntry<>(key, get(key));
                }
            };
        }

        @Override
        public int size() {
            return CuckooHashing.this.size();
        }
    };

    public CuckooHashing(int exp) {
        this(exp, 1);
    }

    public CuckooHashing(int exp, double eplison) {
        this.eplison = eplison;

        int proper = 1;
        int atLeast = (int) Math.ceil(exp * (1 + eplison));
        while (proper < atLeast) {
            proper <<= 1;
        }
        realloc(proper);
        keyQueue = new ArrayDeque<>(m);
        valQueue = new ArrayDeque<>(m);
        n = 0;
        repickAlgo();
    }

    private int whichBucket(int h) {
        return h & mask;
    }

    private void realloc(int m) {
        this.m = m;
        threshold = (int) Math.floor(m / (1 + eplison));
        mask = m - 1;
        key1 = new Object[m];
        val1 = new Object[m];
        key2 = new Object[m];
        val2 = new Object[m];
        visit1 = new int[m];
        visit2 = new int[m];
    }

    private void repickAlgo() {
        int a1 = random.nextInt();
        int a2 = random.nextInt();
        int b1 = random.nextInt();
        int b2 = random.nextInt();
        h1 = (h) -> a1 * h + b1;
        h2 = (h) -> a2 * h + b2;
    }

    private void collect() {
        for (int i = 0; i < m; i++) {
            if (key1[i] != null) {
                keyQueue.addLast(key1[i]);
                valQueue.addLast(val1[i]);
                key1[i] = null;
                val1[i] = null;
            }
            if (key2[i] != null) {
                keyQueue.addLast(key2[i]);
                valQueue.addLast(val2[i]);
                key2[i] = null;
                val2[i] = null;
            }
            visit1[i] = visit2[i] = 0;
        }

        repickAlgo();
    }

    private void process() {
        while (!keyQueue.isEmpty()) {
            Object k = keyQueue.removeFirst();
            Object v = valQueue.removeFirst();
            if (!insert(k, v)) {
                collect();
            }
        }
    }

    int time = 0;

    private boolean insert(Object key, Object val) {
        time++;
        int circle = 0;
        while (circle < 2) {
            int whichBucket = whichBucket(h1.hash(key.hashCode()));
            if (visit1[whichBucket] == time) {
                circle++;
            }
            visit1[whichBucket] = time;

            if (key1[whichBucket] == null) {
                key1[whichBucket] = key;
                val1[whichBucket] = val;
                return true;
            }

            Object tmp = key1[whichBucket];
            key1[whichBucket] = key;
            key = tmp;
            tmp = val1[whichBucket];
            val1[whichBucket] = val;
            val = tmp;

            whichBucket = whichBucket(h2.hash(key.hashCode()));
            if (visit2[whichBucket] == time) {
                circle++;
            }
            visit2[whichBucket] = time;

            if (key2[whichBucket] == null) {
                key2[whichBucket] = key;
                val2[whichBucket] = val;
                return true;
            }

            tmp = key2[whichBucket];
            key2[whichBucket] = key;
            key = tmp;
            tmp = val2[whichBucket];
            val2[whichBucket] = val;
            val = tmp;
        }

        keyQueue.addLast(key);
        valQueue.addLast(val);
        return false;
    }

    private static interface HashFunction {
        int hash(int h);
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public Collection<V> values() {
        return valueSet;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return entrySet;
    }

    @Override
    public int size() {
        return n;
    }

    @Override
    public boolean containsKey(Object key) {
        int b1 = whichBucket(h1.hash(key.hashCode()));
        int b2 = whichBucket(h2.hash(key.hashCode()));
        return key1[b1] != null && key.equals(key1[b1]) || key2[b2] != null && key.equals(key2[b2]);
    }

    @Override
    public V get(Object key) {
        int b1 = whichBucket(h1.hash(key.hashCode()));
        int b2 = whichBucket(h2.hash(key.hashCode()));
        if (key1[b1] != null && key.equals(key1[b1])) {
            return (V) val1[b1];
        }
        if (key2[b2] != null && key.equals(key2[b2])) {
            return (V) val2[b2];
        }
        return null;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public V put(K key, V value) {
        V val = remove(key);
        n++;
        keyQueue.addLast(key);
        valQueue.addLast(value);
        if (n > threshold) {
            collect();
            realloc(m + m);
        }
        process();
        return val;
    }

    @Override
    public V remove(Object key) {
        modVersion++;
        int b1 = whichBucket(h1.hash(key.hashCode()));
        int b2 = whichBucket(h2.hash(key.hashCode()));
        if (key1[b1] != null && key.equals(key1[b1])) {
            V val = (V) val1[b1];
            key1[b1] = null;
            val1[b1] = null;
            n--;
            return val;
        }
        if (key2[b2] != null && key.equals(key2[b2])) {
            V val = (V) val2[b2];
            key2[b2] = null;
            val2[b2] = null;
            n--;
            return val;
        }
        return null;
    }

    @Override
    public void clear() {
        modVersion++;
        for (int i = 0; i < m; i++) {
            key1[i] = null;
            key2[i] = null;
            val1[i] = null;
            val2[i] = null;
        }
        n = 0;
    }
}
