package template.datastructure;

import template.rand.RandomWrapper;

import java.util.Comparator;
import java.util.function.Consumer;

public class TreapT<T> {

    public static final TreapT NIL = new TreapT();

    static {
        NIL.left = NIL.right = NIL;
        NIL.size = 0;
    }

    public TreapT<T> left = NIL;
    public TreapT<T> right = NIL;
    public int size = 1;
    public T key;

    public TreapT(T key) {
        this.key = key;
    }

    public TreapT() {
    }

    @Override
    public TreapT clone() {
        try {
            return (TreapT) super.clone();
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
    }

    /**
     * split by rank and the node whose rank is argument will stored at result[0]
     */
    public static<T> TreapT<T>[] splitByRank(TreapT<T> root, int rank) {
        if (root == NIL) {
            return new TreapT[]{NIL, NIL};
        }
        root.pushDown();
        TreapT<T>[] result;
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

    public static<T> TreapT<T> merge(TreapT<T> a, TreapT<T> b) {
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

    public static<T> void toString(TreapT<T> root, StringBuilder builder) {
        if (root == NIL) {
            return;
        }
        root.pushDown();
        toString(root.left, builder);
        builder.append(root.key).append(',');
        toString(root.right, builder);
    }

    public static<T> TreapT<T> clone(TreapT<T> root) {
        if (root == NIL) {
            return NIL;
        }
        TreapT<T> clone = root.clone();
        clone.left = clone(root.left);
        clone.right = clone(root.right);
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append(key).append(":");
        toString(clone(this), builder);
        return builder.toString();
    }

    /**
     * nodes with key <= arguments will stored at result[0]
     */
    public static<T> TreapT<T>[] splitByKey(TreapT<T> root, T key, Comparator<T> comp, boolean preferLeft) {
        if (root == NIL) {
            return new TreapT[]{NIL, NIL};
        }
        root.pushDown();
        TreapT<T>[] result;
        int compRes = comp.compare(root.key, key);
        if ((compRes > 0) || (!preferLeft && compRes == 0)) {
            result = splitByKey(root.left, key, comp, preferLeft);
            root.left = result[1];
            result[1] = root;
        } else {
            result = splitByKey(root.right, key, comp, preferLeft);
            root.right = result[0];
            result[0] = root;
        }
        root.pushUp();
        return result;
    }

    public static<T> T getKeyByRank(TreapT<T> treap, int k) {
        while (treap.size > 1) {
            treap.pushDown();
            if (treap.left.size >= k) {
                treap = treap.left;
            } else {
                k -= treap.left.size;
                if (k == 1) {
                    break;
                }
                k--;
                treap = treap.right;
            }
        }
        return treap.key;
    }

    public static<T> int getRankByKey(TreapT<T> treap, T k, Comparator<T> comp) {
        int rank = 0;
        while (treap != NIL) {
            int compRes = comp.compare(treap.key, k);
            if (compRes == 0) {
                rank += treap.left.size + 1;
                return rank;
            } else if (compRes < 0) {
                rank += treap.left.size + 1;
                treap = treap.right;
            } else {
                treap = treap.left;
            }
        }
        return rank;
    }

    public static<T> void collect(TreapT<T> treap, Consumer<TreapT<T>> consumer){
        if(treap == NIL){
            return;
        }
        collect(treap.left, consumer);
        consumer.accept(treap);
        collect(treap.right, consumer);
    }
}
