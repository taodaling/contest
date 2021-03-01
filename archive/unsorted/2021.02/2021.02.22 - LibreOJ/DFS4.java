package contest;

import template.datastructure.SegTree;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CloneSupportObject;
import template.utils.Sum;
import template.utils.Update;

import java.util.List;

public class DFS4 {
    List<UndirectedEdge>[] g;
    int order;
    int[] depth;
    int[] open;
    int[] close;
    int[] fa;
    long[] sumOfAncestor;
    int[] v;

    public void dfs(int root, int p) {
        fa[root] = p;
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        open[root] = ++order;
        sumOfAncestor[root] = v[root];
        if (p != -1) {
            sumOfAncestor[root] += sumOfAncestor[p];
        }
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root);
        }
        close[root] = order;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int root = in.ri() - 1;
        v = in.ri(n);
        depth = new int[n];
        open = new int[n];
        close = new int[n];
        fa = new int[n];
        sumOfAncestor = new long[n];
        g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            Graph.addUndirectedEdge(g, in.ri() - 1, in.ri() - 1);
        }
        LcaOnTree lca = new LcaOnTree(g, i -> i == root);
        dfs(root, -1);
        int[] inv = new int[n + 1];
        for (int i = 0; i < n; i++) {
            inv[open[i]] = i;
        }
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(1, n, SumImpl::new, UpdateImpl::new,
                i -> {
                    SumImpl s = new SumImpl();
                    s.depth = depth[inv[i]];
                    s.total = sumOfAncestor[inv[i]];
                    return s;
                }
        );
        UpdateImpl upd = new UpdateImpl();
        SumImpl sum = new SumImpl();
        for (int i = 0; i < m; i++) {
            int t = in.ri();
            int a = in.ri() - 1;
            if (t == 1) {
                int x = in.ri();
                upd.clear();
                upd.b = x;
                st.update(open[a], close[a], 1, n, upd);
            } else if (t == 2) {
                int x = in.ri();
                upd.clear();
                upd.a = x;
                upd.b = (long) (1 - depth[a]) * x;
                st.update(open[a], close[a], 1, n, upd);
            } else {
                int b = in.ri() - 1;
                int c = lca.lca(a, b);
                long ans = 0;
                sum.clear();
                st.query(open[a], open[a], 1, n, sum);
                ans += sum.total;
                sum.clear();
                st.query(open[b], open[b], 1, n, sum);
                ans += sum.total;
                sum.clear();
                st.query(open[c], open[c], 1, n, sum);
                ans -= sum.total;
                if (fa[c] != -1) {
                    sum.clear();
                    st.query(open[fa[c]], open[fa[c]], 1, n, sum);
                    ans -= sum.total;
                }
                out.println(ans);
            }
        }
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    long a;
    long b;

    @Override
    public void update(UpdateImpl update) {
        a += update.a;
        b += update.b;
    }

    @Override
    public void clear() {
        a = b = 0;
    }

    @Override
    public boolean ofBoolean() {
        return a != 0 || b != 0;
    }

}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    long total;
    long depth;

    @Override
    public void add(SumImpl sum) {
        total += sum.total;
    }

    void clear() {
        total = 0;
    }

    @Override
    public void update(UpdateImpl update) {
        total += depth * update.a + update.b;
    }

    @Override
    public void copy(SumImpl sum) {
        total = sum.total;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }
}