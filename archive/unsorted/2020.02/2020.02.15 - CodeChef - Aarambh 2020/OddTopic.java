package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.BitSet;

public class OddTopic {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int q = in.readInt();
        BitSet[] a = new BitSet[n + 1];
        BitSet[] b = new BitSet[n + 1];
        a[0] = b[0] = new BitSet(4000 + 1);
        for (int i = 1; i <= n; i++) {
            int x = in.readInt();
            a[i] = (BitSet) a[i - 1].clone();
            a[i].set(x, !a[i].get(x));
        }
        for (int i = 1; i <= m; i++) {
            int x = in.readInt();
            b[i] = (BitSet) b[i - 1].clone();
            b[i].set(x, !b[i].get(x));
        }
        for (int i = 0; i < q; i++) {
            BitSet ans = (BitSet) a[in.readInt() - 1].clone();
            ans.xor(a[in.readInt()]);
            ans.xor(b[in.readInt() - 1]);
            ans.xor(b[in.readInt()]);
            out.println(ans.cardinality());
        }
    }
}
