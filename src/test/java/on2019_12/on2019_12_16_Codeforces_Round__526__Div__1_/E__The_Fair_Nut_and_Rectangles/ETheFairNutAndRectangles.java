package on2019_12.on2019_12_16_Codeforces_Round__526__Div__1_.E__The_Fair_Nut_and_Rectangles;



import template.algo.GeqSlopeOptimizer;
import template.geometry.LongConvexHullTrick;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ETheFairNutAndRectangles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Rect[] rects = new Rect[n + 1];
        for (int i = 1; i <= n; i++) {
            rects[i] = new Rect();
            rects[i].x = in.readInt();
            rects[i].y = in.readInt();
            rects[i].a = in.readLong();
        }
        rects[0] = new Rect();
        rects[0].x = 0;
        rects[0].y = Integer.MAX_VALUE;
        rects[0].a = 0;
        Arrays.sort(rects, (a, b) -> a.x - b.x);

        long[] dp = new long[n + 1];
        LongConvexHullTrick cht = new LongConvexHullTrick();
        cht.insert(0, 0);
        for(int i = 1; i <= n; i++){
            dp[i] = cht.query(rects[i].y) + rects[i].x * (long)rects[i].y - rects[i].a;
            cht.insert(-rects[i].x, dp[i]);
        }
        
        long max = 0;
        for (int i = 1; i <= n; i++) {
            max = Math.max(max, dp[i]);
        }

        out.println(max);
    }
}

class Rect {
    int x;
    int y;
    long a;
}
