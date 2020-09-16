package on2020_09.on2020_09_09_Codeforces___Codeforces_Round__363__Div__1_.D__Limak_and_Shooting_Points;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.GCDs;

import java.util.*;

public class DLimakAndShootingPoints {
    Point[] hero;
    Point[] monster;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        int n = in.readInt();

        hero = new Point[k];
        for (int i = 0; i < k; i++) {
            hero[i] = new Point(in.readInt(), in.readInt());
            hero[i].correspond = new List[n];
        }
        monster = new Point[n];
        for (int i = 0; i < n; i++) {
            monster[i] = new Point(in.readInt(), in.readInt());
            monster[i].id = i;
        }
        for (int i = 0; i < n; i++) {
            Map<Long, List<Point>> map = new HashMap<>();
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                Long sign = getSign(monster[j].x - monster[i].x,
                        monster[j].y - monster[i].y);
                map.computeIfAbsent(sign, x -> new ArrayList<>()).add(monster[j]);
            }
            for (List<Point> list : map.values()) {
                list.sort(monster[i].distComparator);
            }
            monster[i].map = map;
            for (int j = 0; j < k; j++) {
                long sign = getSign(hero[j].x - monster[i].x, hero[j].y - monster[i].y);
                List<Point> list = monster[i].map.getOrDefault(sign, Collections.emptyList());
                hero[j].correspond[i] = list;
            }
        }

        fact(new boolean[k], new Point[k], 0);
        int ans = 0;
        for (Point pt : monster) {
            if (pt.killable) {
                ans++;
            }
        }

        out.println(ans);
    }


    private void fact(boolean[] used, Point[] seq, int i) {
        if (i == seq.length) {
            for (Point m : monster) {
                if (m.killable) {
                    continue;
                }
                heroDq.clear();
                for (Point pt : seq) {
                    heroDq.addLast(pt);
                }
                killMonster(m);
                for (Point pt : trace) {
                    pt.killed = false;
                }
                trace.clear();
            }
            return;
        }
        for (int j = 0; j < used.length; j++) {
            if (used[j]) {
                continue;
            }
            used[j] = true;
            seq[i] = hero[j];
            fact(used, seq, i + 1);
            used[j] = false;
        }
    }

    Deque<Point> heroDq = new ArrayDeque<>();
    List<Point> trace = new ArrayList<>();

    private boolean killMonster(Point t) {
        if (t.killed) {
            return true;
        }
        if (heroDq.isEmpty()) {
            return false;
        }
        Point h = heroDq.removeFirst();

        List<Point> list = h.correspond[t.id];

        for (Point pt : list) {
            if (t.distComparator.compare(pt, h) >= 0) {
                break;
            }
            if (!killMonster(pt)) {
                return false;
            }
        }

        trace.add(t);
        t.killable = t.killed = true;
        return true;
    }

    private long getSign(int x, int y) {
        int g = GCDs.gcd(Math.abs(x), Math.abs(y));
        return DigitUtils.asLong(x / g, y / g);
    }

}


class Point {
    int x;
    int y;
    Comparator<Point> distComparator = (a, b) -> {
        return Integer.compare(weight(a), weight(b));
    };
    boolean killable;
    boolean killed;
    Map<Long, List<Point>> map;
    List<Point>[] correspond;
    int id;


    public int weight(Point a) {
        return Math.max(Math.abs(a.x - x), Math.abs(a.y - y));
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
