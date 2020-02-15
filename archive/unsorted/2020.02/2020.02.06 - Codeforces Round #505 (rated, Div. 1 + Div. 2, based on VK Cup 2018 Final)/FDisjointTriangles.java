package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.GeometryUtils;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class FDisjointTriangles {
    int n;
    Point[] relativePoints;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        Point[] points = new Point[n];
        relativePoints = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(in.readInt(), in.readInt());
            relativePoints[i] = new Point();
        }

        long ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                relativePoints[j].x = points[j].x - points[i].x;
                relativePoints[j].y = points[j].y - points[i].y;
                relativePoints[j].atan2 = GeometryUtils.theta(relativePoints[j].y, relativePoints[j].x);
            }
            SequenceUtils.swap(relativePoints, i, n - 1);
            ans += count(points[i]);
        }

        System.err.println(ans);
        out.println(ans / 4);
    }

    public long cross(Point a, Point b) {
        return cross(a.x, a.y, b.x, b.y);
    }

    public long cross(long x1, long y1, long x2, long y2) {
        return x1 * y2 - x2 * y1;
    }

    public long pick(int n, int m) {
        if (n < m) {
            return 0;
        }
        return m == 0 ? 1 : (n * pick(n - 1, m - 1) / m);
    }

    public long count(Point center) {
        Arrays.sort(relativePoints, 0, n - 1, (a, b) -> Double.compare(a.atan2, b.atan2));

        long total = 0;
        int iter = 1;
        for (int i = 0; i < n - 1; i++) {
            while (!(cross(relativePoints[i], relativePoints[DigitUtils.mod(iter - 1, n - 1)]) >= 0 &&
                    cross(relativePoints[i], relativePoints[iter]) <= 0)) {
                iter = DigitUtils.mod(iter + 1, n - 1);
            }
            int leftCnt = DigitUtils.mod(iter - 1 - i, n - 1);
            int rightCnt = n - 1 - 1 - leftCnt;
            long contrib = pick(leftCnt, 2) * pick(rightCnt, 2) * 2;
            total += contrib;

//            System.err.printf("(%d,%d)=>(%d,%d) = %d\n", center.x, center.y,
//                    relativePoints[i].x + center.x, relativePoints[i].y + center.y,
//                    contrib);
        }
        return total;
    }

}

class Point {
    int x;
    int y;
    double atan2;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
    }
}
