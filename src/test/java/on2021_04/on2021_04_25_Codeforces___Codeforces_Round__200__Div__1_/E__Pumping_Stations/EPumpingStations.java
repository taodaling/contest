package on2021_04.on2021_04_25_Codeforces___Codeforces_Round__200__Div__1_.E__Pumping_Stations;



import template.datastructure.DSU;
import template.datastructure.LeftistTree;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.graph.*;

import java.util.Comparator;
import java.util.List;

public class EPumpingStations {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        List<IntegerFlowEdge>[] flow = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int c = in.ri();
            IntegerFlow.addFlowEdge(flow, a, b, c);
            IntegerFlow.addFlowEdge(flow, b, a, c);
        }
        IntegerGomoryHuTree tree = new IntegerGomoryHuTree(flow, new IntegerISAP());
        List<IntegerWeightUndirectedEdge>[] g = tree.tree();


        DSU dsu = new DSU(n);
        dsu.init();
        IntegerWeightUndirectedEdge best = null;
        for (List<IntegerWeightUndirectedEdge> adj : g) {
            for (IntegerWeightUndirectedEdge e : adj) {
                if (best == null || comp.compare(e, best) < 0) {
                    best = e;
                }
            }
        }

        int root = best.to;
        LeftistTree<IntegerWeightUndirectedEdge> pq = asLeftistTree(g[root]);
        IntegerArrayList ans = new IntegerArrayList(n);
        ans.add(root);
        long sum = 0;
        while (!pq.isEmpty()) {
            IntegerWeightUndirectedEdge head = pq.peek();
            pq = LeftistTree.pop(pq, comp);
            if (dsu.find(head.to) == dsu.find(root)) {
                continue;
            }
            dsu.merge(head.to, root);
            sum += head.weight;
            ans.add(head.to);
            pq = LeftistTree.merge(pq, asLeftistTree(g[head.to]), comp);
        }

        out.println(sum);
        for(int x : ans.toArray()){
            out.append(x + 1).append(' ');
        }
    }

    Comparator<IntegerWeightUndirectedEdge> comp = Comparator.comparingInt(x -> -x.weight);

    LeftistTree<IntegerWeightUndirectedEdge> asLeftistTree(List<IntegerWeightUndirectedEdge> adj) {
        LeftistTree<IntegerWeightUndirectedEdge> ans = LeftistTree.NIL;
        for (IntegerWeightUndirectedEdge e : adj) {
            LeftistTree<IntegerWeightUndirectedEdge> newInstance = new LeftistTree<>(e);
            ans = LeftistTree.merge(ans, newInstance, comp);
        }
        return ans;
    }
}

