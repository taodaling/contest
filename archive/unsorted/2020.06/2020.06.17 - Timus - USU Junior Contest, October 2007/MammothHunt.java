package contest;

import template.geometry.geo2.IntegerPoint2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;

public class MammothHunt {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt() + 2;
        IntegerPoint2[] pts = new IntegerPoint2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new IntegerPoint2(in.readInt(), in.readInt());
        }
        out.println("YES");
        boolean[] used = new boolean[n];
        used[0] = true;
        IntegerPoint2 last = pts[0];
        out.append(1).append(' ');
        for (int i = 1; i < n; i++) {
            int farthest = -1;
            long dist = -1;
            for (int j = 0; j < n; j++) {
                if (used[j]) {
                    continue;
                }
                long d = IntegerPoint2.dist2(last, pts[j]);
                if (d > dist) {
                    dist = d;
                    farthest = j;
                }
            }
            out.append(farthest + 1).append(' ');
            used[farthest] = true;
            last = pts[farthest];
        }
    }
}
