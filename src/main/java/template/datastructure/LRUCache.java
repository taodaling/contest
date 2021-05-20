package template.datastructure;

import template.rand.Hasher;

import java.util.*;

public class LRUCache<K, V> extends AbstractMap<K, V> {
    public LRUCache(int cap) {
        if (cap <= 0) {
            throw new IllegalArgumentException();
        }
        int proper = 1;
        while (proper < cap) {
            proper <<= 1;
        }
        slots = new Node[proper];
        mask = proper - 1;
        this.cap = cap;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    public void access(Node<K, V> node) {
        node.detach();
        node.attach(accessList.prev);
    }

    Node<K, V> find(int slot, Object key, int h) {
        for (Node<K, V> head = slots[slot]; head != null; head = head.back) {
            if (head.h == h && Objects.equals(head.key, key)) {
                return head;
            }
        }
        return null;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        //access
        int h = hasher.hash(key);
        int slotIndex = h & mask;
        Node<K, V> res = find(slotIndex, key, h);
        if (res == null) {
            return defaultValue;
        } else {
            access(res);
            return res.val;
        }
    }

    private Node<K, V> alloc(K k, V v, int h) {
        Node<K, V> res;
        if (size == cap) {
            //delete first node
            res = accessList.next;
            del(res, false);
        } else if (pool.next == pool) {
            res = new Node<>();
        } else {
            res = pool.next;
            res.detach();
        }
        res.key = k;
        res.val = v;
        res.h = h;
        res.front = res.back = null;
        size++;
        return res;
    }

    @Override
    public V get(Object key) {
        return getOrDefault(key, null);
    }

    @Override
    public V put(K key, V value) {
        //access
        int h = hasher.hash(key);
        int slotIndex = h & mask;
        Node<K, V> res = find(slotIndex, key, h);
        V old;
        if (res == null) {
            old = null;
            res = alloc(key, value, h);
            res.back = slots[slotIndex];
            if (res.back != null) {
                res.back.front = res;
            }
            slots[slotIndex] = res;
        } else {
            old = res.val;
            res.val = value;
        }

        access(res);
        return old;
    }

    private void del(Node<K, V> res, boolean pool) {
        res.detach();
        if (res.front != null) {
            res.front.back = res.back;
        }
        if (res.back != null) {
            res.back.front = res.front;
        }
        if (res.front == null) {
            slots[res.h & mask] = res.back;
        }
        size--;
        if (pool) {
            res.attach(this.pool);
        }
    }

    @Override
    public V remove(Object key) {
        //access
        int h = hasher.hash(key);
        int slotIndex = h & mask;
        Node<K, V> res = find(slotIndex, key, h);
        if (res == null) {
            return null;
        } else {
            V ans = res.val;
            del(res, true);
            return ans;
        }
    }

    @Override
    public void clear() {
        while (accessList.next != accessList) {
            Node<K, V> next = accessList.next;
            slots[next.h & mask] = null;
            next.detach();
            next.attach(pool);
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return set;
    }

    int cap;
    int mask;
    Node<K, V>[] slots;
    Set<Entry<K, V>> set = new AbstractSet<Entry<K, V>>() {
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new Iterator<Entry<K, V>>() {
                Node<K, V> next = accessList.next;

                @Override
                public boolean hasNext() {
                    return next != accessList;
                }

                @Override
                public Entry<K, V> next() {
                    Node<K, V> ans = next;
                    next = next.next;
                    return ans;
                }
            };
        }

        @Override
        public int size() {
            return size;
        }
    };

    Node<K, V> accessList = new Node<>();
    Node<K, V> pool = new Node<>();
    Hasher hasher = new Hasher();

    int size = 0;

    static class Node<K, V> extends CircularLinkedNode<Node<K, V>> implements Entry<K, V> {
        Node<K, V> front = this;
        Node<K, V> back = this;
        K key;
        V val;
        int h;

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return val;
        }

        @Override
        public V setValue(V value) {
            V old = this.val;
            this.val = value;
            return old;
        }
    }
}
