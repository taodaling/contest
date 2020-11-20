package contest;

import template.datastructure.DSU;
import template.graph.CutVertexes;
import template.graph.DfsTree;
import template.graph.Graph;
import template.graph.KthAncestorOnTreeByBinaryLift;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class ForbiddenCities {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int q = in.readInt();


        DSU dsu = new DSU(n);
        dsu.init();
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        List<UndirectedEdge>[] tree = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            Graph.addUndirectedEdge(g, in.readInt() - 1, in.readInt() - 1);
        }
        DfsTree dt = new DfsTree(n);
        dt.consumer = e -> {
            UndirectedEdge ue = (UndirectedEdge) e;
            tree[ue.rev.to].add(ue);
            tree[ue.to].add(ue.rev);
            dsu.merge(ue.rev.to, ue.to);
        };
        dt.init(g);
        CutVertexes cv = new CutVertexes(n);
        cv.init(dt);
        KthAncestorOnTreeByBinaryLift ka = new KthAncestorOnTreeByBinaryLift(n);
        ka.init(i -> dt.parents[i], n);

        String yes = "YES";
        String no = "NO";
        for (int i = 0; i < q; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            int c = in.readInt() - 1;
            if (dsu.find(a) != dsu.find(b) || a == c || b == c) {
                out.println(no);
                continue;
            }
            if (dsu.find(c) != dsu.find(a) || !cv.cut[c]) {
                out.println(yes);
                continue;
            }
            if(dt.depth[a] - 1 > dt.depth[c]){
                a = ka.kthAncestor(a, dt.depth[a] - (dt.depth[c] + 1));
            }
            if(dt.depth[b] - 1 > dt.depth[c]){
                b = ka.kthAncestor(b, dt.depth[b] - (dt.depth[c] + 1));
            }
            if(a != b && (dt.parents[a] == c && cv.beCut[a] || dt.parents[b] == c && cv.beCut[b])){
                out.println(no);
            }else{
                out.println(yes);
            }
        }
    }
}

