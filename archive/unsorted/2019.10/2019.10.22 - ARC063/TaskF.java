package contest;

import template.ArrayUtils;
import template.FastInput;
import template.FastOutput;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int w = in.readInt();
        int h = in.readInt();
        int n = in.readInt();
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point();
            pts[i].x = in.readInt();
            pts[i].y = in.readInt();
        }


    }

    Point[] buf;

    public void merge(Point[] pts, int l, int r, int m) {
        int a = l;
        int b = m + 1;
        int wpos = l;
        while (a <= m || b <= r) {
            if (a <= m && (b > r || pts[a].x <= pts[b].x)) {
                buf[wpos++] = pts[a++];
            } else {
                buf[wpos++] = pts[b++];
            }
        }
        ArrayUtils.copy(buf, pts, l, l, r - l + 1);
    }

    int[] dp;
    public int dac(Point[] pts, int l, int r, int ll, int rr, int bb, int tt) {
        if (l == r) {
            int ans = 0;
            ans = Math.max(rr - pts[l].x + tt - bb, ans);
            ans = Math.max(pts[l].x - ll + tt - bb, ans);
            ans = Math.max(rr - ll + pts[l].y - bb, ans);
            ans = Math.max(pts[l].x - ll + tt - pts[l].y, ans);
            return ans * 2;
        }

        int m = DigitUtils.floorAverage(l, r);
        int ans = 0;
        ans = Math.max(ans, dac(pts, l, m, ll, rr, bb, pts[m].y));
        ans = Math.max(ans, dac(pts, l, m, ll, rr, pts[m].y, tt));

        for (int i = l; i <= r; i++) {
            pts[i].bot = i <= m;
        }

        merge(pts, l, r, m);
        dp[]
        for(int i = r; i >= l; i--){

        }
    }
}


class Point {
    int x;
    int y;
    boolean bot;
}
