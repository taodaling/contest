package on2020_03.on2020_03_23_Educational_Codeforces_Round_84__Rated_for_Div__2_.F__AND_Segments;



import template.binary.Bits;
import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

import java.util.Arrays;

public class FANDSegments {
    Modular mod = new Modular(998244353);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int m = in.readInt();

        dsu = new DSU(n + 1);
        dp = new int[n + 1];
        Constraint[] constraints = new Constraint[m];
        for (int i = 0; i < m; i++) {
            constraints[i] = new Constraint();
            constraints[i].l = in.readInt();
            constraints[i].r = in.readInt();
            constraints[i].x = in.readInt();
        }
        Arrays.sort(constraints, (a, b) -> a.r - b.r);

        int prod = 1;
        for(int i = 0; i < k; i++){
            int ret = solve(n, constraints, i);
            prod = mod.mul(prod, ret);
        }

        out.println(prod);
    }

    DSU dsu;
    int[] dp;
    int sum;

    public void clearZero(int l, int r) {
        if (l > r) {
            return;
        }
        for (int i = l; i <= r; i++) {
            if (dp[i] == 0) {
                if (i > l) {
                    dsu.merge(i, i - 1);
                }
                i = dsu.max[dsu.find(i)];
            } else {
                sum = mod.subtract(sum, dp[i]);
                dp[i] = 0;
            }
        }
    }

    public int solve(int n, Constraint[] constraints, int bit) {
        dsu.reset();
        dp[0] = 1;
        sum = 1;
        SimplifiedDeque<Constraint> dq = new Range2DequeAdapter<>(i -> constraints[i], 0, constraints.length - 1);
        for (int i = 1; i <= n; i++) {
            dp[i] = sum;
            sum = mod.plus(sum, dp[i]);
            while (!dq.isEmpty() && dq.peekFirst().r == i) {
                Constraint c = dq.removeFirst();
                if (Bits.bitAt(c.x, bit) == 0) {
                    clearZero(0, c.l - 1);
                } else {
                    clearZero(c.l, c.r);
                }
            }
        }

        return sum;
    }
}

class Constraint {
    int l;
    int r;
    int x;
}

class DSU {
    protected int[] p;
    protected int[] rank;
    protected int[] max;

    public DSU(int n) {
        p = new int[n];
        rank = new int[n];
        max = new int[n];
        reset();
    }

    public final void reset() {
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
            rank[i] = 0;
            max[i] = i;
        }
    }

    public final int find(int a) {
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        int ans = find(p[a]);
        refreshParent(a, p[a], ans);
        return ans;
    }

    protected void refreshParent(int a, int old, int refresh) {
        p[a] = refresh;
    }

    /**
     * link a into subtree of b
     */
    protected void link(int a, int b) {
        p[a] = b;
        max[b] = Math.max(max[a], max[b]);
    }

    public final void merge(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (rank[a] == rank[b]) {
            rank[a]++;
        }
        if (rank[a] > rank[b]) {
            link(b, a);
        } else {
            link(a, b);
        }
    }
}
