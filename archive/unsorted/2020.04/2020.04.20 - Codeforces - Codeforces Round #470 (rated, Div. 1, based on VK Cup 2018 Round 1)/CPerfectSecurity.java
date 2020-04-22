package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

public class CPerfectSecurity {
    static final int HEIGHT = 29;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt();
        }

        BNode root = new BNode();
        for (int i = 0; i < n; i++) {
            root.add(p[i], HEIGHT);
        }

        for (int i = 0; i < n; i++){
            int ans = root.pop(a[i], 0, HEIGHT);
            out.append(ans ^ a[i]).append(' ');
        }
    }
}

class BNode {
    BNode[] next = new BNode[2];
    int size;

    public BNode get(int i) {
        if (next[i] == null) {
            next[i] = new BNode();
        }
        return next[i];
    }

    public boolean isEmpty(int i) {
        return next[i] == null || next[i].size == 0;
    }

    public int pop(int x, int trace, int k) {
        size--;
        if (k < 0) {
            return trace;
        }
        int bit = Bits.bitAt(x, k);
        if (!isEmpty(bit)) {
            return get(bit).pop(x, Bits.setBit(trace, k, bit == 1), k - 1);
        } else {
            return get(1 - bit).pop(x, Bits.setBit(trace, k, (1 - bit) == 1), k - 1);
        }
    }

    public void add(int x, int k) {
        size++;
        if (k < 0) {
            return;
        }
        get(Bits.bitAt(x, k)).add(x, k - 1);
    }
}