package template.datastructure;

import template.rand.RandomWrapper;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.IntFunction;

/**
 * memory save
 */
public class CountTreap {
    Treap root = Treap.NIL;

    public long query(long l, long r) {
        Treap[] split0 = Treap.splitByKey(root, l - 1);
        Treap[] split1 = Treap.splitByKey(split0[1], r);
        long ans = split1[0].sum;
        split0[1] = Treap.merge(split1[0], split1[1]);
        root = Treap.merge(split0[0], split0[1]);
        return ans;
    }

    public void update(long pos, long weight) {
        Treap[] split0 = Treap.splitByKey(root, pos - 1);
        Treap[] split1 = Treap.splitByKey(split0[1], pos);
        if (split1[0] == Treap.NIL) {
            split1[0] = new Treap();
            split1[0].key = pos;
        }
        split1[0].weight += weight;
        split1[0].pushUp();
        split0[1] = Treap.merge(split1[0], split1[1]);
        root = Treap.merge(split0[0], split0[1]);
    }

    static class Treap implements Cloneable {

        public static final Treap NIL = new Treap();

        static {
            NIL.left = NIL.right = NIL;
            NIL.size = 0;
        }

        public Treap left = NIL;
        public Treap right = NIL;
        public int size = 1;
        public long key;
        public long sum;
        public long weight;


        public static Treap build(IntFunction<Treap> func, int l, int r) {
            if (l > r) {
                return Treap.NIL;
            }
            int mid = (l + r) / 2;
            Treap root = func.apply(mid);
            root.left = build(func, l, mid - 1);
            root.right = build(func, mid + 1, r);
            root.pushUp();
            return root;
        }

        @Override
        public Treap clone() {
            try {
                return (Treap) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        public void pushDown() {
            if (this == NIL) {
                return;
            }
        }

        public void pushUp() {
            if (this == NIL) {
                return;
            }
            size = left.size + right.size + 1;
            sum = left.sum + right.sum + weight;
        }

        public static Treap merge(Treap a, Treap b) {
            if (a == NIL) {
                return b;
            }
            if (b == NIL) {
                return a;
            }
            if (RandomWrapper.INSTANCE.nextInt(a.size + b.size) < a.size) {
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

        public static void toString(Treap root, Map<Long, Long> map) {
            if (root == NIL) {
                return;
            }
            root.pushDown();
            toString(root.left, map);
            map.put(root.key, root.weight);
            toString(root.right, map);
        }

        public static Treap clone(Treap root) {
            if (root == NIL) {
                return NIL;
            }
            Treap clone = root.clone();
            clone.left = clone(root.left);
            clone.right = clone(root.right);
            return clone;
        }

        @Override
        public String toString() {
            TreeMap<Long, Long> map = new TreeMap<>();
            toString(clone(this), map);
            return map.toString();
        }

        public static Treap[] splitByKey(Treap root, long key) {
            if (root == NIL) {
                return new Treap[]{NIL, NIL};
            }
            root.pushDown();
            Treap[] result;
            if (root.key > key) {
                result = splitByKey(root.left, key);
                root.left = result[1];
                result[1] = root;
            } else {
                result = splitByKey(root.right, key);
                root.right = result[0];
                result[0] = root;
            }
            root.pushUp();
            return result;
        }

    }


    @Override
    public String toString() {
        return root.toString();
    }
}
