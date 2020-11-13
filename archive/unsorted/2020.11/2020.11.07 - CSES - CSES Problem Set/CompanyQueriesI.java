package contest;

import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.KthAncestorOnTree;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class CompanyQueriesI {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        List<DirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 1; i < n; i++) {
            Graph.addEdge(g, in.readInt() - 1, i);
        }
        KthAncestorOnTree kthAncestorOnTree = new KthAncestorOnTree(g, 0);
        for (int i = 0; i < q; i++) {
            int u = in.readInt() - 1;
            int k = in.readInt();
            if(kthAncestorOnTree.getDepth(u) - k < 0){
                out.println(-1);
            }else{
                out.println(1 + kthAncestorOnTree.kthAncestor(u, k));
            }
        }
    }
}
