package on2021_07.on2021_07_05_DMOJ.Canada_Day_Contest_2021___Starfall;



import template.algo.BinarySearch;
import template.geometry.geo2.IntegerRect2;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.function.LongPredicate;

public class CanadaDayContest2021Starfall {
    public IntegerRect2 asCenter(long x, long y, long side) {
        return new IntegerRect2(x - side, y - side, x + side, y + side);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long k = in.rl();
        long[][] pts = new long[n][3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                pts[i][j] = in.rl() * 2;
            }
        }
        LongPredicate predicate = mid -> {
            IntegerRect2 now = asCenter(pts[0][1], pts[0][2], mid);
            for (int i = 1; i < n; i++) {
                long elapse = pts[i][0] - pts[i - 1][0];
                for (int j = 0; j < 2; j++) {
                    now.lb[j] -= elapse * k;
                    now.rt[j] += elapse * k;
                }
                now = IntegerRect2.intersect(now, asCenter(pts[i][1], pts[i][2], mid));
                if (!now.valid()) {
                    return false;
                }
            }
            return true;
        };
        long firstTrue = BinarySearch.firstTrue(predicate, 0, (long) 1e10);
        out.println(firstTrue);
    }
}
