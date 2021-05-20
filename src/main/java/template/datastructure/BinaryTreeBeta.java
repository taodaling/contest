package template.datastructure;

import template.binary.Bits;
import template.binary.Log2;

public class BinaryTreeBeta implements Cloneable {
    int[][] child;
    int[] size;
    int height;
    int alloc = 0;
    int root;

    public int alloc() {
        child[0][alloc] = child[1][alloc] = 0;
        size[alloc] = 0;
        return alloc++;
    }

    public void clear() {
        alloc = 1;
        root = 0;
    }

    public int size() {
        return size[root];
    }

    public BinaryTreeBeta(int largest, int opNum) {
        height = Log2.floorLog(largest);
        int cap = (opNum + 10) * (height + 2) + 1;
        child = new int[2][cap];
        size = new int[cap];
        clear();
    }

    public void add(int x, int mod) {
        root = add(root, height, x, mod);
    }

    private void modify(int i, int mod) {
        size[i] += mod;
    }

    private void pushUp(int i) {
        size[i] = size[child[0][i]] + size[child[1][i]];
    }

    private void pushDown(int i) {
    }

    private int add(int node, int h, int x, int mod) {
        if (node == 0) {
            node = alloc();
        }
        if (h < 0) {
            modify(node, mod);
        } else {
            pushDown(node);
            int bit = Bits.get(x, h);
            child[bit][node] = add(child[bit][node], h - 1, x, mod);
            pushUp(node);
        }
        return node;
    }

    public int maxXor(int x) {
        assert size[root] > 0;
        return genericXor(root, height, x, 1);
    }

    public int minXor(int x) {
        assert size[root] > 0;
        return genericXor(root, height, x, 0);
    }

    private int genericXor(int node, int h, int x, int xor) {
        if (h == -1) {
            return 0;
        }
        pushDown(node);
        int prefer = Bits.get(x, h) ^ xor;
        if (size[child[prefer][node]] > 0) {
            return genericXor(child[prefer][node], h - 1, x, xor) | (prefer << h);
        } else {
            return genericXor(child[1 ^ prefer][node], h - 1, x, xor) | ((1 ^ prefer) << h);
        }
    }

    public int prefixSize(int x) {
        return prefixSize(root, height, x);
    }

    public int intervalSize(int l, int r) {
        int ans = prefixSize(r);
        if (l > 0) {
            ans -= prefixSize(l - 1);
        }
        return ans;
    }

    private int prefixSize(int node, int h, int x) {
        if (h == -1) {
            return size[node];
        }
        pushDown(node);
        int bit = Bits.get(x, h);
        int ans = prefixSize(child[bit][node], h - 1, x);
        if (bit == 1) {
            ans += size[child[0][node]];
        }
        return ans;
    }

    public int kthSmallest(int k) {
        assert size[root] >= k;
        return kthSmallest(root, height, k);
    }

    private int kthSmallest(int node, int h, int k) {
        if (h == -1) {
            return 0;
        }
        pushDown(node);
        if (size[child[0][node]] >= k) {
            return kthSmallest(child[0][node], h - 1, k);
        } else {
            return kthSmallest(child[1][node], h - 1, k - size[child[0][node]]) | (1 << h);
        }
    }

    private void dfsToString(int node, int h, int v, StringBuilder sb) {
        if (node == 0) {
            return;
        }
        if (h == -1) {
            if (size[node] > 0) {
                sb.append(v).append(":").append(size[node]).append(",");
            }
            return;
        }
        pushDown(node);
        dfsToString(child[0][node], h - 1, (v << 1) | 0, sb);
        dfsToString(child[1][node], h - 1, (v << 1) | 1, sb);
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder("{");
        clone().dfsToString(root, height, 0, ans);
        if (ans.length() > 0 && ans.charAt(ans.length() - 1) == ',') {
            ans.setLength(ans.length() - 1);
        }
        ans.append("}");
        return ans.toString();
    }

    @Override
    protected BinaryTreeBeta clone() {
        try {
            BinaryTreeBeta ans = (BinaryTreeBeta) super.clone();
            ans.child = ans.child.clone();
            ans.child[0] = ans.child[0].clone();
            ans.child[1] = ans.child[1].clone();
            ans.size = ans.size.clone();
            return ans;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
