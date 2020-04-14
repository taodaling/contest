package contest;

import template.graph.ManhattanMST;
import template.math.Modular;
import template.math.Power;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class ComponentsForever {
    Debug debug = new Debug(true);

    public int countComponents(int n, int[] Xprefix, int[] Yprefix, int seed, int Xrange, int Yrange, int Llo, int Rhi) {
        Point[] points = new Point[n];

        for (int i = 0; i < Xprefix.length; i++) {
            points[i] = new Point(Xprefix[i], Yprefix[i]);
        }
        long state = seed;
        for (int i = Xprefix.length; i < n; i++) {
            state = (1103515245 * state + 12345);
            long x = state % Xrange;
            state = state % (1L << 31);
            state = (1103515245 * state + 12345);
            long y = state % Yrange;
            state = state % (1L << 31);

            points[i] = new Point(x, y);
        }

        //  debug.debug("points", points);

        List<Point>[] classify = new List[2];
        for (int i = 0; i < 2; i++) {
            classify[i] = new ArrayList<>(n);
        }

        for (int i = 0; i < n; i++) {
            if (points[i].x % 2 == points[i].y % 2) {
                classify[0].add(points[i]);
            } else {
                classify[1].add(points[i]);
            }
        }

        int ans = 0;
        for (int i = 0; i < 2; i++) {
            int exp = exp(classify[i].toArray(new Point[0]), Llo, Rhi);
            // debug.debug("exp", exp);
            ans = mod.plus(ans, exp);
        }
        return ans;
    }

    Modular mod = new Modular(1e9 + 7);
    Power power = new Power(mod);

    public int pick2(int n) {
        return mod.valueOf((long) n * (n - 1) / 2);
    }

    public int pick2(int l, int r) {
        if (l > r) {
            return 0;
        }
        return pick2(r - l + 1);
    }

    public int exp(Point[] points, int l, int r) {
        int n = points.length;

        if (n <= 0) {
            return 0;
        }

        long[] x = new long[n];
        long[] y = new long[n];

        for (int i = 0; i < n; i++) {
            x[i] = points[i].x + points[i].y;
            y[i] = points[i].x - points[i].y;
        }

        ManhattanMST mst = new ManhattanMST(n, x, y);
        int[][] edges = mst.getMst();
        int total = pick2(l, r);
        int ans = total;
        for (int i = 0; i < n - 1; i++) {
            int a = edges[0][i];
            int b = edges[1][i];
            long dist = Math.max(Math.abs(points[a].x - points[b].x), Math.abs(points[a].y - points[b].y));
            if (dist > r) {
                ans = mod.plus(ans, total);
            } else if (dist <= l) {

            } else {
                ans = mod.plus(ans, pick2(l, (int) dist - 1));
            }
        }

        return mod.mul(ans, power.inverseByFermat(total));
    }

}

class Point {
    long x;
    long y;

    public Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

}
