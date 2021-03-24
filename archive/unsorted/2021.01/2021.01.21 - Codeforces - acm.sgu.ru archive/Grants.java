package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Grants {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        BitSet[] sets = new BitSet[n];
        for (int i = 0; i < n; i++) {
            sets[i] = new BitSet(m);
        }
        for (int i = 0; i < m; i++) {
            int k = in.ri();
            for (int j = 0; j < k; j++) {
                sets[in.ri() - 1].set(i);
            }
        }
        BitSet[] unique = SortUtils.unique(sets, 0, n - 1, Comparator.naturalOrder());
        int ans = unique.length;
        for (BitSet x : unique) {
            if (x.size() == 0) {
                ans--;
                break;
            }
        }
        out.println(ans);
    }
}
