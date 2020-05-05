package contest;

import template.datastructure.DSU;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.binary.Log2;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.utils.SequenceUtils;

import java.util.List;

public class FCowAndVacation {
    int logn = 20;
    int[][] jump;
    List<UndirectedEdge>[] g;
    DSU dsu;
    int[] dist;
    LcaOnTree lca;
    int[] depths;

    public void dfsForJump(int root, int p, int depth) {
        depths[root] = depth;
        jump[root][0] = p;
        for (int i = 0; jump[root][i] != -1; i++) {
            jump[root][i + 1] = jump[jump[root][i]][i];
        }
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForJump(e.to, root, depth + 1);
        }
    }

    public int climb(int node, int d) {
        if (depths[node] == d) {
            return node;
        }
        int delta = depths[node] - d;
        int log2 = Log2.floorLog(delta);
        return climb(jump[node][log2], d);
    }

    public void no(FastOutput out) {
        out.println("NO");
    }

    public void yes(FastOutput out) {
        out.println("YES");
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int r = in.readInt();
        dsu = new DSU(2 * n - 1);
        g = Graph.createUndirectedGraph(2 * n - 1);
        jump = new int[2 * n - 1][logn];
        depths = new int[2 * n - 1];
        SequenceUtils.deepFill(jump, -1);
        for (int i = 0; i < n - 1; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            Graph.addUndirectedEdge(g, u, n + i);
            Graph.addUndirectedEdge(g, v, n + i);
        }
        dfsForJump(0, -1, 0);
        lca = new LcaOnTree(g, 0);
        dist = new int[2 * n - 1];
        SequenceUtils.deepFill(dist, (int) 1e9);
        IntegerDeque deque = new IntegerDequeImpl(2 * n - 1);
        for (int i = 0; i < r; i++) {
            int v = in.readInt() - 1;
            deque.addLast(v);
            dist[v] = 0;
        }
        while (!deque.isEmpty()) {
            int front = deque.removeFirst();
            for (UndirectedEdge e : g[front]) {
                if (dist[front] < k) {
                    dsu.merge(front, e.to);
                }
                if (dist[front] + 1 < dist[e.to]) {
                    dist[e.to] = dist[front] + 1;
                    deque.addLast(e.to);
                }
            }
        }

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            int c = lca.lca(a, b);
            if (depths[a] + depths[b] - 2 * depths[c] <= 2 * k) {
                yes(out);
                continue;
            }
            int da = depths[a] - depths[c];
            int db = depths[b] - depths[c];
            int climA;
            if (da >= k) {
                climA = climb(a, depths[a] - k);
            } else {
                climA = climb(b, depths[c] + (k - da));
            }
            int climB;
            if (db >= k) {
                climB = climb(b, depths[b] - k);
            } else {
                climB = climb(a, depths[c] + (k - db));
            }

            if (dsu.find(climA) == dsu.find(climB)) {
                yes(out);
            } else {
                no(out);
            }
        }
    }
}

