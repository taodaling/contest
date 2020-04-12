package contest;

import template.datastructure.DSU;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class PaintItBlack {
    List<UndirectedEdge>[] tree;
    Deque<Integer>[] outgoing;
    int[] depths;
    int[] sizes;
    int[] parents;

    public int[] findWalk(int n, int u, int[] a, int[] b) {
        int m = a.length;
        depths = new int[n];
        parents = new int[n];
        outgoing = new Deque[n];
        sizes = new int[n];
        for (int i = 0; i < n; i++) {
            outgoing[i] = new LinkedList<>();
        }
        tree = Graph.createUndirectedGraph(n);
        DSU dsu = new DSU(n);
        for (int i = 0; i < m; i++) {
            if (dsu.find(a[i]) != dsu.find(b[i])) {
                Graph.addUndirectedEdge(tree, a[i], b[i]);
                dsu.merge(a[i], b[i]);
            }
        }

        LcaOnTree lot = new LcaOnTree(tree, u);
        dfs(u, -1, 0);
        if (n % 2 == 1) {
            //need odd circle
            List<Integer> circle = null;
            for (int i = 0; i < m; i++) {
                if (depths[a[i]] % 2 == depths[b[i] % 2]) {
                    int lca = lot.lca(a[i], b[i]);
                    circle = new ArrayList<>();
                    up(a[i], lca, circle);
                    SequenceUtils.reverse(circle);
                    up(b[i], lca, circle);
                    circle.remove(circle.size() - 1);
                    break;
                }
            }

            if (circle == null) {
                return new int[0];
            }

            for (int i = 0; i < circle.size(); i++) {
                int cur = circle.get(i);
                int next = circle.get((i + 1) % circle.size());
                outgoing[cur].addLast(next);
            }
        }

        List<Integer> ans = new ArrayList<>(n * 5);
        eulerTrace(u, ans);
        return ans.stream().mapToInt(Integer::intValue).toArray();
    }

    public void eulerTrace(int root, List<Integer> trace) {
        while (!outgoing[root].isEmpty()) {
            eulerTrace(outgoing[root].removeFirst(), trace);
        }
        trace.add(root);
    }

    public void up(int root, int target, List<Integer> trace) {
        trace.add(root);
        if (root != target) {
            up(parents[root], target, trace);
        }
    }

    public void dfs(int root, int p, int d) {
        depths[root] = d;
        sizes[root] = 1;
        parents[root] = p;
        for (UndirectedEdge e : tree[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root, d + 1);
            sizes[root] += sizes[e.to];
        }

        if (p != -1) {
            int withP = sizes[root] % 2;
            if (withP == 0) {
                withP = 2;
            }
            for (int i = 0; i < withP; i++) {
                outgoing[root].addLast(p);
                outgoing[p].addLast(root);
            }
        }
    }
}
