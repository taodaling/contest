package on2021_09.on2021_09_27_AtCoder___AtCoder_Beginner_Contest_220.G___Isosceles_Trapezium;



import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.GCDs;
import template.string.FastHash;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GIsoscelesTrapezium {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        PointExt[] pts = new PointExt[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new PointExt(in.ri() * 2, in.ri() * 2, in.ri());
        }
        List<Line> lines = new ArrayList<>(n * n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                lines.add(makeLine(pts[i], pts[j]));
            }
        }
        Map<Long, List<Line>> groupByHash = lines.stream().collect(Collectors.groupingBy(x -> x.hash));
        long best = -1;
        Comparator<Line> sortHorizontalLines = (a, b) -> {
            return IntegerPoint2.orient(a.a, a.b, b.a);
        };
        long inf = (long) 1e18;
        for (List<Line> list : groupByHash.values()) {
            list.sort(sortHorizontalLines);
            long largest = -inf;
            for (int i = 0; i < list.size(); i++) {
                int l = i;
                int r = i;
                while (r + 1 < list.size() && sortHorizontalLines.compare(list.get(r + 1), list.get(l)) == 0) {
                    r++;
                }
                i = r;

                long localLargest = -inf;
                for (int j = l; j <= r; j++) {
                    localLargest = Math.max(localLargest, list.get(j).weight());
                }
                best = Math.max(best, localLargest + largest);
                largest = Math.max(localLargest, largest);
            }
        }
        out.println(best);

    }

    FastHash fh = new FastHash();

    public long[] move(long x, long y, long mx, long my) {
        if (mx < 0) {
            x = -x;
            mx = -mx;
        }
        if (my < 0) {
            y = -y;
            my = -my;
        }

        long k;
        long[] ans = new long[2];
        if (mx != 0) {
            //x + kmx >= 0
            // k >= -x / mx
            k = DigitUtils.ceilDiv(-x, mx);
        } else {
            //y + kmy >= 0
            k = DigitUtils.ceilDiv(-y, my);
        }
        ans[0] = x + k * mx;
        ans[1] = y + k * my;

        return ans;
    }

    public Line makeLine(PointExt a, PointExt b) {
        if (IntegerPoint2.SORT_BY_XY.compare(a, b) < 0) {
            PointExt tmp = a;
            a = b;
            b = tmp;
        }
        long dx = a.x - b.x;
        long dy = a.y - b.y;
        long g = GCDs.gcd(Math.abs(dx), Math.abs(dy));
        dx /= g;
        dy /= g;

        long mx = -dy;
        long my = dx;
        Line line = new Line();
        line.a = a;
        line.b = b;
        long[] moved = move((a.x + b.x) / 2, (a.y + b.y) / 2, mx, my);
        line.hash = fh.hash(DigitUtils.highBit(mx), DigitUtils.lowBit(mx),
                DigitUtils.highBit(my), DigitUtils.lowBit(my),
                DigitUtils.highBit(moved[0]),
                DigitUtils.lowBit(moved[0]),
                DigitUtils.highBit(moved[1]),
                DigitUtils.lowBit(moved[1]));
        return line;
    }
}

class Line {
    PointExt a;
    PointExt b;
    long hash;

    public long weight() {
        return a.w + b.w;
    }
}

class PointExt extends IntegerPoint2 {
    long w;

    public PointExt(long x, long y, long w) {
        super(x, y);
        this.w = w;
    }
}
