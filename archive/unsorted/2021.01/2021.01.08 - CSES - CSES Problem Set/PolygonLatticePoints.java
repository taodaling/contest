package contest;

import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;

public class PolygonLatticePoints {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerPoint2[] pts = new IntegerPoint2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new IntegerPoint2(in.ri(), in.ri());
        }
        long A2 = Math.abs(IntegerPoint2.area2(pts));
        long onBound = 0;
        for (int i = 0; i < n; i++) {
            IntegerPoint2 cur = pts[i];
            IntegerPoint2 next = pts[(i + 1) % n];
            long w = Math.abs(next.x - cur.x);
            long h = Math.abs(next.y - cur.y);
            onBound += GCDs.gcd(w, h);
        }
        long inner2 = A2 + 2 - onBound;
        out.println(inner2 / 2);
        out.println(onBound);
    }
}
