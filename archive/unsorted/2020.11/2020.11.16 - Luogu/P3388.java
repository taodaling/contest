package contest;

import template.graph.CutVertexes;
import template.graph.DfsTree;
import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.List;

public class P3388 {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            Graph.addUndirectedEdge(g, in.readInt() - 1, in.readInt() - 1);
        }

        DfsTree dt = new DfsTree(n);
        dt.consumer = e -> {
            debug.debug("e", e);
        };
        dt.init(g);

        CutVertexes cv = new CutVertexes(n);
        cv.init(dt);

        int cnt = 0;
        for (int i = 0; i < n; i++) {
            if (cv.cut[i]) {
                cnt++;
            }
        }
        out.println(cnt);
        for (int i = 0; i < n; i++) {
            if (cv.cut[i]) {
                out.append(i + 1).append(' ');
            }
        }
    }
}
