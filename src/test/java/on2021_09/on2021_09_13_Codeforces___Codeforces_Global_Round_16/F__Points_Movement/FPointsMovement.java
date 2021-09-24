package on2021_09.on2021_09_13_Codeforces___Codeforces_Global_Round_16.F__Points_Movement;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.*;

public class FPointsMovement {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        pts = new Point[n + 2];
        Line[] lines = new Line[m];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point();
            pts[i].l = in.ri();
        }
        pts[n] = new Point();
        pts[n].l = -inf;
        pts[n + 1] = new Point();
        pts[n + 1].l = inf;

        for (int i = 0; i < m; i++) {
            lines[i] = new Line();
            lines[i].l = in.ri();
            lines[i].r = in.ri();
        }
        n += 2;
        Arrays.sort(pts, Comparator.comparingLong(x -> x.l));
        Arrays.sort(lines, Comparator.<Line>comparingLong(x -> x.l).thenComparingLong(x -> -x.r));
        Deque<Line> filterList = new ArrayDeque<>();
        for(Line line : lines){
            while(!filterList.isEmpty() && filterList.peekLast().r >= line.r){
                Line removed = filterList.removeLast();
                debug.debug("removed", removed);
                continue;
            }
            filterList.addLast(line);
        }

        ptIter = 0;
        lines = filterList.stream().filter(line -> {
            while (ptIter < pts.length && pts[ptIter].l < line.l) {
                ptIter++;
            }
            return ptIter == pts.length || pts[ptIter].l > line.r;
        }).toArray(i -> new Line[i]);
        m = lines.length;

        debug.debug("lines", lines);
        debug.debug("pts", pts);
        int lIter = 0;
        for (int i = 0; i < n - 1; i++) {
            Point cur = pts[i];
            Point next = pts[i + 1];
            int begin = lIter;
            while (lIter < m && lines[lIter].l < next.l) {
                lIter++;
            }
            cur.follow = Arrays.copyOfRange(lines, begin, lIter);
        }

        long[][] ans = dac(0, n - 1);
        long best = inf;
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 2; j++){
                best = Math.min(best, ans[i][j]);
            }
        }
        out.println(best);
    }

    Debug debug = new Debug(false);
    Point[] pts;

    public long take(Line[] lines, Point lp, Point rp, int ltake, int rtake) {
        if (lines.length == 0) {
            return 0;
        }
        long best = Math.min((rp.l - lines[0].r) * rtake, (lines[lines.length - 1].l - lp.l) * ltake);
        for (int i = 0; i + 1 < lines.length; i++) {
            best = Math.min(best, (lines[i].l - lp.l) * ltake + (rp.l - lines[i + 1].r) * rtake);
        }
        return best;
    }

    public long[][] merge(long[][] a, long[][] b, Point lp, Point rp) {
        long[][] ans = new long[2][2];
        Line[] lines = lp.follow;
        SequenceUtils.deepFill(ans, inf);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    for (int t = 0; t < 2; t++) {
                        long cost = a[i][j] + b[k][t] + take(lines, lp, rp, j + 1, k + 1);
                        ans[i][t] = Math.min(ans[i][t], cost);
                    }
                }
            }
        }

        return ans;
    }

    public long[][] dac(int l, int r) {
        if (l == r) {
            long[][] ans = new long[2][2];
            ans[0][0] = ans[1][1] = inf;
            return ans;
        }
        int m = (l + r) / 2;
        long[][] L = dac(l, m);
        long[][] R = dac(m + 1, r);
        long[][] ans = merge(L, R, pts[m], pts[m + 1]);
        debug.debug("l", l);
        debug.debug("r", r);
        debug.debug("ans", ans);
        return ans;
    }

    int ptIter = 0;
    long inf = (long) 1e15;
}

class Line extends Point {
    long r;


    @Override
    public String toString() {
        return "Line{" +
                "r=" + r +
                ", l=" + l +
                '}';
    }
}

class Point {
    long l;
    Line[] follow;

    @Override
    public String toString() {
        return "Point{" +
                "l=" + l +
                '}';
    }
}