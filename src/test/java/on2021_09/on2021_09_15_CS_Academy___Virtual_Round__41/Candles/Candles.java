package on2021_09.on2021_09_15_CS_Academy___Virtual_Round__41.Candles;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;
import template.rand.Randomized;

import java.util.Arrays;
import java.util.function.IntFunction;

public class Candles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] h = in.ri(n);
        Randomized.shuffle(h);
        Arrays.sort(h);
        Treap root = Treap.build(i -> {
            Treap ans = new Treap();
            ans.key = h[i];
            ans.pushUp();
            return ans;
        }, 0, n - 1);

        int[] c = in.ri(m);
        int best = 0;
        for (int i = 0; i < m; i++) {
            int req = c[i];
            if (req > n) {
                break;
            }
            int before = n - req;
            Treap[] split1 = Treap.splitByRank(root, before);
            int min = Treap.getKeyByRank(split1[1], 1);
            if(min <= 0){
                break;
            }
            split1[1].modify(-1);
            Treap[] split2 = Treap.splitByKey(split1[1], min - 1);
            Treap[] split3 = Treap.splitByKey(split1[0], min - 1);
            split3[0] = Treap.merge(split3[0], split2[0]);
            split1[0] = Treap.merge(split3[0], split3[1]);
            split1[1] = split2[1];
            root = Treap.merge(split1[0], split1[1]);
            best = i + 1;
        }
        out.println(best);

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
    public int key;
    int dirty;

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

    public void modify(int x){
        if(this == NIL){
            return;
        }
        dirty += x;
        key += x;
    }

    public void pushDown() {
        if (this == NIL) {
            return;
        }
        if(dirty != 0){
            left.modify(dirty);
            right.modify(dirty);
            dirty = 0;
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