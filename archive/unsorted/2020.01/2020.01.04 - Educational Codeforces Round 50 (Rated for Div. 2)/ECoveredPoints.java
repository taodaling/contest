package contest;

import template.geometry.Point2D;
import template.geometry.Segment2D;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitBase;
import template.math.DigitUtils;
import template.math.GCDs;
import template.primitve.generated.LongHashSet;
import template.rand.Randomized;

public class ECoveredPoints {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        //Segment2D[] segs = new Segment2D[n];
        int[][] segs = new int[n][4];
        for (int i = 0; i < n; i++) {
            //Point2D a = new Point2D(in.readInt(), in.readInt());
            //Point2D b = new Point2D(in.readInt(), in.readInt());
            //segs[i] = new Segment2D(a, b);
            for (int j = 0; j < 4; j++) {
                segs[i][j] = in.readInt();
            }
        }

        long ans = 0;
//        for (Segment2D seg : segs) {
//            int a = DigitUtils.roundToInt(Math.abs(seg.a.x - seg.b.x));
//            int b = DigitUtils.roundToInt(Math.abs(seg.a.y - seg.b.y));
//            if (a == 0) {
//                ans += b + 1;
//            } else if (b == 0) {
//                ans += a + 1;
//            } else {
//                int g = GCDs.gcd(a, b);
//                ans += 1 + g;
//            }
//        }

        for (int[] seg : segs) {
            long a = Math.abs(seg[0] - seg[2]);
            long b = Math.abs(seg[1] - seg[3]);
            long g = GCDs.gcd(a, b);
            ans += g + 1;
        }

        LongHashSet set = new LongHashSet(n, false);
        //Randomized.randomizedArray(segs, 0, n - 1);
        for (int i = 0; i < n; i++) {
            set.clear();
            for (int j = i + 1; j < n; j++) {
                long[] pos;
                if (!isCrossOrTouchIntersect(segs[i][0], segs[i][1], segs[i][2], segs[i][3],
                        segs[j][0], segs[j][1], segs[j][2], segs[j][3]) ||
                        (pos = intersect(segs[i][0], segs[i][1], segs[i][2], segs[i][3],
                                segs[j][0], segs[j][1], segs[j][2], segs[j][3])) == null){
                    continue;
                }

                long key = DigitUtils.asLong((int)pos[0], (int)pos[1]);
                set.add(key);
            }
            ans -= set.size();
        }

        out.println(ans);
    }

    static boolean isCrossOrTouchIntersect(long x1, long y1, long x2, long y2, long x3, long y3, long x4, long y4) {
        if (Math.max(x1, x2) < Math.min(x3, x4) || Math.max(x3, x4) < Math.min(x1, x2)
                || Math.max(y1, y2) < Math.min(y3, y4) || Math.max(y3, y4) < Math.min(y1, y2))
            return false;
        long z1 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
        long z2 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1);
        long z3 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
        long z4 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3);
        return (z1 <= 0 || z2 <= 0) && (z1 >= 0 || z2 >= 0) && (z3 <= 0 || z4 <= 0) && (z3 >= 0 || z4 >= 0);
    }

    static long[] intersect(long x1, long y1, long x2, long y2, long x3, long y3, long x4, long y4) {
        long a1 = y2 - y1;
        long b1 = x1 - x2;
        long c1 = -(x1 * y2 - x2 * y1);
        long a2 = y4 - y3;
        long b2 = x3 - x4;
        long c2 = -(x3 * y4 - x4 * y3);
        long det = a1 * b2 - a2 * b1;
        if (det == 0)
            return null;
        if ((c1 * b2 - c2 * b1) % det == 0 && (a1 * c2 - a2 * c1) % det == 0) {
            return new long[]{-(c1 * b2 - c2 * b1) / det, -(a1 * c2 - a2 * c1) / det};
        }
        return null;
    }
}
