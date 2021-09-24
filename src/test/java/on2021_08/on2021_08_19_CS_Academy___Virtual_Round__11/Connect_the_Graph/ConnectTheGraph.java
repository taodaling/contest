package on2021_08.on2021_08_19_CS_Academy___Virtual_Round__11.Connect_the_Graph;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ConnectTheGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        DSU dsu = new DSU(n);
        dsu.init();
        List<int[]> dump = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            if (dsu.find(a) == dsu.find(b)) {
                dump.add(new int[]{a, b});
            } else {
                dsu.merge(a, b);
            }
        }
        if (m < n - 1) {
            out.println(-1);
            return;
        }
        int cc = 0;
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == i) {
                cc++;
            }
        }
        out.println(cc - 1);
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) != dsu.find(0)) {
                int[] back = CollectionUtils.pop(dump);
                out.append(back[0] + 1).append(' ').append(back[1] + 1).append(' ');
                out.append(1).append(' ').append(i + 1).println();
                dsu.merge(0, i);
            }
        }
    }
}
