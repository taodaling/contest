package contest;

import template.geometry.geo2.Point;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class Archipelago {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int v1 = in.readInt() - 1;
        int v2 = in.readInt() - 1;
        Point p1 = new Point(in.readDouble(), in.readDouble());
        Point p2 = new Point(in.readDouble(), in.readDouble());
        List<Point> ans = new ArrayList<>(n);
        Point step = new Point(1, 0);
        double theta = Math.PI + (n - 2) * Math.PI / n;
        ans.add(new Point(0, 0));
        for (int i = 1; i < n; i++) {
            ans.add(Point.plus(Point.rotate(step, (i - 1) * theta),
                    ans.get(i - 1)));
        }

        debug.debug("ans", ans);
        Point w1 = ans.get(v1);
        Point w2 = ans.get(v2);
        List<Point> transformed = new ArrayList<>(n);
        debug.debug("", Point.linearTransform(w1, p1, w2, p2, w1));
        debug.debug("", Point.linearTransform(w1, p1, w2, p2, w2));
        for (Point pt : ans) {
            transformed.add(Point.linearTransform(w1, p1, w2, p2, pt));
        }

        for (Point pt : transformed) {
            out.append(pt.x).append(' ').append(pt.y).println();
        }
    }
}
