package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class RoadConstruction {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        DSUExt dsu = new DSUExt(n);
        dsu.init();

        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            dsu.merge(a, b);
            out.append(dsu.cc).append(' ').println(dsu.max);
        }
    }
}

class DSUExt extends DSU {
    int[] size;
    int cc;
    int max;

    public DSUExt(int n) {
        super(n);
        size = new int[n];
    }

    @Override
    public void init(int n) {
        super.init(n);
        cc = n;
        max = 1;
        Arrays.fill(size, 1);
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        cc--;
        size[a] += size[b];
        max = Math.max(max, size[a]);
    }
}