package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Pair;

public class RiolkusMockCCCS5KeenKeenerMultiset {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        BinaryTree root = new BinaryTree();
        int h = 19;

        for (int i = 0; i < n; i++) {
            int x = in.ri();
            root.add(x, h, 1);
        }
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            if (t == 1) {
                int x = in.ri();
                root.add(x, h, 1);
            } else if (t == 2) {
                int l = in.ri();
                int r = in.ri();
                int x = in.ri();
                Pair<BinaryTree, BinaryTree> split0 = BinaryTree.split(root, r, h, true);
                Pair<BinaryTree, BinaryTree> split1 = BinaryTree.split(split0.a, l, h, false);
                split1.b.modify(x, h);
                split0.a = BinaryTree.merge(split1.a, split1.b, h);
                root = BinaryTree.merge(split0.a, split0.b, h);
            }else{
                int l = in.ri();
                int r = in.ri();
                int sum = BinaryTree.prefix(root, r, h, true) ^ BinaryTree.prefix(root, l, h, false);
                out.println(sum);
            }
        }
    }
}


class BinaryTree implements Cloneable {
    public BinaryTree left;
    public BinaryTree right;
    public int xor;
    public int size;
    int mod;

    private BinaryTree get(int i) {
        if (i == 0) {
            if (left == null) {
                left = new BinaryTree();
            }
            return left;
        } else {
            if (right == null) {
                right = new BinaryTree();
            }
            return right;
        }
    }


    private BinaryTree getRaw(int i) {
        if (i == 0) {
            return left;
        } else {
            return right;
        }
    }

    public void modify(int x, int height) {
        if ((size & 1) == 1) {
            xor ^= x;
        }
        mod ^= x;
    }

    public void add(int x, int height, int mod) {
        if (height < 0) {
            xor ^= x;
            size += mod;
            return;
        }
        pushDown(height);
        get(Bits.get(x, height)).add(x, height - 1, mod);
        pushUp();
    }

    public void pushUp() {
        xor = 0;
        size = 0;
        if (left != null) {
            xor ^= left.xor;
            size += left.size;
        }
        if (right != null) {
            xor ^= right.xor;
            size += right.size;
        }
    }

    public static int prefix(BinaryTree bt, int key, int height, boolean include) {
        if (bt == null) {
            return 0;
        }
        if (height == -1) {
            return include ? bt.xor : 0;
        }
        bt.pushDown(height);
        int ans = prefix(bt.getRaw(Bits.get(key, height)), key, height - 1, include);
        if (Bits.get(key, height) == 1) {
            BinaryTree left = bt.left;
            if (left != null) {
                ans ^= left.xor;
            }
        }
        return ans;
    }

    public void pushDown(int height) {
        if (mod != 0) {
            if (Bits.get(mod, height) == 1) {
                BinaryTree tmp = left;
                left = right;
                right = tmp;
            }
            if (left != null) {
                left.modify(mod, height - 1);
            }
            if (right != null) {
                right.modify(mod, height - 1);
            }
            mod = 0;
        }
    }

    /**
     * res.a <= key and res.b > key
     *
     * @param key
     * @return
     */
    public static Pair<BinaryTree, BinaryTree> split(BinaryTree bt, int key, int height, boolean toLeft) {
        if (bt == null || height == -1) {
            if (toLeft) {
                return new Pair<>(bt, null);
            } else {
                return new Pair<>(null, bt);
            }
        }
        Pair<BinaryTree, BinaryTree> ans;
        bt.pushDown(height);
        if (Bits.get(key, height) == 0) {
            ans = split(bt.left, key, height - 1, toLeft);
            bt.left = ans.b;
            BinaryTree a = new BinaryTree();
            a.left = ans.a;
            ans.a = a;
            ans.b = bt;
        } else {
            ans = split(bt.right, key, height - 1, toLeft);
            bt.right = ans.a;
            BinaryTree b = new BinaryTree();
            b.right = ans.b;
            ans.a = bt;
            ans.b = b;
        }
        ans.a.pushUp();
        ans.b.pushUp();
        return ans;
    }

    public static BinaryTree merge(BinaryTree a, BinaryTree b, int height) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        if(height == -1){
            a.size += b.size;
            a.xor ^= b.xor;
            return a;
        }
        a.pushDown(height);
        b.pushDown(height);
        a.left = merge(a.left, b.left, height - 1);
        a.right = merge(a.right, b.right, height - 1);
        a.pushUp();
        return a;
    }

    @Override
    public BinaryTree clone() {
        try {
            return (BinaryTree) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
