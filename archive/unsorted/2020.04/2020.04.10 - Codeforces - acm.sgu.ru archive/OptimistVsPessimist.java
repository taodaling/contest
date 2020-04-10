package contest;

import template.geometry.geo2.IntegerPoint2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;

import java.util.Arrays;

public class OptimistVsPessimist {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        double[] areas = new double[n];
        int inf = (int) 1e8;
        for (int i = 0; i < n; i++) {
            double l = inf;
            double r = -inf;
            double b = inf;
            double t = -inf;
            for (int j = 0; j < 4; j++) {
                double x = in.readInt();
                double y = in.readInt();
                l = Math.min(l, x);
                r = Math.max(r, x);
                b = Math.min(b, y);
                t = Math.max(t, y);
            }

            Point2 center = new Point2((l + r) / 2.0, (b + t) / 2.0);
            Point2 c1 = new Point2(in.readInt(), in.readInt());
            Point2 c2 = new Point2(in.readInt(), in.readInt());
            if (Point2.onSegment(center, c1, c2) || Point2.onSegment(center, c2, c1)) {
                areas[i] = 0;
            } else {
                areas[i] = (r - l) * (t - b) / 2.0;
            }
        }

        Arrays.sort(areas);
        double max = 0;
        double min = 0;
        for (int i = 0; i < k; i++) {
            min += areas[i];
            max += areas[n - 1 - i];
        }
        out.append(min).append(' ').append(max);
    }
}
