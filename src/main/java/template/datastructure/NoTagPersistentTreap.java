package template.datastructure;

import java.util.Random;

public class NoTagPersistentTreap implements Cloneable {
    private static Random random = new Random();
    static NoTagPersistentTreap NIL = new NoTagPersistentTreap();

    static {
        NIL.left = NIL.right = NIL;
        NIL.size = 0;
    }

    NoTagPersistentTreap left = NIL;
    NoTagPersistentTreap right = NIL;
    int size = 1;
    int key;

    @Override
    public NoTagPersistentTreap clone() {
        if (this == NIL) {
            return this;
        }
        try {
            return (NoTagPersistentTreap) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
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
    public static NoTagPersistentTreap[] splitByRank(NoTagPersistentTreap root, int rank) {
        if (root == NIL) {
            return new NoTagPersistentTreap[]{NIL, NIL};
        }
        root = root.clone();
        NoTagPersistentTreap[] result;
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

    public static NoTagPersistentTreap merge(NoTagPersistentTreap a, NoTagPersistentTreap b) {
        if (a == NIL) {
            return b;
        }
        if (b == NIL) {
            return a;
        }
        if (random.nextInt(a.size + b.size) < a.size) {
            a = a.clone();
            a.right = merge(a.right, b);
            a.pushUp();
            return a;
        } else {
            b = b.clone();
            b.left = merge(a, b.left);
            b.pushUp();
            return b;
        }
    }

    public static void toString(NoTagPersistentTreap root, StringBuilder builder) {
        if (root == NIL) {
            return;
        }
        root = root.clone();
        toString(root.left, builder);
        builder.append(root.key).append(',');
        toString(root.right, builder);
    }

    public static NoTagPersistentTreap clone(NoTagPersistentTreap root) {
        if (root == NIL) {
            return NIL;
        }
        NoTagPersistentTreap clone = root.clone();
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
    public static NoTagPersistentTreap[] splitByKey(NoTagPersistentTreap root, int key) {
        if (root == NIL) {
            return new NoTagPersistentTreap[]{NIL, NIL};
        }
        root = root.clone();
        NoTagPersistentTreap[] result;
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

    public static int getKeyByRank(NoTagPersistentTreap treap, int k) {
        while (treap.size > 1) {
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
}