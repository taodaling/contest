package on2019_11.on2019_11_04_agc019.Fountain_Walk;




import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import template.FastInput;

public class FountainWalk {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        Point begin = new Point();
        begin.x = in.readInt();
        begin.y = in.readInt();

        Point end = new Point();
        end.x = in.readInt();
        end.y = in.readInt();

        int n = in.readInt();
        List<Point> pts = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            Point pt = new Point();
            pt.x = in.readInt();
            pt.y = in.readInt();
            pts.add(pt);
        }

        if (begin.x > end.x) {
            begin.x = -begin.x;
            end.x = -end.x;
            for (Point pt : pts) {
                pt.x = -pt.x;
            }
        }
        if (begin.y > end.y) {
            begin.y = -begin.y;
            end.y = -end.y;
            for (Point pt : pts) {
                pt.y = -pt.y;
            }
        }

        Point[] ptArray = pts.stream().filter(x -> inRange(begin, end, x.x, x.y)).toArray(x -> new Point[x]);
        Arrays.sort(ptArray, (a, b) -> Integer.compare(a.x, b.x));

        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < ptArray.length; i++) {
            Point pt = ptArray[i];
            Map.Entry<Integer, Integer> floor = map.floorEntry(pt.y);
            int dp = (floor == null ? 0 : floor.getValue()) + 1;
            while (true) {
                Map.Entry<Integer, Integer> ceil = map.ceilingEntry(pt.y);
                if (ceil == null || ceil.getValue() >= dp) {
                    break;
                }
                map.remove(ceil.getKey());
            }
            map.put(pt.y, dp);
        }

        int cnt = map.isEmpty() ? 0 : map.lastEntry().getValue();
        double ans = (double)(end.x - begin.x + end.y - begin.y) * 100;
        ans = ans + cnt * (circle - 20);

        if (cnt == (end.x - begin.x + 1) || cnt == (end.y - begin.y + 1)) {
            ans += circle;
        }

        out.printf("%.15f", ans);
    }

    double circle = Math.PI * 10 / 2;

    public boolean inRange(Point lb, Point rt, int x, int y) {
        return lb.x <= x && rt.x >= x && lb.y <= y && rt.y >= y;
    }
}


class Point {
    int x;
    int y;
}
