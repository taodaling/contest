package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.rand.RandomWrapper;
import template.utils.Debug;
import template.utils.Sum;

public class CApplemanAndASheetOfPaper {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        Treap root = Treap.NIL;
        for (int i = 0; i < n; i++) {
            Treap node = new Treap();
            node.key = 1;
            node.pushUp();
            root = Treap.merge(root, node);
        }
        for (int i = 0; i < q; i++) {
            debug.debug("root", root);
            int t = in.ri();
            if (t == 1) {
                int p = in.ri();
                Treap[] split = Treap.splitByRank(root, p);
                split[0].reverse();

                root = merge(split[0], split[1]);
            } else {
                int l = in.ri() + 1;
                int r = in.ri();
                Treap[] split = Treap.splitByRank(root, r);
                Treap[] split2 = Treap.splitByRank(split[0], l - 1);
                out.println(split2[1].sum);
                split[0] = Treap.merge(split2[0], split2[1]);
                root = Treap.merge(split[0], split[1]);
            }
        }
    }

    public Treap merge(Treap a, Treap b) {
        Treap build = Treap.NIL;
        while (a != Treap.NIL && b != Treap.NIL) {
            Treap[] as = Treap.splitByRank(a, 1);
            Treap[] bs = Treap.splitByRank(b, 1);
            as[0].key += bs[0].key;
            as[0].pushUp();
            build = Treap.merge(build, as[0]);
            a = as[1];
            b = bs[1];
        }
        build = Treap.merge(build, a);
        build = Treap.merge(build, b);
        return build;
    }
}

class Treap implements Cloneable {

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
    public boolean rev;

    @Override
    public Treap clone() {
        try {
            return (Treap) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void reverse() {
        rev = !rev;
    }

    public void pushDown() {
        if (this == NIL) {
            return;
        }
        if (rev) {
            Treap tmp = left;
            left = right;
            right = tmp;
            left.reverse();
            right.reverse();
            rev = false;
        }
    }

    public void pushUp() {
        if (this == NIL) {
            return;
        }
        size = left.size + right.size + 1;
        sum = key + left.sum + right.sum;
    }

    /**
     * split by rank and the node whose rank is argument will stored at result[0]
     */
    public static Treap[] splitByRank(Treap root, int rank) {
        if (root == NIL) {
            return new Treap[]{NIL, NIL};
        }
        root.pushDown();
        Treap[] result;
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

    public static void toString(Treap root, StringBuilder builder) {
        if (root == NIL) {
            return;
        }
        root.pushDown();
        toString(root.left, builder);
        builder.append(root.key).append(',');
        toString(root.right, builder);
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
        StringBuilder builder = new StringBuilder().append(key).append(":");
        toString(clone(this), builder);
        return builder.toString();
    }

    /**
     * nodes with key <= arguments will stored at result[0]
     */
    public static Treap[] splitByKey(Treap root, int key) {
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
