package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class CXorTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int highest = 30 - 1;
        BinaryTree bt = new BinaryTree();
        for (int x : a) {
            bt.add(x, highest, 1);
        }
        long ans = dp(bt, highest);
        out.println(n - ans);
    }

    public int dp(BinaryTree bt, int h) {
        if (bt == null || bt.size == 0) {
            return 0;
        }
        if (h == -1) {
            return 1;
        }
        int left = dp(bt.left, h - 1);
        int right = dp(bt.right, h - 1);
        return Math.max(left + Math.min(right, 1), right + Math.min(left, 1));
    }
}

class BinaryTree {
    BinaryTree left;
    BinaryTree right;
    int size;

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
        get(Bits.get(x, height)).add(x, height - 1, mod);
        size += mod;
    }

    public int find(int x, int height) {
        if (height < 0) {
            return size;
        }
        return get(Bits.get(x, height)).find(x, height - 1);
    }

    public int kthElement(int k, int height) {
        if (height < 0) {
            return 0;
        }
        if (size(0) >= k) {
            return get(0).kthElement(k, height - 1);
        }
        return get(1).kthElement(k - size(0), height - 1);
    }

    public int prefixSum(int x, int height) {
        if (height < 0) {
            return size;
        }
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

    public int maxXor(int x, int height) {
        if (height < 0) {
            return 0;
        }
        int prefer = Bits.get(x, height) ^ 1;
        int ans;
        if (size(prefer) > 0) {
            ans = get(prefer).maxXor(x, height - 1);
            ans = Bits.set(ans, height);
        } else {
            ans = get(1 ^ prefer).maxXor(x, height - 1);
        }
        return ans;
    }

    public int minXor(int x, int height) {
        if (height < 0) {
            return 0;
        }
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
}
