package on2021_05.on2021_05_25_AtCoder___Japanese_Student_Championship_2021.F___Max_Matrix;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;
import template.utils.CloneSupportObject;
import template.utils.Sum;
import template.utils.Update;

import java.util.function.IntFunction;

public class FMaxMatrix {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int q = in.ri();


        int[] a = new int[n];
        int[] b = new int[m];
        Treap t1 = Treap.build(i -> new Treap(), 0, n - 1);
        Treap t2 = Treap.build(i -> new Treap(), 0, m - 1);

        for (int i = 0; i < q; i++) {
            int t = in.ri();
            int x = in.ri() - 1;
            int y = in.ri();

            if(t == 1){
                Treap[] res = replace(t1, t2, a[x], y, true);
                t1 = res[0];
                t2 = res[1];
                a[x] = y;
            }else{
                Treap[] res = replace(t2, t1, b[x], y, false);
                t1 = res[1];
                t2 = res[0];
                b[x] = y;
            }

            out.println(ans);
        }
    }

    long ans = 0;

    public Treap[] replace(Treap t1, Treap t2, int oldKey, int newKey, boolean preferT1) {
        //remove node at first
        Treap[] t1Split = Treap.splitByKey(t1, oldKey);
        Treap[] t2Split = Treap.splitByKey(t2, preferT1 ? oldKey : oldKey - 1);

        ans -= t2Split[0].size * (long) oldKey;
        ans -= t2Split[1].sum;

        Treap[] t1Split1 = Treap.splitByRank(t1Split[0], t1Split[0].size - 1);
        t1Split1[1].key = newKey;
        t1Split1[1].pushUp();
        Treap newNode = t1Split1[1];

        t1Split[0] = t1Split1[0];
        t1 = Treap.merge(t1Split[0], t1Split[1]);
        t2 = Treap.merge(t2Split[0], t2Split[1]);

        t1Split = Treap.splitByKey(t1, newKey);
        t2Split = Treap.splitByKey(t2, preferT1 ? newKey : newKey - 1);

        ans += t2Split[0].size * (long) newKey;
        ans += t2Split[1].sum;

        t1Split[0] = Treap.merge(t1Split[0], newNode);
        t1 = Treap.merge(t1Split[0], t1Split[1]);
        t2 = Treap.merge(t2Split[0], t2Split[1]);

        return new Treap[]{t1, t2};
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
    long sum;

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

