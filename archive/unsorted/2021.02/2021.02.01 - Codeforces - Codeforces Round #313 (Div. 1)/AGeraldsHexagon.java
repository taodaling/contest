package contest;

import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class AGeraldsHexagon {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] side = in.ri(6);
        //0 3
        List<Point2> pts = new ArrayList<>(6);
        pts.add(new Point2(0, 0));
        Point2 dir = new Point2(1, 0);
        for(int i = 0; i < 5; i++){
            dir = Point2.rotate(dir, Math.PI / 3);
            Point2 back = pts.get(pts.size() - 1);
            pts.add(Point2.plus(back, Point2.mul(dir, side[i])));
        }
        debug.debug("pts", pts);
        double area = Math.abs(Point2.area(pts.toArray(new Point2[0])));
        double ans = area / (Math.sqrt(3) / 4);
        out.println(Math.round(ans));
    }

}
