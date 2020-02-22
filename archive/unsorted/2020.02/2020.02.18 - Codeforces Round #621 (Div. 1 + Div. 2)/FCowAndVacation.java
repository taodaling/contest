package contest;

import template.datastructure.DSU;
import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.List;

public class FCowAndVacation {
    int[][] jump;
    int[] heal;
    int[] dist;
    int LIMIT = 20;
    int[] depth;
    DSU dsu;
    List<UndirectedEdge>[] g;
    LcaOnTree lcaOnTree;
    int k;
    int[] top;
    int[] from;


    public void dfs(int root, int p, int d) {
        depth[root] = d;
        top[root] = heal[root] == 0 ? root : p == -1 ? -1 : top[p];
        jump[0][root] = p;
        for (int i = 0; jump[i][root] != -1; i++) {
            jump[i + 1][root] = jump[i][jump[i][root]];
        }
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root, d + 1);
        }
    }

    public int distance(int a, int b) {
        int lca = lcaOnTree.lca(a, b);
        return depth[a] + depth[b] - 2 * depth[lca];
    }

    public void yes(FastOutput out) {
        out.println("YES");
    }

    public void no(FastOutput out) {
        out.println("NO");
    }

    private boolean check(int i, int t) {
        return t <= Math.max(heal[i], dist[i]);
    }

    static <T extends DirectedEdge> void multiSourceBfs(List<T>[] g, IntegerList sources, int[] dist, int[] from, int inf, IntegerDeque deque) {
        Arrays.fill(dist, inf);
        deque.clear();
        for (int i = 0; i < sources.size(); i++) {
            int v = sources.get(i);
            dist[v] = 0;
            from[v] = v;
            deque.addLast(v);
        }
        while (!deque.isEmpty()) {
            int head = deque.removeFirst();
            for (T e : g[head]) {
                if (dist[e.to] == inf) {
                    dist[e.to] = dist[head] + 1;
                    deque.addLast(e.to);
                    from[e.to] = from[head];
                }
            }
        }
    }


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        k = in.readInt();
        int r = in.readInt();
        g = Graph.createUndirectedGraph(n);
        jump = new int[LIMIT][n];
        SequenceUtils.deepFill(jump, -1);
        dist = new int[n];
        top = new int[n];
        dsu = new DSU(n);
        heal = new int[n];
        depth = new int[n];
        from = new int[n];
        IntegerDequeImpl deque = new IntegerDequeImpl(n);
        for (int i = 1; i < n; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            Graph.addUndirectedEdge(g, u, v);
        }
        int[] specialVertex = new int[r];
        for (int i = 0; i < r; i++) {
            specialVertex[i] = in.readInt() - 1;
        }
        IntegerList sources = new IntegerList();
        sources.addAll(specialVertex);

        multiSourceBfs(g, sources, dist, from, (int) 1e9, deque);
        for (int i = 0; i < n; i++) {
            heal[i] = Math.max(0, k - dist[i]);
        }
        lcaOnTree = new LcaOnTree(g, 0);
        for (int i = 0; i < n; i++) {
            for (UndirectedEdge e : g[i]) {
                if (dist[i] + dist[e.to] + 1 <= k) {
                    dsu.merge(i, e.to);
                }
            }
        }
        dfs(0, -1, 0);

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            int c = lcaOnTree.lca(a, b);
            if (distance(a, b) <= k) {
                out.println("YES");
                continue;
            }
            int begin;
            //left
            if (check(c, k - distance(a, c))) {
                begin = a;
                for (int j = LIMIT - 1; j >= 0; j--) {
                    if (depth[begin] - (1 << j) < depth[c]) {
                        continue;
                    }
                    int next = jump[j][begin];
                    if (!check(c, k - distance(a, next))) {
                        begin = next;
                    }
                }
                begin = jump[0][begin];
            } else {
                begin = b;
                for (int j = LIMIT - 1; j >= 0; j--) {
                    if (depth[begin] - (1 << j) < depth[c]) {
                        continue;
                    }
                    int next = jump[j][begin];
                    if (check(c, k - distance(a, next))) {
                        begin = next;
                    }
                }
            }

            begin = from[begin];
            c = lcaOnTree.lca(begin, b);
            int end;
            if (dsu.find(begin) == dsu.find(c)) {
                end = begin;
                for (int j = LIMIT - 1; j >= 0; j--) {
                    if (depth[end] - (1 << j) < depth[c]) {
                        continue;
                    }
                    int next = jump[j][end];
                    if (dsu.find(next) != dsu.find(begin)) {
                        end = next;
                    }
                }
                if (dsu.find(end) != dsu.find(begin)) {
                    end = jump[0][end];
                }
            } else {
                end = begin;
                for (int j = LIMIT - 1; j >= 0; j--) {
                    if (depth[end] - (1 << j) < depth[c]) {
                        continue;
                    }
                    int next = jump[j][end];
                    if (dsu.find(next) == dsu.find(begin)) {
                        end = next;
                    }
                }
            }

            if(heal[end] >= distance(end, b)){
                yes(out);
            }else{
                no(out);
            }
        }
    }
}
