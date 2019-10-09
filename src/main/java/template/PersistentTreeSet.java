package template;

import java.util.Comparator;
import java.util.Random;

public class PersistentTreeSet<K> implements Cloneable {
    private Treap<K> treap = Treap.NIL;
    private Comparator<K> keyComparator;

    public PersistentTreeSet(Comparator<K> keyComparator) {
        this.keyComparator = keyComparator;
    }

    public int size() {
        return treap.size;
    }

    public K lower(K k) {
        Treap<K> op = treap.clone();
        Treap<K>[] pair = Treap.splitByKeyPreferRight(op, k, keyComparator);
        return Treap.findMax(pair[0]).key;
    }

    public K floor(K k) {
        Treap<K> op = treap.clone();
        Treap<K>[] pair = Treap.splitByKeyPreferLeft(op, k, keyComparator);
        return Treap.findMax(pair[0]).key;
    }

    public K ceiling(K k) {
        Treap<K> op = treap.clone();
        Treap<K>[] pair = Treap.splitByKeyPreferRight(op, k, keyComparator);
        return Treap.findMin(pair[1]).key;
    }

    public K higher(K k) {
        Treap<K> op = treap.clone();
        Treap<K>[] pair = Treap.splitByKeyPreferLeft(op, k, keyComparator);
        return Treap.findMin(pair[1]).key;
    }

    public K pollFirst() {
        Treap<K> op = treap.clone();
        Treap<K>[] pair = Treap.splitByRank(op, 1);
        treap = pair[1];
        return pair[0].key;
    }

    public K pollLast() {
        Treap<K> op = treap.clone();
        Treap<K>[] pair = Treap.splitByRank(op, op.size - 1);
        treap = pair[0];
        return pair[1].key;
    }


    public boolean contain(K key) {
        K last = floor(key);
        return last != null && keyComparator.compare(last, key) == 0;
    }

    public Comparator<? super K> comparator() {
        return keyComparator;
    }

    public K first() {
        Treap<K> op = treap.clone();
        Treap<K>[] pair = Treap.splitByRank(op, 1);
        return pair[0].key;
    }

    public K last() {
        Treap<K> op = treap.clone();
        Treap<K>[] pair = Treap.splitByRank(op, op.size - 1);
        return pair[1].key;
    }

    public void add(K key) {
        Treap<K> op = treap.clone();
        Treap<K>[] pair = Treap.splitByKeyPreferLeft(op, key, keyComparator);

        Treap<K> t = new Treap<>();
        t.key = key;
        treap = Treap.merge(pair[0], t);
        treap = Treap.merge(treap, pair[1]);
    }

    public void delete(K key) {
        Treap<K> op = treap.clone();
        Treap<K>[] pair = Treap.splitByKeyPreferLeft(op, key, keyComparator);
        if (pair[0] == Treap.NIL) {
            return;
        }
        pair[0] = Treap.splitByKeyPreferRight(pair[0], key, keyComparator)[0];
        treap = Treap.merge(pair[0], pair[1]);
    }

    private static class Treap<T> implements Cloneable {
        private static Random random = new Random();

        private static Treap NIL = new Treap();

        static {
            NIL.left = NIL.right = NIL;
            NIL.size = 0;
        }

        Treap<T> left = NIL;
        Treap<T> right = NIL;
        int size = 1;
        T key;
        int w = random.nextInt();

        @Override
        public Treap<T> clone() {
            if (this == NIL) {
                return this;
            }
            try {
                return (Treap<T>) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        public void pushDown() {
            left = left.clone();
            right = right.clone();
        }

        public void pushUp() {
            size = left.size + right.size + 1;
        }

        public static <T> Treap<T>[] splitByRank(Treap<T> root, int rank) {
            if (root == NIL) {
                return new Treap[]{NIL, NIL};
            }
            root.pushDown();
            Treap<T>[] result;
            if (root.left.size >= rank) {
                result = splitByRank(root.left, rank);
                root.left = result[1];
                result[1] = root;
            } else {
                result = splitByRank(root.right, rank - (root.size - root.right.size));
                root.right = result[0];
                result[0] = root;
            }
            root.pushUp();
            return result;
        }

        public static <T> Treap<T> merge(Treap<T> a, Treap<T> b) {
            if (a == NIL) {
                return b;
            }
            if (b == NIL) {
                return a;
            }
            if (a.w <= b.w) {
                a.pushDown();
                a.right = merge(a.right, b);
                a.pushUp();
                return a;
            } else {
                b.pushDown();
                b.left = merge(a, b.left);
                b.pushUp();
                return b;
            }
        }

        public static <T> void toString(Treap<T> root, StringBuilder builder) {
            if (root == NIL) {
                return;
            }
            root.pushDown();
            toString(root.left, builder);
            builder.append(root.key).append(',');
            toString(root.right, builder);
        }

        public static <T> Treap<T> clone(Treap<T> root) {
            if (root == NIL) {
                return NIL;
            }
            Treap<T> clone = root.clone();
            clone.left = clone(root.left);
            clone.right = clone(root.right);
            return clone;
        }

        public static <T> Treap<T> findMax(Treap<T> root) {
            return root.right == NIL ? root : findMax(root.right);
        }

        public static <T> Treap<T> findMin(Treap<T> root) {
            return root.left == NIL ? root : findMin(root.left);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder().append(key).append(":");
            toString(clone(this), builder);
            return builder.toString();
        }

        public static <T> Treap<T>[] splitByKeyPreferLeft(Treap<T> root, T key, Comparator<T> keyComparator) {
            if (root == NIL) {
                return new Treap[]{NIL, NIL};
            }
            root.pushDown();
            Treap<T>[] result;
            if (keyComparator.compare(root.key, key) > 0) {
                result = splitByKeyPreferLeft(root.left, key, keyComparator);
                root.left = result[1];
                result[1] = root;
            } else {
                result = splitByKeyPreferLeft(root.right, key, keyComparator);
                root.right = result[0];
                result[0] = root;
            }
            root.pushUp();
            return result;
        }

        public static <T> Treap<T>[] splitByKeyPreferRight(Treap<T> root, T key, Comparator<T> keyComparator) {
            if (root == NIL) {
                return new Treap[]{NIL, NIL};
            }
            root.pushDown();
            Treap<T>[] result;
            if (keyComparator.compare(root.key, key) >= 0) {
                result = splitByKeyPreferRight(root.left, key, keyComparator);
                root.left = result[1];
                result[1] = root;
            } else {
                result = splitByKeyPreferRight(root.right, key, keyComparator);
                root.right = result[0];
                result[0] = root;
            }
            root.pushUp();
            return result;
        }
    }

    @Override
    public PersistentTreeSet<K> clone() {
        try {
            return (PersistentTreeSet<K>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
