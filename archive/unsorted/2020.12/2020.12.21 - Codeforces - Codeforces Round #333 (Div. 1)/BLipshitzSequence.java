package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerSparseTable;

public class BLipshitzSequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = new int[n];
        in.populate(a);
        delta = new int[n - 1];
        for (int i = 0; i < n - 1; i++) {
            delta[i] = Math.abs(a[i + 1] - a[i]);
        }
        st = new IntegerSparseTable(i -> i, n - 1, (x, y) -> delta[x] >= delta[y] ? x : y);
        for (int i = 0; i < q; i++) {
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            long ans = solve(l, r);
            out.println(ans);
        }
    }

    int[] delta;
    IntegerSparseTable st;

    public long solve(int l, int r) {
        if (l == r) {
            return 0;
        }
        int index = st.query(l, r - 1);
        long left = index - l + 1;
        long right = r - (index + 1) + 1;
        long ans = left * right * delta[index];
        ans += solve(l, index);
        ans += solve(index + 1, r);
        return ans;
    }
}
