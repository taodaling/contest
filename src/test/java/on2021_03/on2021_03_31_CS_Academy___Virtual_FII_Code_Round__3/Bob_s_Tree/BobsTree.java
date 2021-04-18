package on2021_03.on2021_03_31_CS_Academy___Virtual_FII_Code_Round__3.Bob_s_Tree;



import template.datastructure.SegTree;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.CloneSupportObject;
import template.utils.Sum;
import template.utils.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BobsTree {
    int[] open;
    int[][] g;
    int[] depth;

    void dfs(int root, int p, IntegerArrayList list) {
        open[root] = list.size();
        list.add(root);
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        for (int node : g[root]) {
            if (node == p) {
                continue;
            }
            dfs(node, root, list);
            list.add(root);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] colors = in.ri(n);
        int[] u = new int[n - 1];
        int[] v = new int[n - 1];
        depth = new int[n];
        open = new int[n];
        for (int i = 0; i < n - 1; i++) {
            u[i] = in.ri() - 1;
            v[i] = in.ri() - 1;
        }
        g = Graph.createUndirectedGraph(n, n - 1, u, v);
        IntegerArrayList seq = new IntegerArrayList(n * 2);
        dfs(0, -1, seq);
        int m = seq.size();
        UpdateImpl upd = new UpdateImpl();
        upd.enable = -1;
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(0, m - 1,
                SumImpl::new, UpdateImpl::new,
                i -> {
                    SumImpl ans = new SumImpl();
                    ans.depth = depth[seq.get(i)];
                    ans.update(upd);
                    return ans;
                });
        int q = in.ri();
        List<Query> qList = new ArrayList<>(n + 2 * q);
        for (int i = 0; i < n; i++) {
            qList.add(new UpdateType(1, i, colors[i]));
        }
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            if (t == 1) {
                int node = in.ri() - 1;
                int color = in.ri();
                qList.add(new UpdateType(-1, node, colors[node]));
                colors[node] = color;
                qList.add(new UpdateType(1, node, colors[node]));
            } else {
                int color = in.ri();
                qList.add(new QueryType(color));
            }
        }

        Map<Integer, List<Query>> groupBy = qList.stream().collect(Collectors.groupingBy(x -> x.c));
        for (List<Query> list : groupBy.values()) {
            for (Query query : list) {
                if (query instanceof UpdateType) {
                    UpdateType ut = (UpdateType) query;
                    upd.enable = ut.op;
                    st.update(open[ut.v], open[ut.v], 0, m - 1, upd);
                } else {
                    QueryType qt = (QueryType) query;
                    qt.ans = st.sum.abc;
                }
            }
            for (Query query : list) {
                if (query instanceof UpdateType) {
                    UpdateType ut = (UpdateType) query;
                    if (ut.op == -1) {
                        continue;
                    }
                    upd.enable = -1;
                    st.update(open[ut.v], open[ut.v], 0, m - 1, upd);
                } else {
                }
            }
        }

        for (Query query : qList) {
            if (query instanceof QueryType) {
                QueryType qt = (QueryType) query;
                out.println(qt.ans);
            }
        }
    }
}

class Query {
    int c;
}

class UpdateType extends Query {
    int op;
    int v;

    public UpdateType(int op, int v, int c) {
        this.op = op;
        this.v = v;
        this.c = c;
    }
}

class QueryType extends Query {
    public QueryType(int c) {
        this.c = c;
    }

    int ans;
}


class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    int enable;

    @Override
    public void update(UpdateImpl update) {
        if (update.enable != 0) {
            enable = update.enable;
        }
    }

    @Override
    public void clear() {
        enable = 0;
    }

    @Override
    public boolean ofBoolean() {
        return enable != 0;
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    int depth;
    int a;
    int b;
    int c;
    int ab;
    int bc;
    int abc;
    static int inf = (int) 1e8;

    @Override
    public void add(SumImpl sum) {
        abc = Math.max(abc, sum.abc);
        abc = Math.max(abc, ab + sum.c);
        abc = Math.max(abc, a + sum.bc);
        ab = Math.max(ab, sum.ab);
        ab = Math.max(ab, a + sum.b);
        bc = Math.max(bc, sum.bc);
        bc = Math.max(bc, b + sum.c);
        a = Math.max(a, sum.a);
        b = Math.max(b, sum.b);
        c = Math.max(c, sum.c);
    }

    @Override
    public void update(UpdateImpl update) {
        if (update.enable == -1) {
            a = c = ab = bc = abc = -inf;
        } else {
            a = depth;
            c = depth;
            ab = bc = -depth;
            abc = 0;
        }
        b = -2 * depth;
    }

    @Override
    public void copy(SumImpl sum) {
        depth = sum.depth;
        a = sum.a;
        b = sum.b;
        c = sum.c;
        ab = sum.ab;
        bc = sum.bc;
        abc = sum.abc;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public String toString() {
        return "" + depth;
    }
}
