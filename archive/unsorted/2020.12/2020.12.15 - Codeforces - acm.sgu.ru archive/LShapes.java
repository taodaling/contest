package contest;

import template.geometry.geo2.IntegerPoint2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.*;

public class LShapes {
    Map<Long, Point> map = new HashMap<>((int) 5e3);

    public long sig(int x, int y) {
        return DigitUtils.asLong(x, y);
    }

    public Point get(int x, int y) {
        long sig = sig(x, y);
        if (!map.containsKey(sig)) {
            Point pt = new Point(x, y);
            map.put(sig, pt);
        }
        return map.get(sig);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        for (int i = 0; i < n; i++) {
            Point a = get(in.ri(), in.ri());
            Point b = get(in.ri(), in.ri());
            a.adj.add(b);
            b.adj.add(a);
        }
        long ans = 0;
        Deque<Point> dq = new ArrayDeque<>(n * 2);
        for (Point pt : map.values()) {
            Comparator<IntegerPoint2> comp = IntegerPoint2.sortByPolarAngleAround(pt);
            pt.adj.sort(comp);
            dq.clear();
            dq.addAll(pt.adj);
            for (int i = 0; i < pt.adj.size(); i++) {
                Point x = pt.adj.get(i);
                int r = i;
                while (r + 1 < pt.adj.size() && comp.compare(pt.adj.get(r + 1), x) == 0) {
                    r++;
                }
                IntegerPoint2 perp = IntegerPoint2.plus(
                        IntegerPoint2.minus(x, pt).perpendicular(), pt);
                while (!dq.isEmpty() && IntegerPoint2.orient(pt, x, dq.peekFirst()) >= 0 &&
                        IntegerPoint2.orient(pt, perp, dq.peekFirst()) < 0) {
                    dq.removeFirst();
                }
                long count = 0;
                while (!dq.isEmpty() && IntegerPoint2.orient(pt, x, dq.peekFirst()) > 0 &&
                        IntegerPoint2.orient(pt, perp, dq.peekFirst()) == 0) {
                    dq.removeFirst();
                    count++;
                }
                ans += count * (r - i + 1);
                for (int j = i; j <= r; j++) {
                    dq.addLast(pt.adj.get(j));
                }
                i = r;
            }
        }
        out.println(ans);
    }
}

class Point extends IntegerPoint2 {
    List<Point> adj = new ArrayList<>();

    public Point(long x, long y) {
        super(x, y);
    }
}
