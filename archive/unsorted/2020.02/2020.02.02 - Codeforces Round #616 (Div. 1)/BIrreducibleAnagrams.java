package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerSparseTable;

public class BIrreducibleAnagrams {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] s = new int[2 * (int) 1e5];
        int n = in.readString(s, 0);
        for (int i = 0; i < n; i++) {
            s[i] = 1 << (s[i] - 'a');
        }
        IntegerSparseTable ist = new IntegerSparseTable(s, n, (a, b) -> a | b);
        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;
            if (l == r) {
                yes(out);
                continue;
            }
            if (s[l] != s[r]) {
                yes(out);
                continue;
            }
            if (Integer.bitCount(ist.query(l, r)) > 2) {
                yes(out);
                continue;
            }
            no(out);
        }
    }

    public void yes(FastOutput out) {
        out.println("Yes");
        return;
    }

    public void no(FastOutput out) {
        out.println("No");
        return;
    }
}
