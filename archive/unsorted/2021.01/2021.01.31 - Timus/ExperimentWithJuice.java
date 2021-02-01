package contest;

import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class ExperimentWithJuice {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Point2[] pts = new Point2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2(in.rd(), in.rd());
        }
        Point2 hole = new Point2(in.rd(), in.rd());
        for (int i = 0; i < n; i++) {
            Point2 cur = pts[i];
            Point2 next = pts[(i + 1) == n ? 0 : i + 1];
            if (Point2.onSegment(cur, next, hole)) {
                //i -> n - 1
                //0 -> n - 1 - i
                SequenceUtils.rotate(pts, 0, n - 1, n - 1 - i);
                break;
            }
        }
        Point2 right = Point2.plus(hole, new Point2(1, 0));
        if(pts[0])
    }
}
