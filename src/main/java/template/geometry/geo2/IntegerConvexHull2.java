package template.geometry.geo2;

import template.utils.SequenceUtils;

import java.util.*;

public class IntegerConvexHull2 {

    public static Collection<IntegerPoint2> grahamScan(List<IntegerPoint2> pointPolygon, boolean includeSameLine) {
        if (pointPolygon.size() <= 1) {
            return pointPolygon;
        }

        final IntegerPoint2[] points = pointPolygon.toArray(new IntegerPoint2[0]);
        int n = points.length;
        for (int i = 1; i < n; i++) {
            int cmp = IntegerPoint2.SORT_BY_XY.compare(points[i], points[0]);
            if (cmp >= 0) {
                continue;
            }
            SequenceUtils.swap(points, 0, i);
        }

        IntegerPoint2 first = points[0];
        Comparator<IntegerPoint2> cmpByPolarAngle = IntegerPoint2.sortByPolarAngleAround(first);
        Arrays.sort(points, 1, n, cmpByPolarAngle.thenComparingLong(x -> IntegerPoint2.dist2(first, x)));
        if (!includeSameLine) {
            int shrinkSize = 2;
            for (int i = 2; i < n; i++) {
                if (cmpByPolarAngle.compare(points[i], points[shrinkSize - 1]) == 0) {
                    points[shrinkSize - 1] = points[i];
                } else {
                    points[shrinkSize++] = points[i];
                }
            }
            n = shrinkSize;
        } else {
            int r = n - 1;
            int l = r;
            while (l - 1 > 0 && cmpByPolarAngle.compare(points[l], points[l - 1]) == 0) {
                l--;
            }
            SequenceUtils.reverse(points, l, r);
        }
        Deque<IntegerPoint2> stack = new ArrayDeque<>(n);
        stack.addLast(points[0]);
        for (int i = 1; i < n; i++) {
            while (stack.size() >= 2) {
                IntegerPoint2 last = stack.removeLast();
                IntegerPoint2 second = stack.peekLast();
                int sign = Long.signum(IntegerPoint2.cross(second, points[i], last));
                if (sign < 0 || includeSameLine && sign == 0) {
                    stack.addLast(last);
                    break;
                }
            }
            stack.addLast(points[i]);
        }

        return stack;
    }
}
