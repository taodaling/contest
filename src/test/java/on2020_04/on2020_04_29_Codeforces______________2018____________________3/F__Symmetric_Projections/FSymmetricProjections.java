package on2020_04.on2020_04_29_Codeforces______________2018____________________3.F__Symmetric_Projections;



import template.geometry.GeoConstant;
import template.geometry.geo2.Line2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongHashMap;
import template.primitve.generated.datastructure.LongHashSet;
import template.utils.CompareUtils;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class FSymmetricProjections {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point2[] pts = new Point2[n];
        long[] sum = new long[2];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2(in.readInt(), in.readInt());
            sum[0] += pts[i].x;
            sum[1] += pts[i].y;
        }

        Point2 center = new Point2(sum[0] / (double) n, sum[1] / (double) n);

        boolean symmetric = true;
        TreeSet<Point2> set = new TreeSet<>(Point2.SORT_BY_XY);
        set.addAll(Arrays.asList(pts));

        for (int i = 0; i < n; i++) {
            Point2 mirror = Point2.mirror(center, pts[i]);
            if (!set.contains(mirror)) {
                symmetric = false;
                break;
            }
        }

        if (symmetric) {
            out.println(-1);
            return;
        }

        for (int i = 0; i < n; i++) {
            Point2 mirror = Point2.mirror(center, pts[i]);
            if (set.contains(mirror)) {
                set.remove(mirror);
                set.remove(pts[i]);
            }
        }

        pts = set.stream().toArray(c -> new Point2[c]);
        n = pts.length;

        Point2[] vecs = new Point2[n * n];
        int tail = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                Point2 between = Point2.middle(pts[i], pts[j]);
                Point2 vec = Point2.minus(center, between).perpendicular();

                if (GeoConstant.sign(vec.y) < 0 || GeoConstant.sign(vec.y) == 0 && GeoConstant.sign(vec.x) < 0) {
                    vec = vec.negate();
                }
                vec = vec.norm();

                vecs[tail++] = vec;
                if (i != j) {
                    vecs[tail++] = vec;
                }
            }
        }

        CompareUtils.quickSort(vecs, Point2.SORT_BY_XY, 0, vecs.length);
        List<Line2> candidates = new ArrayList<>(n);
        for (int i = 0, size = vecs.length; i < size; i++) {
            int r = i;
            while (r + 1 < size && Point2.SORT_BY_XY.compare(vecs[r + 1], vecs[r]) == 0) {
                r++;
            }

            if (r - i + 1 >= n) {
                candidates.add(new Line2(Point2.ORIGIN, vecs[i]));
            }
            i = r;
        }

        debug.debug("n", n);
        debug.debug("center", center);
        debug.debug("vecs", vecs);
        debug.debug("candidates", candidates);

        int ans = 0;
        Point2[] projections = new Point2[n];
        for (int j = 0; j < candidates.size(); j++) {
            Line2 line = candidates.get(j);
            for (int i = 0; i < n; i++) {
                projections[i] = line.projection(pts[i]);
            }
            CompareUtils.quickSort(projections, line.sortPointAlongLine(), 0, projections.length);
            Point2 proj = line.projection(center);
            int l = 0;
            int r = n - 1;
            boolean valid = true;
            while (l < r) {
                if (GeoConstant.compare((projections[l].x + projections[r].x) / 2, proj.x) != 0 ||
                        GeoConstant.compare((projections[l].y + projections[r].y) / 2, proj.y) != 0) {
                    valid = false;
                    break;
                }
                l++;
                r--;
            }

            if (valid) {
                ans++;
                debug.debug("allow", line);
            }
        }

        out.println(ans);
    }
}
