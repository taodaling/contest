package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;

public class COptimalPolygonPerimeter {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point[] points = new Point[n];

        int inf = (int) 1e8;
        int left = inf;
        int right = -inf;
        int top = -inf;
        int bot = inf;

        for (int i = 0; i < n; i++) {
            points[i] = new Point(in.readInt(), in.readInt());
            left = Math.min(left, points[i].x);
            right = Math.max(right, points[i].x);
            top = Math.max(top, points[i].y);
            bot = Math.min(bot, points[i].y);
        }

        //lt
        Point lt = SortUtils.maxOf(points, 0, n - 1, (a, b) -> {
            return Integer.compare(a.y - a.x, b.y - b.x);
        });
        //lb
        Point lb = SortUtils.maxOf(points, 0, n - 1, (a, b) -> {
            return Integer.compare(-a.y - a.x, -b.y - b.x);
        });
        //rt
        Point rt = SortUtils.maxOf(points, 0, n - 1, (a, b) -> {
            return Integer.compare(a.y + a.x, b.y + b.x);
        });
        //rt
        Point rb = SortUtils.maxOf(points, 0, n - 1, (a, b) -> {
            return Integer.compare(-a.y + a.x, -b.y + b.x);
        });

        long ans = 0;
        ans = Math.max(ans, lt.y - lt.x + right - bot);
        ans = Math.max(ans, -lb.y - lb.x + right + top);
        ans = Math.max(ans, rt.y + rt.x - left - bot);
        ans = Math.max(ans, -rb.y + rb.x - left + top);

        out.println(ans * 2);
        long otherAns = right - left + top - bot;
        for(int i = 4; i <= n; i++){
            out.println(otherAns * 2);
        }
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