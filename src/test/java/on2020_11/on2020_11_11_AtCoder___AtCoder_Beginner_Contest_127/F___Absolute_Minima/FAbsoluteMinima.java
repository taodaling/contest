package on2020_11.on2020_11_11_AtCoder___AtCoder_Beginner_Contest_127.F___Absolute_Minima;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Random;

public class FAbsoluteMinima {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.readInt();
        Treap root = Treap.NIL;
        long constant = 0;
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 1) {
                int a = in.readInt();
                int b = in.readInt();
                constant += b;
                {
                    Treap[] splited = Treap.splitByKey(root, a);
                    Treap node = new Treap();
                    node.key = a;
                    node.pushUp();
                    splited[0] = Treap.merge(splited[0], node);
                    root = Treap.merge(splited[0], splited[1]);
                }
            } else {
                int size = root.size;
                Treap[] splited = Treap.splitByRank(root, DigitUtils.ceilDiv(size, 2));
                long x = Treap.getKeyByRank(splited[0], splited[0].size);
                long ans = x * splited[0].size - splited[0].sum + splited[1].sum - splited[1].size * x;
                out.append(x).append(' ').println(ans + constant);
                root = Treap.merge(splited[0], splited[1]);
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
    public int key;
    public long sum;

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
        sum = left.sum + right.sum + key;
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

    public static int getKeyByRank(Treap treap, int k) {
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

    public static int getRankByKey(Treap treap, int k) {
        int rank = 0;
        while (treap != NIL) {
            if (treap.key == k) {
                rank += treap.left.size + 1;
                return rank;
            } else if (treap.key < k) {
                rank += treap.left.size + 1;
                treap = treap.right;
            } else {
                treap = treap.left;
            }
        }
        return rank;
    }
}
