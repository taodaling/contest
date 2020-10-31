package template.problem;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongArrayList;
import template.primitve.generated.datastructure.LongBIT;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class RectPointSumProblem {
    private static int sign(int x) {
        return (Integer.bitCount(x) & 1) == 0 ? 1 : -1;
    }

    public static long[] solve(Point2D[] pts, Query2D[] qs) {
        int m = qs.length;
        Point2D[][] sub = new Point2D[4][m];
        long[] ans = new long[m];
        long[] xs = new long[2];
        long[] ys = new long[2];
        for (int i = 0; i < m; i++) {
            xs[0] = qs[i].xr;
            xs[1] = qs[i].xl - 1;
            ys[0] = qs[i].yr;
            ys[1] = qs[i].yl - 1;
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    sub[(j << 1) | k][i] = new Point2D(xs[j], ys[k], 0);
                }
            }
        }
        prefixSum(SequenceUtils.pack(i -> new Point2D[i], sub[0], sub[1], sub[2], sub[3], pts));
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 4; j++) {
                ans[i] += sign(j) * sub[j][i].sum;
            }
        }
        return ans;
    }

    public static void prefixSum(Point2D[] pts) {
        Arrays.sort(pts, (a, b) -> {
            int cmp = Long.compare(a.x, b.x);
            if (cmp == 0) {
                cmp = Long.compare(a.y, b.y);
            }
            if (cmp == 0) {
                cmp = -Long.compare(a.w, b.w);
            }
            return cmp;
        });
        dac(pts, 0, pts.length - 1, new Point2D[pts.length]);
    }

    public static long[] solve(Point3D[] pts, Query3D[] qs) {
        int m = qs.length;
        Point3D[][] sub = new Point3D[8][m];
        long[] ans = new long[m];
        long[] xs = new long[2];
        long[] ys = new long[2];
        long[] zs = new long[2];
        for (int i = 0; i < m; i++) {
            xs[0] = qs[i].xr;
            xs[1] = qs[i].xl - 1;
            ys[0] = qs[i].yr;
            ys[1] = qs[i].yl - 1;
            zs[0] = qs[i].zr;
            zs[1] = qs[i].zl - 1;
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    for (int t = 0; t < 2; t++) {
                        sub[(j << 2) | (k << 1) | t][i] = new Point3D(xs[j], ys[k], zs[t], 0);
                    }
                }
            }
        }
        prefixSum(SequenceUtils.pack(i -> new Point3D[i], sub[0], sub[1], sub[2], sub[3], sub[4], sub[5], sub[6], sub[7], pts));
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 8; j++) {
                ans[i] += sign(j) * sub[j][i].sum;
            }
        }
        return ans;
    }

    public static void prefixSum(Point3D[] pts) {
        Arrays.sort(pts, (a, b) -> {
            int cmp = Long.compare(a.x, b.x);
            if (cmp == 0) {
                cmp = Long.compare(a.y, b.y);
            }
            if (cmp == 0) {
                cmp = Long.compare(a.z, b.z);
            }
            if (cmp == 0) {
                cmp = -Long.compare(a.w, b.w);
            }
            return cmp;
        });
        LongArrayList list = new LongArrayList(pts.length);
        for (Point3D pt : pts) {
            list.add(pt.z);
        }
        list.unique();
        for (Point3D pt : pts) {
            pt.z = list.binarySearch(pt.z) + 1;
        }
        dac(pts, 0, pts.length - 1, new Point3D[pts.length], new LongBIT(list.size()));
    }

    private static void dac(Point3D[] pts, int l, int r, Point3D[] buf, LongBIT bit) {
        if (l == r) {
            pts[l].sum = pts[l].w;
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        dac(pts, l, m, buf, bit);
        dac(pts, m + 1, r, buf, bit);
        int i = l;
        int j = m + 1;
        int wpos = l;
        while (i <= m || j <= r) {
            if (j > r || i <= m && pts[i].y <= pts[j].y) {
                bit.update((int) pts[i].z, pts[i].w);
                buf[wpos++] = pts[i];
                i++;
            } else {
                pts[j].sum += bit.query((int) pts[j].z);
                buf[wpos++] = pts[j];
                j++;
            }
        }
        for (int t = l; t <= m; t++) {
            bit.update((int) pts[t].z, -pts[t].w);
        }
        assert wpos == r + 1;
        System.arraycopy(buf, l, pts, l, r - l + 1);
    }

    private static void dac(Point2D[] pts, int l, int r, Point2D[] buf) {
        if (l == r) {
            pts[l].sum = pts[l].w;
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        dac(pts, l, m, buf);
        dac(pts, m + 1, r, buf);
        int i = l;
        int j = m + 1;
        long sum = 0;
        int wpos = l;
        while (i <= m || j <= r) {
            if (j > r || i <= m && pts[i].y <= pts[j].y) {
                sum += pts[i].w;
                buf[wpos++] = pts[i];
                i++;
            } else {
                pts[j].sum += sum;
                buf[wpos++] = pts[j];
                j++;
            }
        }
        assert wpos == r + 1;
        System.arraycopy(buf, l, pts, l, r - l + 1);
    }

    public static class Point2D {
        public long x;
        public long y;

        public Point2D(long x, long y, long w) {
            this.x = x;
            this.y = y;
            this.w = w;
        }

        long sum;
        long w;
    }

    public static class Point3D extends Point2D {
        public long z;

        public Point3D(long x, long y, long z, long w) {
            super(x, y, w);
            this.z = z;
        }
    }

    public static class Query2D {
        public long xl;
        public long xr;
        public long yl;
        public long yr;

        public Query2D(long xl, long xr, long yl, long yr) {
            assert xl <= xr && yl <= yr;
            this.xl = xl;
            this.xr = xr;
            this.yl = yl;
            this.yr = yr;
        }
    }


    public static class Query3D {
        public long xl;
        public long xr;
        public long yl;
        public long yr;
        public long zl;
        public long zr;

        public Query3D(long xl, long xr, long yl, long yr, long zl, long zr) {
            this.xl = xl;
            this.xr = xr;
            this.yl = yl;
            this.yr = yr;
            this.zl = zl;
            this.zr = zr;
        }

        public long ans;
        private Point3D[] sub;
    }
}
