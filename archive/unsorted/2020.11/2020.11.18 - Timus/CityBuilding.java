package contest;

import template.datastructure.DSU;
import template.graph.DirectedEdge;
import template.graph.DirectedTarjanSCC;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class CityBuilding {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        long x = in.readLong();
        long y = in.readLong();
        x = Math.min(x, y);

        DSU dsu = new DSU(n);
        dsu.init();
        List<DirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            Graph.addEdge(g, u, v);
            dsu.merge(u, v);
        }
        DirectedTarjanSCC scc = new DirectedTarjanSCC(n);
        scc.init(g);
        int[] sccSize = new int[n];
        int[] dsuSize = new int[n];
        List<Integer>[] cc = Graph.createGraph(n);

        for (int i = 0; i < n; i++) {
            sccSize[scc.set[i]]++;
            dsuSize[dsu.find(i)]++;
            cc[dsu.find(i)].add(i);
        }
        long ans = 0;
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == i) {
                long local = x * dsuSize[i];

                long local2 = 0;
                int sccCnt = 0;
                for (int node : cc[i]) {
                    if (scc.set[node] == node) {
                        sccCnt++;
                        local2 += (sccSize[node] - 1) * y;
                    }
                }
                local2 += (sccCnt - 1) * x;
                ans += Math.min(local, local2);
            }
        }
        out.println(ans);
    }
}


