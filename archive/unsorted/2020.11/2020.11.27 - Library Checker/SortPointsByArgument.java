package contest;

import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortPointsByArgument {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        IntegerPoint2[] pts = new IntegerPoint2[n];
        int inf = (int) 2e9;
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            if (x == 0 && y == 0) {
                x = inf;
            }
            pts[i] = new IntegerPoint2(x, y);
        }
        Arrays.sort(pts, IntegerPoint2.SORT_BY_POLAR_ANGLE);
        for (IntegerPoint2 pt : pts) {
            long x = pt.x;
            long y = pt.y;
            if (x == inf) {
                x = 0;
            }
            out.append(x).append(' ').append(y).println();
        }
    }
}
