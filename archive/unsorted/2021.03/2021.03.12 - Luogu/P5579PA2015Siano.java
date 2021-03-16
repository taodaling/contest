package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.rand.RandomWrapper;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.IntFunction;

public class P5579PA2015Siano {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Treap root = Treap.NIL;
        for (int i = 0; i < n; i++) {
            Treap node = new Treap();
            node.key = in.ri();
            node.pushUp();
            Treap[] pair = Treap.splitByKey(root, node.key);
            pair[0] = Treap.merge(pair[0], node);
            root = Treap.merge(pair[0], pair[1]);
        }
        Deque<Item> dq = new ArrayDeque<>(n);
        dq.add(new Item(root, 0, 0));
        for (int i = 0; i < m; i++) {
            long d = in.rl();
            long b = in.rl();
            long sum = 0;
            Item newItem = new Item(Treap.NIL, d, b);
            while (!dq.isEmpty()) {
                Item head = dq.removeFirst();
                long speedAtLeast = DigitUtils.ceilDiv(b - head.lastRetain, d - head.lastVisit);
                Treap[] pair = Treap.splitByKey(head.root, speedAtLeast - 1);
                sum += (pair[1].sumKey * (d - head.lastVisit) + pair[1].size * head.lastRetain)
                        - (pair[1].size * b);
                newItem.root = Treap.merge(pair[1], newItem.root);
                head.root = pair[0];
                if (pair[0].size > 0) {
                    dq.addFirst(head);
                    break;
                }
            }
            if (newItem.root != Treap.NIL) {
                dq.addFirst(newItem);
            }
            out.println(sum);
        }
    }
}

class Item {
    Treap root;
    long lastVisit;
    long lastRetain;

    public Item(Treap root, long lastVisit, long lastRetain) {
        this.root = root;
        this.lastVisit = lastVisit;
        this.lastRetain = lastRetain;
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
    public long sumKey;

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
        sumKey = key + left.sumKey + right.sumKey;
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
    public static Treap[] splitByKey(Treap root, long key) {
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

    public static long getKeyByRank(Treap treap, int k) {
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
