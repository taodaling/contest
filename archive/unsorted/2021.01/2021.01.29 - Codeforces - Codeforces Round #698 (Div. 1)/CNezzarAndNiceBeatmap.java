package contest;

import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class CNezzarAndNiceBeatmap {
    public boolean valid(IntegerPoint2 a, IntegerPoint2 b, IntegerPoint2 c) {
        IntegerPoint2 z = IntegerPoint2.plus(IntegerPoint2.minus(b, a).perpendicular(), b);
        return IntegerPoint2.cross(b, z, c) > 0;
    }

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerPoint2[] pts = new IntegerPoint2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new IntegerPoint2(in.ri(), in.ri());
        }
        int[] order = new int[n];
        order[0] = 0;
        order[1] = 1;
        for (int i = 2; i < n; i++) {
            IntegerPoint2 pt = pts[i];
            int j = i;
            while (j >= 2) {
                if (valid(pts[order[j - 2]], pts[order[j - 1]], pt)) {
                    break;
                }
                order[j] = order[j - 1];
                assert valid(pts[order[j - 2]], pt, pts[order[j]]);
                j--;
            }
            order[j] = i;
            debug.debugArray("order", order);
        }
        for(int x : order){
            out.append(x + 1).append(' ');
        }
    }
}
