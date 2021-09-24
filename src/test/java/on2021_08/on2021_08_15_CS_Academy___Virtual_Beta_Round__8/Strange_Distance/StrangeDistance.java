package on2021_08.on2021_08_15_CS_Academy___Virtual_Beta_Round__8.Strange_Distance;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.IntPredicate;

public class StrangeDistance {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long k = in.rl();
        int L = (int) 1e5;
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point();
            pts[i].x = in.ri();
            pts[i].y = in.ri();
        }
        Arrays.sort(pts, Comparator.comparingInt(v -> v.x));
        IntegerBIT bit = new IntegerBIT(L);
        long total = (long) n * (n - 1) / 2;
        IntPredicate pred = m -> {
            long cnt = 0;
            bit.clear();
            for (int i = 0, j = 0; i < n; i++) {
                while (j < n && pts[j].x < pts[i].x - m) {
                    Point head = pts[j++];
                    bit.update(head.y, 1);
                }
                cnt += j - bit.query(pts[i].y - m, pts[i].y + m);
            }
            return total - cnt >= k;
        };
//        pred.test(1);
        int ans = BinarySearch.firstTrue(pred, 0, (int) 1e5);
        out.println(ans);
    }
}

class Point {
    int x;
    int y;
}