package template.datastructure;

import java.util.Random;

public class PersistentTreap implements Cloneable {
    private static Random random = new Random();
    static PersistentTreap NIL = new PersistentTreap();

    static {
        NIL.left = NIL.right = NIL;
        NIL.size = 0;
    }

    PersistentTreap left = NIL;
    PersistentTreap right = NIL;
    int size = 1;
    int key;

    @Override
    public PersistentTreap clone() {
        if (this == NIL) {
            return this;
        }
        try {
            return (PersistentTreap) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void pushDown() {
        if (this == NIL) {
            return;
        }
        left = left.clone();
        right = right.clone();
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
    public static PersistentTreap[] splitByRank(PersistentTreap root, int rank) {
        if (root == NIL) {
            return new PersistentTreap[]{NIL, NIL};
        }
        root.pushDown();
        PersistentTreap[] result;
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

    public static PersistentTreap merge(PersistentTreap a, PersistentTreap b) {
        if (a == NIL) {
            return b;
        }
        if (b == NIL) {
            return a;
        }
        if (random.nextInt(a.size + b.size) < a.size) {
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

    public static void toString(PersistentTreap root, StringBuilder builder) {
        if (root == NIL) {
            return;
        }
        root.pushDown();
        toString(root.left, builder);
        builder.append(root.key).append(',');
        toString(root.right, builder);
    }

    public static PersistentTreap clone(PersistentTreap root) {
        if (root == NIL) {
            return NIL;
        }
        PersistentTreap clone = root.clone();
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
    public static PersistentTreap[] splitByKey(PersistentTreap root, int key) {
        if (root == NIL) {
            return new PersistentTreap[]{NIL, NIL};
        }
        root.pushDown();
        PersistentTreap[] result;
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

    public static int getKeyByRank(PersistentTreap PersistentTreap, int k) {
        while (PersistentTreap.size > 1) {
            PersistentTreap.pushDown();
            if (PersistentTreap.left.size >= k) {
                PersistentTreap = PersistentTreap.left;
            } else {
                k -= PersistentTreap.left.size;
                if (k == 1) {
                    break;
                }
                k--;
                PersistentTreap = PersistentTreap.right;
            }
        }
        return PersistentTreap.key;
    }
}
