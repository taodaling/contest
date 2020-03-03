package on2020_03.on2020_03_03_Codeforces_Round__495__Div__2_.F__Sonya_and_Bitwise_OR;



import template.binary.Bits;
import template.datastructure.PreXor;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

public class FSonyaAndBitwiseOR {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int x = in.readInt();
        int[] val = new int[n];
        for (int i = 0; i < n; i++) {
            val[i] = in.readInt();
        }
        int bSize = (int) Math.ceil(Math.sqrt(n));
        int split = DigitUtils.ceilDiv(n, bSize);
        Block[] blocks = new Block[split];  
        for (int i = 0; i < split; i++) {
            blocks[i] = new Block();
            blocks[i].pre = new int[bSize];
        }

        debug.debug("val", val);
        PreXor xor = new PreXor(val);
        int limit = 19;
        for (int i = 0; i < n; i++) {
            blocks[i / bSize].pre[i % bSize] = (int) xor.prefix(i);
            blocks[i / bSize].bTree.add((int) xor.prefix(i), limit, 1);
        }

        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            if (t == 1) {
                int index = in.readInt() - 1;
                int y = in.readInt();
                int ll = y;
                int rr = n - 1;
                for (int j = index / bSize; j < blocks.length; j++) {
                    int l = j * bSize;
                    int r = l + bSize - 1;
                    blocks[j].xor(Math.max(l, ll) - l, Math.min(r, rr) - l, y ^ val[index]);
                }
                val[index] = y;
            } else {
                int ll = in.readInt() - 1;
                int rr = in.readInt() - 1;
                int mask = 0;
                if (ll > 0) {
                    int bId = (ll - 1) / bSize;
                    mask = blocks[bId].get(ll - 1 - bId * bSize);
                }
                int ans = 0;
                for (int j = 0; j < blocks.length; j++) {
                    int l = j * bSize;
                    int r = l + bSize - 1;
                    ans += blocks[j].greaterThan(Math.max(l, ll) - l, Math.min(r, rr) - l, mask, x);
                }
                out.println(ans);
            }
        }
    }
}

class Block {
    BTree bTree = new BTree();
    int mask;
    int[] pre;

    public int greaterThan(int l, int r, int xor, int x) {
        if (l == 0 && r == pre.length - 1) {
            return bTree.greater(x, 19, xor ^ mask);
        } else {
            xor ^= mask;
            int cnt = 0;
            for (int i = l; i <= r; i++) {
                if ((pre[i] ^ xor) >= x) {
                    cnt++;
                }
            }
            return cnt;
        }
    }

    public void xor(int l, int r, int x) {
        if (l == 0 && r == pre.length - 1) {
            mask ^= x;
        }
        for (int i = l; i <= r; i++) {
            bTree.add(pre[i], 19, -1);
            pre[i] ^= x;
            bTree.add(pre[i], 19, 1);
        }
    }

    public int get(int i) {
        return pre[i] ^ mask;
    }
}

class BTree {
    BTree[] next = new BTree[2];
    int size = 0;

    public void add(int x, int bit, int mod) {
        if (bit < 0) {
            size += mod;
            return;
        }
        size += mod;
        get(Bits.bitAt(x, bit)).add(x, bit - 1, mod);
    }

    public int greater(int x, int bit, int xor) {
        int y = Bits.bitAt(xor, bit);
        if (Bits.bitAt(x, bit) == 1) {
            if (getSize(1 ^ y) == 0) {
                return 0;
            }
            return get(1 ^ y).greater(x, bit - 1, xor);
        }
        int ans = getSize(1 ^ y);
        if (getSize(y) > 0) {
            ans += get(y).greater(x, bit - 1, xor);
        }
        return ans;
    }

    public int getSize(int i) {
        return next[i] == null ? 0 : next[i].size;
    }

    public BTree get(int i) {
        if (next[i] == null) {
            next[i] = new BTree();
        }
        return next[i];
    }
}