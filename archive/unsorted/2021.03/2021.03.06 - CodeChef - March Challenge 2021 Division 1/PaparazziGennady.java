package contest;

import template.geometry.geo2.IntegerConvexHull2;
import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class PaparazziGennady {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerPoint2[] pts = new IntegerPoint2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new IntegerPoint2(i + 1, in.ri());
        }
       // pts[n] = IntegerPoint2.ORIGIN;
        IntegerPoint2[] ch = IntegerConvexHull2.grahamScan(Arrays.asList(pts), false).toArray(new IntegerPoint2[0]);
        long ans = 0;
        for(int i = 0; i < ch.length; i++){
            IntegerPoint2 cur = ch[i];
            IntegerPoint2 next = ch[i + 1 == ch.length ? 0 : i + 1];
            if(cur == IntegerPoint2.ORIGIN || next == IntegerPoint2.ORIGIN || cur.x <= next.x){
                continue;
            }
            ans = Math.max(ans, Math.abs(cur.x - next.x));
        }
        out.println(ans);

    }
}
