package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class DEqualCut {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);

        Region r1 = new Region(a, 0, 1);
        Region r2 = new Region(a, 2, n - 1);
        r1.fix();
        r2.fix();
        long ans = solve(r1, r2);
        while (r2.r - r2.l + 1 > 2) {
            r1.rr();
            r2.lr();
            r1.fix();
            r2.fix();
            ans = Math.min(ans, solve(r1, r2));
        }

        out.println(ans);
    }

    public long solve(Region r1, Region r2) {
        long max = Math.max(r1.sl, r1.sr);
        max = Math.max(max, r2.sl);
        max = Math.max(max, r2.sr);

        long min = Math.min(r1.sl, r1.sr);
        min = Math.min(min, r2.sl);
        min = Math.min(min, r2.sr);

        return max - min;
    }
}

class Region {
    int[] area;
    int l;
    int r;
    int mid;
    long sl;
    long sr;

    public Region(int[] area, int l, int r) {
        mid = l;
        this.area = area;
        this.l = l;
        this.r = r;
        for (int i = l; i <= r; i++) {
            if (i <= mid) {
                sl += area[i];
            } else {
                sr += area[i];
            }
        }
    }

    public void ll() {
        l--;
        sl += area[l];
    }

    public void lr() {
        sl -= area[l];
        l++;
    }

    public void rl() {
        sr -= area[r];
        r--;
    }

    public void rr() {
        r++;
        sr += area[r];
    }

    public void fix() {
        while (mid > l && Math.abs(sr - sl) > Math.abs(sr - sl + 2 * area[mid])) {
            sr += area[mid];
            sl -= area[mid];
            mid--;
        }
        while (r - mid > 1 && Math.abs(sr - sl) > Math.abs(sr - sl - 2 * area[mid + 1])) {
            mid++;
            sr -= area[mid];
            sl += area[mid];
        }
    }
}