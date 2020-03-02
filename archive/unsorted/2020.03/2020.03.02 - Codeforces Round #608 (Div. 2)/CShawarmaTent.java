package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class CShawarmaTent {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int sx = in.readInt();
        int sy = in.readInt();

        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(in.readInt() - sx, in.readInt() - sy);
        }

        int ans = 0;
        Point best = new Point(0, 0);

        for (int[] sign : new int[][]{
                {1, 1},
                {-1, 1},
                {-1, -1},
                {1, -1}
        }) {
            for (Point pt : points) {
                pt.x *= sign[0];
                pt.y *= sign[1];
            }

            Point tmp = new Point(0, 0);
            int x = solve(points, tmp);
            if (x > ans) {
                ans = x;
                best = tmp;
                best.x *= sign[0];
                best.y *= sign[1];
            }

            for (Point pt : points) {
                pt.x *= sign[0];
                pt.y *= sign[1];
            }
        }

        out.println(ans);
        out.append(best.x + sx).append(' ').append(best.y + sy).println();
    }

    public int solve(Point[] pts, Point camp) {
        int ans = 0;
        int ct1 = count(pts, new Point(1, 0));
        if (ct1 > ans) {
            ans = ct1;
            camp.x = 1;
            camp.y = 0;
        }
        int ct2 = count(pts, new Point(1, 1));
        if (ct2 > ans) {
            ans = ct2;
            camp.x = 1;
            camp.y = 1;
        }
        int ct3 = count(pts, new Point(0, 1));
        if (ct3 > ans) {
            ans = ct3;
            camp.x = 0;
            camp.y = 1;
        }
        return ans;
    }

    public int count(Point[] pts, Point lb) {
        int count = 0;
        for (Point pt : pts) {
            if (lb.y == 0 && pt.x >= lb.x) {
                count++;
            } else if (lb.x == 0 && pt.y >= lb.y) {
                count++;
            } else if (lb.x > 0 && lb.y > 0 && pt.x >= lb.x && pt.y >= lb.y) {
                count++;
            }
        }
        return count;
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
