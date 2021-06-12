package on2021_05.on2021_05_22_Library_Checker.Lowest_Common_Ancestor;



import template.datastructure.RMQBySegment;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerSparseTable;

public class LowestCommonAncestor {
    int[] dfns;
    int[] open;
    int[] close;
    int[][] g;
    int now = 0;

    public void dfs(int root, IntegerArrayList eulerTour){
        int dfn = now++;
        dfns[root] = dfn;
        open[root] = eulerTour.size();
        eulerTour.add(root);
        for(int node : g[root]){
            dfs(node, eulerTour);
            eulerTour.add(root);
        }
        close[root] = eulerTour.size() - 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        IntegerArrayList us = new IntegerArrayList(n - 1);
        IntegerArrayList vs = new IntegerArrayList(n - 1);
        for (int i = 1; i < n; i++) {
            int p = in.ri();
            us.add(p);
            vs.add(i);
        }
        g = Graph.createGraph(n, us.toArray(), vs.toArray());
        dfns = new int[n];
        open = new int[n];
        close = new int[n];
        IntegerArrayList eulerTour = new IntegerArrayList(n * 2);
        dfs(0, eulerTour);
        int[] et = eulerTour.toArray();
        RMQBySegment rmq = new RMQBySegment(et.length, (i, j) -> Integer.compare(dfns[et[i]], dfns[et[j]]));

        for (int i = 0; i < q; i++) {
            int u = in.ri();
            int v = in.ri();
            int lca = et[rmq.query(Math.min(open[u], open[v]), Math.max(close[u], close[v]))];
            out.println(lca);
        }
    }
}
