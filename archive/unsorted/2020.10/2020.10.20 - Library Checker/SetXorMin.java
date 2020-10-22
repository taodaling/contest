package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.utils.Debug;

import java.io.PrintWriter;

public class SetXorMin {

    int highest = 29;
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int q = in.readInt();
        BTree root = new BTree();
        for (int i = 0; i < q; i++) {
            debug.debug("i", i);
            int t = in.readInt();
            if (t == 0) {
                root.add(in.readInt(), highest, true);
            } else if (t == 1) {
                root.add(in.readInt(), highest, false);
            } else {
                assert root.size > 0;
                int x = in.readInt();
                int y = root.minXor(x, highest, 0);
                out.println(x ^ y);
            }
        }
    }
}

class BTree {
    BTree[] next = new BTree[2];
    int size;

    public BTree getOrCreate(int i) {
        if (next[i] == null) {
            next[i] = new BTree();
        }
        return next[i];
    }

    public int getSize(int i) {
        return next[i] == null ? 0 : next[i].size;
    }

    public int minXor(int x, int bit, int ans) {
        if (bit < 0) {
            return ans;
        }
        int y = Bits.get(x, bit);
        if (getSize(y) == 0) {
            y ^= 1;
        }
        return getOrCreate(y).minXor(x, bit - 1, Bits.set(ans, bit, y == 1));
    }

    public int add(int x, int bit, boolean add) {
        int preSize = size;
        if (bit < 0) {
            size = add ? 1 : 0;
        } else {
            size += getOrCreate(Bits.get(x, bit)).add(x, bit - 1, add);
        }
        return size - preSize;
    }
}