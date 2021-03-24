package on2021_03.on2021_03_24_Codeforces___Codeforces_Round__238__Div__1_.D__Hill_Climbing;



import template.geometry.geo2.IntegerPoint2;
import template.graph.KthAncestorOnTreeByBinaryLift;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.Deque;

public class DHillClimbing {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Pt[] pts = new Pt[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Pt(in.rl(), in.rl(), i);
        }
        int[] p = new int[n];
        int[] depth = new int[n];
        Deque<Pt> convex = new ArrayDeque<>(n);
        for (int i = n - 1; i >= 0; i--) {
            Pt pt = pts[i];
            while (convex.size() >= 2) {
                Pt first = convex.removeFirst();
                Pt second = convex.peekFirst();
                if (IntegerPoint2.orient(pt, second, first) < 0) {
                    continue;
                }
                convex.addFirst(first);
                break;
            }
            p[i] = convex.isEmpty() ? -1 : convex.peekFirst().id;
            depth[i] = p[i] == -1 ? 0 : depth[p[i]] + 1;
            convex.addFirst(pt);
        }
        KthAncestorOnTreeByBinaryLift bl = new KthAncestorOnTreeByBinaryLift(n);
        bl.init(i -> p[i], n);
        int m = in.ri();
        for(int i = 0; i < m; i++){
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int lca = bl.lca(a, depth[a], b, depth[b]);
            out.println(lca + 1);
        }
    }
}

class Pt extends IntegerPoint2 {
    int id;

    public Pt(long x, long y, int id) {
        super(x, y);
        this.id = id;
    }
}
