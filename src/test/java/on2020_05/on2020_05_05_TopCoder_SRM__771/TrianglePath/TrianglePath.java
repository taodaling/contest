package on2020_05.on2020_05_05_TopCoder_SRM__771.TrianglePath;



import template.geometry.geo2.Line2;
import template.geometry.geo2.Point2;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class TrianglePath {
    //Debug debug = new Debug(true);

    public int[] construct(int N, int Xmax, int Ymax, int[] Xprefix, int[] Yprefix, int seed) {
        long state = seed;

        List<Point2> triangle = new ArrayList<>(3);
        triangle.add(new Point2(0, Ymax));
        triangle.add(new Point2(0, 0));
        triangle.add(new Point2(Xmax, 0));
        List<Point2> list = new ArrayList<>(N);
        for (int i = 0; i < Xprefix.length; i++) {
            list.add(new Point2(Xprefix[i], Yprefix[i]));
        }

        while (list.size() < N) {
            state = (state * 1103515245 + 12345) % (1L << 31);
            int xnew = (int) (state % (Xmax + 1));
            state = (state * 1103515245 + 12345) % (1L << 31);
            int ynew = (int) (state % (Ymax + 1));

            Point2 pt = new Point2(xnew, ynew);
            if (Point2.inPolygon(triangle, pt) == 0) {
                continue;
            }
            list.add(pt);
        }

        //solve
        Point2[] pts = list.toArray(new Point2[0]);
        int[] indices = new int[N];
        for (int i = 0; i < N; i++) {
            indices[i] = i;
        }
        dfs(pts, indices, 0, pts.length - 1, triangle);
        return indices;
    }

    //start with A and end with C
    public void dfs(Point2[] pt, int[] indices, int l, int r, List<Point2> triangles) {
        if (l > r) {
            return;
        }
        if (l == r) {
            return;
        }

        boolean same = true;
        for (int i = l + 1; i <= r; i++) {
            if (Point2.SORT_BY_XY.compare(pt[indices[i]],
                    pt[indices[i - 1]]) != 0) {
                same = false;
            }
        }
        if (same) {
            return;
        }


        //debug.debug("tri", triangles);
        Line2 back = new Line2(triangles.get(0), triangles.get(2));
        Point2 proj = back.projection(triangles.get(1));

        List<Point2> up = new ArrayList<>(3);
        List<Point2> bot = new ArrayList<>(3);
        up.add(triangles.get(0));
        up.add(proj);
        up.add(triangles.get(1));

        bot.add(triangles.get(1));
        bot.add(proj);
        bot.add(triangles.get(2));

        int whead = l - 1;

        for (int i = l; i <= r; i++) {
            if (Point2.inPolygon(up, pt[indices[i]]) > 0) {
                SequenceUtils.swap(indices, ++whead, i);
            }

        }
        dfs(pt, indices, l, whead, up);
        dfs(pt, indices, whead + 1, r, bot);
    }
}
