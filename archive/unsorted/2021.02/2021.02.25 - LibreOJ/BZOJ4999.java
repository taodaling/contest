package contest;

import graphs.lca.Lca;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.IntegerBITExt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BZOJ4999 {
    List<UndirectedEdge>[] g;
    int[] fa;
    int[] open;
    int[] close;
    int order;

    public void dfs(int root, int p) {
        fa[root] = p;
        open[root] = ++order;
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
        int q = in.ri();
        g = Graph.createGraph(n);
        int[] init = new int[n];
        fa = new int[n];
        open = new int[n];
        close = new int[n];
        List<Op> ops = new ArrayList<>(n + 2 * q);
        for (int i = 0; i < n; i++) {
            init[i] = in.ri();
            ops.add(new Op('A', i, init[i]));
        }
        for (int i = 0; i < n - 1; i++) {
            Graph.addUndirectedEdge(g, in.ri() - 1, in.ri() - 1);
        }
        dfs(0, -1);
        LcaOnTree lca = new LcaOnTree(g, 0);
        for (int i = 0; i < q; i++) {
            int t = in.rc();
            if (t == 'C') {
                int a = in.ri() - 1;
                int x = in.ri();
                ops.add(new Op('A', a, x));
                ops.add(new Op('S', a, init[a]));
                init[a] = x;
            } else {
                int a = in.ri() - 1;
                int b = in.ri() - 1;
                int x = in.ri();
                ops.add(new Query('Q', a, x, b));
            }
        }
        Map<Integer, List<Op>> groupByX = ops.stream().collect(Collectors.groupingBy(x -> x.x));
        IntegerBITExt bit = new IntegerBITExt(n);
        for (List<Op> list : groupByX.values()) {
            for (Op op : list) {
                if (op.type == 'A') {
                    bit.update(open[op.a], close[op.a], 1);
                } else if (op.type == 'S') {
                    bit.update(open[op.a], close[op.a], -1);
                } else {
                    Query query = (Query) op;
                    int c = lca.lca(query.a, query.b);
                    int sum = bit.query(open[query.a], open[query.a]) + bit.query(open[query.b], open[query.b]) - bit.query(open[c], open[c]);
                    if (fa[c] != -1) {
                        sum -= bit.query(open[fa[c]], open[fa[c]]);
                    }
                    query.ans = sum;
                }
            }
            for (Op op : list) {
                if (op.type == 'A') {
                    bit.update(open[op.a], close[op.a], -1);
                } else if (op.type == 'S') {
                    bit.update(open[op.a], close[op.a], 1);
                } else {
                }
            }
        }
        for (Op op : ops) {
            if (op.type == 'Q') {
                Query query = (Query) op;
                out.println(query.ans);
            }
        }
    }
}

class Op {
    int type;
    int a;
    int x;

    public Op(int type, int a, int x) {
        this.type = type;
        this.a = a;
        this.x = x;
    }
}

class Query extends Op {
    int b;
    int ans;

    public Query(int type, int a, int x, int b) {
        super(type, a, x);
        this.b = b;
    }
}