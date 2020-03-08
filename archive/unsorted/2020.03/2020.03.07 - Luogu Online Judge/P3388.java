package contest;

import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;

import java.util.List;

public class P3388 {
    List<UndirectedEdge>[] g;
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        g = Graph.createUndirectedGraph(n);
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            Graph.addUndirectedEdge(g, a, b);
        }

        //debug.debug("g[2]", g[2]);

        low = new int[n];
        dfn = new int[n];
        instk = new boolean[n];
        isCutVertex = new boolean[n];
        dq = new IntegerDequeImpl(n);
        subtree = new int[n];

        for (int i = 0; i < n; i++) {
            if (dfn[i] != 0) {
                continue;
            }
            //debug.debug("i", i);
            tarjan(i, -1);
            isCutVertex[i] = subtree[i] > 1;
        }

        IntegerList ans = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            if (isCutVertex[i]) {
                ans.add(i);
            }
        }

        out.println(ans.size());
        for (int i = 0; i < ans.size(); i++) {
            out.append(ans.get(i) + 1).append(' ');
        }


    }

    IntegerDequeImpl dq;
    boolean[] instk;
    int[] low;
    int[] dfn;
    boolean[] isCutVertex;
    int order = 0;
    int[] subtree;

    public int tarjan(int root, int p) {
        if (dfn[root] != 0) {
            return 0;
        }
        dfn[root] = low[root] = ++order;
        instk[root] = true;
        dq.addLast(root);
        //debug.debug("dq.size()", dq.size());

        for (UndirectedEdge e : g[root]) {
            if (e.to == p || e.to == root) {
                continue;
            }
            boolean consider = dfn[e.to] == 0;
            subtree[root] += tarjan(e.to, root);
            if(consider) {
                isCutVertex[root] = isCutVertex[root] || low[e.to] >= dfn[root];
            }
            if (instk[e.to]) {
                low[root] = Math.min(low[root], low[e.to]);
            }
        }

        if (low[root] == dfn[root]) {
            while (true) {
                int last = dq.removeLast();
                instk[last] = false;
                if (last == root) {
                    break;
                }
            }
        }
        return 1;
    }
}
