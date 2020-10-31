package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

public class Unionfind {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        DSU dsu = new DSU(n);
        dsu.init(n);
        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            int u = in.readInt();
            int v = in.readInt();
            if (t == 0) {
                dsu.merge(u, v);
            } else {
                out.println(dsu.find(u) == dsu.find(v) ? 1 : 0);
            }
        }
    }
}
