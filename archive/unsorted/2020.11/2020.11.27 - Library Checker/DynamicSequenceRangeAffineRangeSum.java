package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Random;

public class DynamicSequenceRangeAffineRangeSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        Treap root = Treap.NIL;
        for (int i = 0; i < n; i++) {
            Treap t = new Treap();
            t.v = a[i];
            t.pushUp();
            root = Treap.merge(root, t);
        }
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 0) {
                int index = in.ri();
                int x = in.ri();
                Treap node = new Treap();
                node.v = x;
                node.pushUp();
                Treap[] splited = Treap.splitByRank(root, index);
                splited[0] = Treap.merge(splited[0], node);
                root = Treap.merge(splited[0], splited[1]);
            } else if (t == 1) {
                int index = in.ri();
                Treap[] splited = Treap.splitByRank(root, index);
                splited[1] = Treap.splitByRank(splited[1], 1)[1];
                root = Treap.merge(splited[0], splited[1]);
            } else if (t == 2) {
                int l = in.ri();
                int r = in.ri();
                Treap[] split0 = Treap.splitByRank(root, l);
                Treap[] split1 = Treap.splitByRank(split0[1], r - l);
                split1[0].reverse();
                split0[1] = Treap.merge(split1[0], split1[1]);
                root = Treap.merge(split0[0], split0[1]);
            } else if (t == 3) {
                int l = in.ri();
                int r = in.ri();
                int aa = in.ri();
                int bb = in.ri();
                Treap[] split0 = Treap.splitByRank(root, l);
                Treap[] split1 = Treap.splitByRank(split0[1], r - l);
                split1[0].modify(aa, bb);
                split0[1] = Treap.merge(split1[0], split1[1]);
                root = Treap.merge(split0[0], split0[1]);
            } else if (t == 4) {
                int l = in.ri();
                int r = in.ri();
                Treap[] split0 = Treap.splitByRank(root, l);
                Treap[] split1 = Treap.splitByRank(split0[1], r - l);
                out.println(split1[0].sum);
                split0[1] = Treap.merge(split1[0], split1[1]);
                root = Treap.merge(split0[0], split0[1]);
            }
        }
    }
}

class Treap implements Cloneable {
    private static Random random = new Random();

    public static final Treap NIL = new Treap();

    static {
        NIL.left = NIL.right = NIL;
        NIL.size = 0;
    }

    public Treap left = NIL;
    public Treap right = NIL;
    public int size = 1;
    private static int mod = 998244353;
    public long a = 1;
    public long b = 0;
    public long v;
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

    public void modify(long aa, long bb) {
        a = a * aa % mod;
        b = (b * aa + bb) % mod;
        v = (aa * v + bb) % mod;
        sum = (sum * aa + bb * size) % mod;
    }

    public void reverse() {
        rev = !rev;
        Treap tmp = left;
        left = right;
        right = tmp;
    }

    public void pushDown() {
        if (this == NIL) {
            return;
        }
        if (!(a == 1 && b == 0)) {
            left.modify(a, b);
            right.modify(a, b);
            a = 1;
            b = 0;
        }
        if (rev) {
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
        sum = (left.sum + right.sum + v) % mod;
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

    public static void toString(Treap root, StringBuilder builder) {
        if (root == NIL) {
            return;
        }
        root.pushDown();
        toString(root.left, builder);
        builder.append(root.v).append(',');
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
        StringBuilder builder = new StringBuilder().append(v).append(":");
        toString(clone(this), builder);
        return builder.toString();
    }


}
