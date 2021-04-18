package on2021_03.on2021_03_29_Codeforces___Codeforces_Round__223__Div__1_.D__Sereja_and_Cinema;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DSerejaAndCinema {
    int mod = (int) 1e9 + 7;
    Combination comb = new Combination((int) 2e5, mod);
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        List<Point[]> pts = new ArrayList<>(n);
        int middle = -1;
        pts.add(new Point[]{new Point(0, n - 1, 1, true)});
        for (int i = 0; i < n; i++) {
            int index = in.ri();
            if (index == 0) {
                continue;
            }
            if (index != 1) {
                Point[] pair = new Point[]{
                        //as l
                        new Point(i + 1, i + index - 1, 0, true),
                        new Point(i - index + 1, i - 1, 0, false),
                };

                pts.add(pair);
            } else {
                middle = i;
                continue;
            }
        }

        pts.sort(Comparator.<Point[]>comparingInt(x -> x[0].length()).reversed());
        Point[] last = null;
        for (Point[] arr : pts) {
            if (last == null) {
                last = arr;
                continue;
            }

            //upd
            for (Point prev : last) {
                if (prev.way == 0) {
                    continue;
                }
                for (Point next : arr) {
                    if (next.l < prev.l || next.r > prev.r) {
                        continue;
                    }
                    int step = prev.length() - next.length() - 1;
                    int ldelta = next.l - prev.l;
                    if (next.leftMove) {
                        ldelta--;
                    } else {
                    }
                    next.way += prev.way * comb.combination(step, ldelta) % mod;
                }
            }

            for (Point pt : arr) {
                pt.way %= mod;
            }
            last = arr;
        }

        debug.debug("pts", pts);
        long ans = 0;
        for (Point pt : last) {
            int l = pt.l;
            int r = pt.r;
            if (middle == -1) {
                for (int m = l; m <= r; m++) {
                    int left = m - l;
                    int right = r - m;
                    ans += pt.way * comb.combination(left + right, left) % mod;
                }
            } else {
                int m = middle;
                int left = m - l;
                int right = r - m;
                if (left >= 0 && right >= 0) {
                    ans += pt.way * comb.combination(left + right, left) % mod;
                }
            }
        }

        ans %= mod;
        out.println(ans);
    }
}

class Point {
    int l;
    int r;
    long way;
    boolean leftMove;

    public Point(int l, int r, long way, boolean leftMove) {
        this.l = l;
        this.r = r;
        this.way = way;
        this.leftMove = leftMove;
    }

    public int length() {
        return r - l + 1;
    }

    @Override
    public String toString() {
        return "Point{" +
                "l=" + l +
                ", r=" + r +
                ", way=" + way +
                '}';
    }
}
