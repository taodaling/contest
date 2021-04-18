package on2021_03.on2021_03_30_CS_Academy___Virtual_FII_Code_2020_Round__1__rated_for_all_.Cosmological_Nightmare;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Arrays;
import java.util.Comparator;

public class CosmologicalNightmare {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point();
            pts[i].x = in.ri();
            pts[i].y = in.ri();
        }
        int m = in.ri();
        for (int i = 0; i < m; i++) {
            long u = in.ri();
            long v = in.ri();
            if (u == 0 && v == 0) {
                out.println("NO");
                continue;
            }
            for (Point pt : pts) {
                //move
                if (v == 0) {
                    //move by u
                    pt.sx = DigitUtils.mod(pt.x, Math.abs(u));
                    pt.sy = pt.y;
                } else if (u == 0) {
                    pt.sx = pt.x;
                    pt.sy = DigitUtils.mod(pt.y, Math.abs(v));
                } else {
                    long to = DigitUtils.mod(pt.x, Math.abs(u));
                    long step = (to - pt.x) / u;
                    pt.sx = pt.x + u * step;
                    pt.sy = pt.y + v * step;
                }
            }

            Arrays.sort(pts, Comparator.<Point>comparingLong(x -> x.sx).thenComparingLong(x -> x.sy));
            boolean ok = true;
            for (int j = 0; j < n; j++) {
                int l = j;
                int r = j;
                while (r + 1 < n && pts[r + 1].sx == pts[l].sx && pts[r + 1].sy == pts[r].sy) {
                    r++;
                }
                j = r;
                int cnt = r - l + 1;
                if(cnt % 2 != 0){
                    ok = false;
                }
            }
            out.println(ok ? "YES" : "NO");
        }
    }
}

class Point {
    long x;
    long y;
    long sx;
    long sy;
}