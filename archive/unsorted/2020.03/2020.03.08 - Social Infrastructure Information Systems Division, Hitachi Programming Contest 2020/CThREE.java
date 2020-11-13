package contest;

import template.datastructure.DeltaDSU;
import template.datastructure.XorDeltaDSU;
import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.Arrays;
import java.util.List;

public class CThREE {
    List<UndirectedEdge>[] g;
    XorDeltaDSU dsu;
    int[] depths;
    int[] parent;
    int[] merged;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        g = Graph.createUndirectedGraph(n);
        for (int i = 1; i < n; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            Graph.addUndirectedEdge(g, a, b);
        }
        dsu = new XorDeltaDSU(n);
        depths = new int[n];
        parent = new int[n];
        merged = new int[n];
        Arrays.fill(merged, -1);

        TreeDiameter diameter = new TreeDiameter(g, n);
        int root = diameter.getEnds().get(0);
        dfs(root, -1, 0);

        for (int i = 0; i < n; i++) {
            if (depths[i] <= 1) {
                continue;
            }
            if (depths[i] >= 3) {
                dsu.merge(i, findAncestor(i, depths[i] - 3), 1);
            }
            int ff = findAncestor(i, depths[i] - 2);
            if (merged[ff] == -2 || merged[ff] == parent[i]) {
                for (UndirectedEdge e : g[ff]) {
                    if (e.to == parent[ff] || e.to == merged[ff]) {
                        continue;
                    }
                    dsu.merge(i, e.to, 1);
                    break;
                }
            } else if (merged[ff] == -1) {
                merged[ff] = parent[i];
                for (UndirectedEdge e : g[ff]) {
                    if (e.to == parent[ff] || e.to == merged[ff]) {
                        continue;
                    }
                    dsu.merge(i, e.to, 1);
                }
            } else {
                merged[ff] = -1;
                for (UndirectedEdge e : g[ff]) {
                    if (e.to == parent[ff]) {
                        continue;
                    }
                    dsu.merge(i, e.to, 1);
                }
            }
        }

        int[] colors = new int[n];
        int[] cnt = new int[2];
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == dsu.find(root)) {
                cnt[dsu.delta(i, root)]++;
            }
        }
        IntegerDequeImpl[] dqs = new IntegerDequeImpl[3];
        for(int i = 0; i < 3; i++){
            dqs[i] = new IntegerDequeImpl(n);
        }
        for (int i = 1; i <= n; i++) {
            dqs[i % 3].addLast(i);
        }
        int[] vals = new int[2];
        vals[0] = cnt[0] > cnt[1] ? 1 : 2;
        vals[1] = 3 - vals[0];
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == dsu.find(root)) {
                int v = vals[dsu.delta(i, root)];
                if (dqs[v].isEmpty()) {
                    if (dqs[0].isEmpty()) {
                        out.println(-1);
                        return;
                    }
                    colors[i] = dqs[0].removeFirst();
                } else {
                    colors[i] = dqs[v].removeFirst();
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == dsu.find(root)) {
                continue;
            }
            for(int j = 0; j < 3; j++){
                if(dqs[j].isEmpty()){
                    continue;
                }
                colors[i] = dqs[j].removeFirst();
                break;
            }
        }

        for(int i = 0; i < n; i++){
            out.append(colors[i] ).append(' ');
        }
    }

    public int findAncestor(int root, int d) {
        if (depths[root] == d) {
            return root;
        }
        return findAncestor(parent[root], d);
    }

    public void dfs(int root, int p, int depth) {
        depths[root] = depth;
        parent[root] = p;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root, depth + 1);
        }
    }
}
