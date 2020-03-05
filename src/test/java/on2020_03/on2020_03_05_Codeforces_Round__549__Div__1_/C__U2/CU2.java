package on2020_03.on2020_03_05_Codeforces_Round__549__Div__1_.C__U2;



import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.geometry.Line2D;
import template.geometry.Point2D;
import template.geometry.PointConvexHull;
import template.geometry.PointPolygon;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;
import template.utils.GeometryUtils;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CU2 {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Map<Integer, Point2D> map = new HashMap<>(n);
        for (int i = 0; i < n; i++) {
            long x = in.readInt();
            long y = in.readInt();
            y -= x * x;
            Point2D pt = new Point2D(x, y);
            Point2D exist = map.get((int)x);
            if(exist != null && exist.y >= pt.y){
                continue;
            }
            map.put((int)x, pt);
        }
        Point2D[] pts = map.values().stream().toArray(x -> new Point2D[x]);
        n = pts.length;
        if (n <= 2) {
            out.println(n - 1);
            return;
        }

        PointPolygon pp = new PointPolygon(Arrays.asList(pts));
        PointConvexHull pch = PointConvexHull.grahamScan(pp);
        List<Point2D> list = pch.getData();
        SequenceUtils.reverse(list);
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).x < list.get(index).x) {
                index = i;
            }
        }

        SequenceUtils.rotate(list, 0, list.size() - 1, DigitUtils.mod(-index, list.size()));
        index = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).x > list.get(index).x) {
                index = i;
            }
        }


        debug.debug("list", list);
        debug.debug("pts", pts);
        out.println(index);
    }
}
