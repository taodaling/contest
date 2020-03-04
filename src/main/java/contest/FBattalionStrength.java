package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Modular;
import template.math.Power;
import template.utils.Debug;

import java.util.Random;

public class FBattalionStrength {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Modular mod = new Modular(1e9 + 7);
        Power power = new Power(mod);
        int inv2 = power.inverseByFermat(2);

        int n = in.readInt();
        Treap root = Treap.NIL;
        int[] val = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            int x = in.readInt();
            val[i] = x;
            root = add(root, create(x));
        }

        //debug.debug("1/2(1/4+13)", mod.mul(inv2, mod.plus(power.inverseByFermat(4), 13)));
        //debug.debug("11/2", mod.mul(inv2, 11));

        int q = in.readInt();
        out.println(mod.mul(inv2, root.exp));
        for (int i = 0; i < q; i++) {
           // debug.debug("i", i);
            int index = in.readInt();
            int x = in.readInt();
            root = remove(root, val[index]);
            val[index] = x;
            root = add(root, create(val[index]));
            out.println(mod.mul(inv2, root.exp));
        }
    }

    public Treap add(Treap a, Treap b) {
        Treap[] split = Treap.splitByKey(a, b.key);
        b.modify(split[0].size);
        split[1].modify(1);
        split[0] = Treap.merge(split[0], b);
        return Treap.merge(split[0], split[1]);
    }

    public Treap remove(Treap a, int x) {
        Treap[] split = Treap.splitByKey(a, x);
        split[1].modify(-1);
        split[0] = Treap.splitByRank(split[0], split[0].size - 1)[0];
        return Treap.merge(split[0], split[1]);
    }

    public Treap create(int x) {
        Treap t = new Treap();
        t.key = x;
        t.index = 0;
        t.pushUp();
        return t;
    }
}


class Treap implements Cloneable {
    private static Random random = new Random(0);

    static Treap NIL = new Treap();
    static Modular mod = new Modular(1e9 + 7);
    static Modular powMod = mod.getModularForPowerComputation();
    static CachedPow pow = new CachedPow(2, mod);

    static {
        NIL.left = NIL.right = NIL;
        NIL.size = 0;
    }

    Treap left = NIL;
    Treap right = NIL;
    int size = 1;
    int key;

    int index;
    int dirty;
    int sumX;
    int sumY;
    int exp;


    @Override
    public Treap clone() {
        try {
            return (Treap) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void modify(int d) {
        if (this == NIL) {
            return;
        }
        dirty = powMod.plus(dirty, d);
        index = powMod.plus(index, d);
        sumX = mod.mul(sumX, pow.pow(d));
        sumY = mod.mul(sumY, pow.inverse(d));
    }

    public void pushDown() {
        if (this == NIL) {
            return;
        }
        if (dirty != 0) {
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

        int pos = mod.mul(key, pow.pow(index));
        int inv = mod.mul(key, pow.inverse(index));
        sumX = mod.plus(left.sumX, right.sumX);
        sumX = mod.plus(sumX, pos);

        sumY = mod.plus(left.sumY, right.sumY);
        sumY = mod.plus(sumY, inv);

        exp = mod.plus(left.exp, right.exp);
        exp = mod.plus(exp, mod.mul(left.sumX, mod.plus(inv, right.sumY)));
        exp = mod.plus(exp, mod.mul(pos, right.sumY));
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
}
