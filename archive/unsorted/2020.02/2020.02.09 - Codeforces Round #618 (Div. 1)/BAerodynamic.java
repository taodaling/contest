package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class BAerodynamic {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(in.readInt(), in.readInt());
        }
        if (n % 2 != 0) {
            out.println("NO");
            return;
        }
        for (int i = 0; i < n; i++) {
            int ii = DigitUtils.mod(i + 1, n);
            int j = DigitUtils.mod(i + (n / 2), n);
            int jj = DigitUtils.mod(j + 1, n);

            if (!(cross(points[i].x - points[ii].x,
                    points[i].y - points[ii].y,
                    points[j].x - points[jj].x,
                    points[j].y - points[jj].y) == 0 &&
                    dist2(points[i], points[ii]) ==
                            dist2(points[j], points[jj]))) {
                out.println("NO");
                return;
            }
        }

        out.println("YES");
    }

    public long cross(long x1, long y1, long x2, long y2) {
        return x1 * y2 - x2 * y1;
    }

    public long dist2(Point a, Point b) {
        long dx = a.x - b.x;
        long dy = a.y - b.y;
        return dx * dx + dy * dy;
    }
}

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}