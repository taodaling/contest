package on2019_12.on2019_12_23_.LUOGU3369;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Random;

public class LUOGU3369 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Treap root = Treap.NIL;

        for (int i = 0; i < n; i++) {
            //System.err.println(i);
            int op = in.readInt();
            int x = in.readInt();
            switch (op) {
                case 1: {
                    Treap[] splitted = Treap.splitByKey(root, x);
                    Treap newNode = new Treap();
                    newNode.key = x;
                    splitted[0] = Treap.merge(splitted[0], newNode);
                    root = Treap.merge(splitted[0], splitted[1]);
                    break;
                }
                case 2: {
                    Treap[] splitted = Treap.splitByKey(root, x);
                    splitted[0] = Treap.splitByRank(splitted[0], splitted[0].size - 1)[0];
                    root = Treap.merge(splitted[0], splitted[1]);
                    break;
                }
                case 3: {
                    Treap[] splitted = Treap.splitByKey(root, x - 1);
                    out.println(splitted[0].size + 1);
                    root = Treap.merge(splitted[0], splitted[1]);
                    break;
                }
                case 4:
                    int key = Treap.getKeyByRank(root, x);
                    out.println(key);
                    break;
                case 5: {
                    Treap[] splitted = Treap.splitByKey(root, x - 1);
                    out.println(Treap.getKeyByRank(splitted[0], splitted[0].size));
                    root = Treap.merge(splitted[0], splitted[1]);
                    break;
                }
                case 6: {
                    Treap[] splitted = Treap.splitByKey(root, x);
                    out.println(Treap.getKeyByRank(splitted[1], 1));
                    root = Treap.merge(splitted[0], splitted[1]);
                    break;
                }
            }
        }
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

    @Override
    public Treap clone() {
        try {
            return (Treap) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void pushDown() {
    }

    public void pushUp() {
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
}
