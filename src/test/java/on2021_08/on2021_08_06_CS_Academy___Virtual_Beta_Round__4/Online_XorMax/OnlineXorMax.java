package on2021_08.on2021_08_06_CS_Academy___Virtual_Beta_Round__4.Online_XorMax;



import template.binary.Bits;
import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Buffer;
import template.utils.Pair;

public class OnlineXorMax {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int[] del = new int[n];
        for (int i = 0; i < n; i++) {
            del[i] = in.ri() - 1;
        }
        DSUExt dsu = new DSUExt(n, a);
        dsu.init();
        boolean[] ok = new boolean[n];
        int[] ans = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            int j = del[i];
            dsu.enable(j);
            ok[j] = true;
            if (j > 0 && ok[j - 1]) {
                dsu.merge(j, j - 1);
            }
            if (j + 1 < n && ok[j + 1]) {
                dsu.merge(j, j + 1);
            }
            ans[i] = dsu.max;
        }
        for (int x : ans) {
            out.println(x);
        }
    }
}

class DSUExt extends DSU {
    int[] left;
    int[] right;
    int max;
    BinaryTree[] bts;
    int h = 29;
    int[] a;
    int[] sum;

    public DSUExt(int n, int[] a) {
        super(n);
        this.a = a;
        bts = new BinaryTree[n];
        sum = new int[n];
        left = new int[n];
        right = new int[n];
        for (int i = 0; i < n; i++) {
            bts[i] = new BinaryTree();
            bts[i].add(a[i], h, 1);
            bts[i].add(0, h, 1);
            sum[i] = a[i];
            max = 0;
        }
    }

    public void enable(int x) {
        max = Math.max(a[x], max);
    }

    @Override
    public void init(int n) {
        super.init(n);
        for (int i = 0; i < n; i++) {
            left[i] = right[i] = i;
        }
    }

    boolean check(int i) {
        boolean ans = bts[i].find(0, h) > 0;
        int ps = 0;
        for (int j = left[i]; j <= right[i]; j++) {
            ps ^= a[j];
            ans = ans && bts[i].find(ps, h) > 0;
        }
        return ans;
    }


    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        bts[b].destroy();
        if (left[a] < left[b]) {
            //right to left
            for (int i = left[b]; i <= right[b]; i++) {
                int x = this.a[i] ^ sum[a];
                sum[a] = x;
                max = Math.max(max, bts[a].maxXor(x, h) ^ x);
                bts[a].add(x, h, 1);
            }
            right[a] = right[b];
        } else {
            for (int i = right[b]; i >= left[b]; i--) {
                max = Math.max(max, bts[a].maxXor(this.a[i], h) ^ this.a[i]);
                sum[a] ^= this.a[i];
                bts[a].xorStructure(this.a[i]);
                bts[a].add(0, h, 1);
            }
            left[a] = left[b];
        }

        assert check(a);
    }
}

class BinaryTree implements Cloneable {
    public BinaryTree left;
    public BinaryTree right;
    public int size;
    public int xor;

    public void xorStructure(int x) {
        xor ^= x;
    }


    static Buffer<BinaryTree> buf = new Buffer<>(BinaryTree::new, t -> {
        t.left = t.right = null;
        t.size = 0;
    });

    private BinaryTree get(int i) {
        if (i == 0) {
            if (left == null) {
                left = buf.alloc();
            }
            return left;
        } else {
            if (right == null) {
                right = buf.alloc();
            }
            return right;
        }
    }


    public void destroy() {
        if (left != null) {
            left.destroy();
        }
        if (right != null) {
            right.destroy();
        }
        buf.release(this);
    }

    public int size(int i) {
        if (i == 0) {
            return left == null ? 0 : left.size;
        }
        return right == null ? 0 : right.size;
    }

    public void add(int x, int height, int mod) {
        if (height < 0) {
            size += mod;
            return;
        }
        pushDown(height);
        get(Bits.get(x, height)).add(x, height - 1, mod);
        pushUp();
    }

    public int find(int x, int height) {
        if (height < 0) {
            return size;
        }
        pushDown(height);
        return get(Bits.get(x, height)).find(x, height - 1);
    }

    public int kthElement(int k, int height) {
        if (height < 0) {
            return 0;
        }
        pushDown(height);
        if (size(0) >= k) {
            return get(0).kthElement(k, height - 1);
        }
        return get(1).kthElement(k - size(0), height - 1);
    }

    public int prefixSum(int x, int height) {
        if (height < 0) {
            return size;
        }
        pushDown(height);
        int ans = get(Bits.get(x, height)).prefixSum(x, height - 1);
        if (Bits.get(x, height) == 1) {
            ans += size(0);
        }
        return ans;
    }

    public int interval(int l, int r, int h) {
        int ans = prefixSum(r, h);
        if (l > 0) {
            ans -= prefixSum(l - 1, h);
        }
        return ans;
    }


    public void pushUp() {
        size = 0;
        if (left != null) {
            size += left.size;
        }
        if (right != null) {
            size += right.size;
        }
    }

    public void pushDown(int height) {
        if (Bits.get(xor, height) == 1) {
            BinaryTree tmp = get(0);
            left = get(1);
            right = tmp;
        }
        if (xor != 0) {
            get(0).xorStructure(xor);
            get(1).xorStructure(xor);
            xor = 0;
        }
    }

    public int maxXor(int x, int height) {
        if (height < 0) {
            return 0;
        }
        pushDown(height);
        int prefer = Bits.get(x, height) ^ 1;
        int ans;
        if (size(prefer) > 0) {
            ans = get(prefer).maxXor(x, height - 1);
            ans |= prefer << height;
        } else {
            ans = get(1 ^ prefer).maxXor(x, height - 1);
            ans |= (1 ^ prefer) << height;
        }
        return ans;
    }

    public int minXor(int x, int height) {
        if (height < 0) {
            return 0;
        }
        pushDown(height);
        int prefer = Bits.get(x, height);
        int ans;
        if (size(prefer) > 0) {
            ans = get(prefer).minXor(x, height - 1);
        } else {
            ans = get(1 ^ prefer).minXor(x, height - 1);
            ans = Bits.set(ans, height);
        }
        return ans;
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
        if (height == -1) {
            a.size += b.size;
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
