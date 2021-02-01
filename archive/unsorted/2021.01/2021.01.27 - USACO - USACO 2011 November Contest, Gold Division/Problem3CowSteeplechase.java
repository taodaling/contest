package contest;

import template.graph.DinicBipartiteMatch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class Problem3CowSteeplechase {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        List<HLine> L = new ArrayList<>(n);
        List<VLine> R = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int x1 = in.ri();
            int y1 = in.ri();
            int x2 = in.ri();
            int y2 = in.ri();
            if (x1 == x2) {
                VLine v = new VLine();
                v.x = x1;
                v.b = Math.min(y1, y2);
                v.t = Math.max(y1, y2);
                R.add(v);
            } else {
                HLine v = new HLine();
                v.y = y1;
                v.l = Math.min(x1, x2);
                v.r = Math.max(x1, x2);
                L.add(v);
            }
        }
        DinicBipartiteMatch bm = new DinicBipartiteMatch(L.size(), R.size());
        for (int i = 0; i < L.size(); i++) {
            for (int j = 0; j < R.size(); j++) {
                HLine a = L.get(i);
                VLine b = R.get(j);
                if (a.y >= b.b && a.y <= b.t && a.l <= b.x && a.r >= b.x) {
                    bm.addEdge(i, j);
                }
            }
        }
        int ans = bm.solve();
        out.println(n - ans);
    }
}

class HLine {
    int y;
    int l;
    int r;
}

class VLine {
    int x;
    int b;
    int t;
}
