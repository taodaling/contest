package on2020_07.on2020_07_21_Codeforces___Codeforces_Round__658__Div__1_.A2__Prefix_Flip__Hard_Version_;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Random;

public class A2PrefixFlipHardVersion {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];
        in.readString(a, 0);
        in.readString(b, 0);
        Treap tree = Treap.NIL;
        for (int i = 0; i < n; i++) {
            a[i] -= '0';
            b[i] -= '0';
            Treap newNode = new Treap();
            newNode.key = a[i];
            tree = Treap.merge(tree, newNode);
        }

        IntegerArrayList seq = new IntegerArrayList(2 * n);
        for (int i = n - 1; i >= 0; i--) {
            int val = Treap.getKeyByRank(tree, i + 1);
            if (val == b[i]) {
                continue;
            }
            if (Treap.getKeyByRank(tree, 1) != val) {
                tree = reversePrefix(tree, 1, seq);
            }
            tree = reversePrefix(tree, i + 1, seq);
        }

        out.append(seq.size()).append(' ');
        for (int i = 0; i < seq.size(); i++) {
            out.append(seq.get(i)).append(' ');
        }

//        for (int i = 0; i < n; i++) {
//            if (b[i] != Treap.getKeyByRank(tree, i + 1)) {
//                throw new RuntimeException();
//            }
//        }

        out.println();
    }

    public Treap reversePrefix(Treap t, int k, IntegerArrayList seq) {
        seq.add(k);
        Treap[] split = Treap.splitByRank(t, k);
        split[0].reverse();
        return Treap.merge(split[0], split[1]);
    }
}

class Treap implements Cloneable {
    private static Random random = new Random();

    static Treap NIL = new Treap();

    static {
        NIL.left = NIL.right = NIL;
        NIL.size = 0;
    }

    Treap left = NIL;
    Treap right = NIL;
    int size = 1;
    int key;
    boolean reverse;

    public void reverse() {
        reverse = !reverse;
        key = 1 - key;
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
        if (reverse) {
            left.reverse();
            right.reverse();
            Treap tmp = left;
            left = right;
            right = tmp;
            reverse = false;
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
