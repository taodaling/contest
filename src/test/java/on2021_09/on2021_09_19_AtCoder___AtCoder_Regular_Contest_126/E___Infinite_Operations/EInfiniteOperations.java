package on2021_09.on2021_09_19_AtCoder___AtCoder_Regular_Contest_126.E___Infinite_Operations;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.IntMath;
import template.math.Power;
import template.rand.RandomWrapper;

public class EInfiniteOperations {
    int mod = 998244353;
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = in.ri(n);
        for (int x : a) {
            add(x);
        }
        long invn = pow.inverse(n);
        for (int i = 0; i < q; i++) {
            int index = in.ri() - 1;
            int to = in.ri();
            remove(a[index]);
            a[index] = to;
            add(a[index]);
            long sum = root.sum;
            long avg = sum % mod * invn % mod;
            Treap[] split = Treap.splitByKey(root, sum, n);
            int m = split[0].size;
            long got = 0;
            got += IntMath.sumOfInterval(1, m) % mod * avg % mod;
            got += split[0].weightSum;
            got -= split[0].sum * (m + 1) % mod;
            got += split[1].weightSum;
            got -= (m + 1) * split[1].sum % mod;
            got -= IntMath.sumOfInterval(0, n - (m + 1)) % mod * avg % mod;
            got = DigitUtils.mod(got, mod);
            out.println(got);

            root = Treap.merge(split[0], split[1]);
        }
    }

    Treap root = Treap.NIL;

    public void remove(int k) {
        Treap[] split = Treap.splitByKey(root, k);
        split[0] = Treap.splitByRank(split[0], split[0].size - 1)[0];
        split[1].modifyIndex(-1);
        root = Treap.merge(split[0], split[1]);
    }

    public void add(int k) {
        Treap[] split = Treap.splitByKey(root, k);
        Treap node = new Treap();
        node.modify(k, split[0].size + 1);
        split[1].modifyIndex(1);
        split[0] = Treap.merge(split[0], node);
        root = Treap.merge(split[0], split[1]);
    }
}

class Treap implements Cloneable {
    public static final Treap NIL = new Treap();
    public Treap left = NIL;
    public Treap right = NIL;
    public int size = 1;
    public int key;
    public long index;
    public long sum;
    public long indexSum;
    public long weightSum;
    public long indexOffset;

    static {
        NIL.left = NIL.right = NIL;
        NIL.size = 0;
    }

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
        if (indexOffset != 0) {
            left.modifyIndex(indexOffset);
            right.modifyIndex(indexOffset);
            indexOffset = 0;
        }
    }

    public void modify(int x, int y) {
        if (this == NIL) {
            return;
        }
        key = x;
        index = y;
        pushUp();
    }

    public void modifyIndex(long x) {
        if (this == NIL) {
            return;
        }
        indexOffset += x;
        index += x;
        indexSum += size * x;
        weightSum += sum * x;
        indexSum %= mod;
        weightSum %= mod;
    }

    static int mod = 998244353;

    public void pushUp() {
        if (this == NIL) {
            return;
        }
        sum = (left.sum + right.sum + key) % mod;
        indexSum = (left.indexSum + right.indexSum + index) % mod;
        weightSum = (left.weightSum + right.weightSum + index * key) % mod;
        size = left.size + right.size + 1;
    }

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

    public String toString() {
        StringBuilder builder = new StringBuilder().append(key).append(":");
        toString(clone(this), builder);
        return builder.toString();
    }

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

    public static Treap[] splitByKey(Treap root, long sum, int n) {
        if (root == NIL) {
            return new Treap[]{NIL, NIL};
        }
        root.pushDown();
        Treap[] result;
        if ((long) root.key * n > sum) {
            result = splitByKey(root.left, sum, n);
            root.left = result[1];
            result[1] = root;
        } else {
            result = splitByKey(root.right, sum, n);
            root.right = result[0];
            result[0] = root;
        }
        root.pushUp();
        return result;
    }

}