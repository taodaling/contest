package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class BReversibleCards {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int limit = 400000;
        int n = in.ri();
        DSUExt dsu = new DSUExt(limit);
        dsu.init();
        for (int i = 0; i < n; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            if (dsu.find(a) != dsu.find(b)) {
                dsu.merge(a, b);
            } else {
                dsu.circle[dsu.find(a)] = true;
            }
        }
        int ans = 0;
        for (int i = 0; i < limit; i++) {
            if (dsu.find(i) != i) {
                continue;
            }
            ans += dsu.sizes[i] - 1;
            if (dsu.circle[i]) {
                ans++;
            }
        }

        out.println(ans);
    }
}

class DSUExt extends DSU {
    boolean[] circle;
    int[] sizes;

    public DSUExt(int n) {
        super(n);
        circle = new boolean[n];
        sizes = new int[n];
    }

    @Override
    public void init(int n) {
        super.init(n);
        Arrays.fill(sizes, 1);
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        sizes[a] += sizes[b];
        circle[a] = circle[a] || circle[b];
    }
}
