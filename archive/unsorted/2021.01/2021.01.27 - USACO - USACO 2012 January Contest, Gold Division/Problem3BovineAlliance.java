package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class Problem3BovineAlliance {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        DSUExt dsu = new DSUExt(n);
        dsu.init(n);
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            if (dsu.find(a) == dsu.find(b)) {
                if (dsu.circle[dsu.find(a)]) {
                    out.println(0);
                    return;
                }
                dsu.circle[dsu.find(a)] = true;
            } else {
                dsu.merge(a, b);
            }
        }

        long ans = 1;
        int mod = (int) 1e9 + 7;
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) != i) {
                continue;
            }
            int contrib = 0;
            if (dsu.circle[i]) {
                contrib = 2;
            }else{
                contrib = dsu.size[i];
            }
            ans = ans * contrib % mod;
        }
        out.println(ans);
    }
}

class DSUExt extends DSU {
    int[] size;
    boolean[] circle;


    public DSUExt(int n) {
        super(n);
        size = new int[n];
        circle = new boolean[n];
    }

    @Override
    public void init(int n) {
        super.init(n);
        Arrays.fill(size, 1);
        Arrays.fill(circle, false);
    }


    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        size[a] += size[b];
        circle[a] = circle[a] || circle[b];
    }
}
