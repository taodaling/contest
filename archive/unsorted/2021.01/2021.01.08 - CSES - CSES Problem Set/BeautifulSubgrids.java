package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.BitSet;

public class BeautifulSubgrids {
    public int choose2(int n) {
        return n * (n - 1) / 2;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        BitSet[] rows = new BitSet[n];
        for (int i = 0; i < n; i++) {
            rows[i] = new BitSet(n);
            for (int j = 0; j < n; j++) {
                int x = in.rc() - '0';
                if (x == 1) {
                    rows[i].set(j);
                }
            }
        }
        long ans = 0;
        BitSet copy = new BitSet(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                copy.clear();
                copy.or(rows[i]);
                copy.and(rows[j]);
                ans += choose2(copy.cardinality());
            }
        }

        out.println(ans);
    }
}
