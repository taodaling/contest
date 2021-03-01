package contest;

import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CompareUtils;

import java.util.Arrays;
import java.util.Comparator;

public class CConstellation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerPoint2Ext[] pts = new IntegerPoint2Ext[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new IntegerPoint2Ext(in.ri(), in.ri());
            pts[i].id = i;
        }
        pts = CompareUtils.<IntegerPoint2Ext>unique(pts, 0, n - 1, Comparator.<IntegerPoint2Ext>comparingLong(x -> x.x)
                .thenComparingLong(x -> x.y));
        IntegerPoint2[] pair = IntegerPoint2.theNearestPointPair(Arrays.asList(pts));
        IntegerPoint2Ext a = (IntegerPoint2Ext) pair[0];
        IntegerPoint2Ext b = (IntegerPoint2Ext) pair[1];
        IntegerPoint2Ext[] triangle = new IntegerPoint2Ext[3];
        triangle[0] = a;
        triangle[1] = b;
        IntegerPoint2Ext[] bestChoice = null;
        long smallestArea = Long.MAX_VALUE;
        for (IntegerPoint2Ext pt : pts) {
            triangle[2] = pt;
            long area = Math.abs(IntegerPoint2.area2(triangle));
            if (area > 0 && area < smallestArea) {
                smallestArea = area;
                bestChoice = triangle.clone();
            }
        }
        for (IntegerPoint2Ext pt : bestChoice) {
            out.append(pt.id + 1).append(' ');
        }
    }
}

class IntegerPoint2Ext extends IntegerPoint2 {
    int id;

    public IntegerPoint2Ext(long x, long y) {
        super(x, y);
    }

    public IntegerPoint2Ext() {
    }
}