package on2021_07.on2021_07_27_Codeforces___Codeforces_Round__187__Div__1_.D__Sereja_and_Straight_Lines;



import template.algo.BinarySearch;
import template.datastructure.MinQueue;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.LongPredicate;

public class DSerejaAndStraightLines {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
//        double cos = Math.cos(Math.PI / 4);
//        double sin = Math.sin(Math.PI / 4);
        long[][] pts = new long[n][2];
        for (int i = 0; i < n; i++) {
            long x = in.ri();
            long y = in.ri();
            pts[i][0] = (x + y) * 2;
            pts[i][1] = (x - y) * 2;
        }
        Arrays.sort(pts, Comparator.comparingLong(x -> x[1]));
        debug.debugMatrix("pts", pts);
        MinQueue<long[]> orderByL = new MinQueue<>(n, Comparator.comparingLong(x -> x[0]));
        MinQueue<long[]> orderByR = new MinQueue<>(n, Comparator.comparingLong(x -> -x[0]));
        LongPredicate predicate = m -> {
            orderByL.clear();
            orderByR.clear();
            for (int i = 0; i < n; i++) {
                orderByL.add(pts[i]);
                orderByR.add(pts[i]);
            }
            int iter = 0;
            for (int i = 0; i < n; i++) {
                while (iter < n && pts[iter][1] <= pts[i][1] + m * 2) {
                    iter++;
                    orderByL.removeFirst();
                    orderByR.removeFirst();
                }

                if (orderByL.isEmpty() || orderByR.query()[0] - orderByL.query()[0] <= m * 2) {
                    return true;
                }

                orderByL.add(pts[i]);
                orderByR.add(pts[i]);
            }
            return false;
        };
        double ans = BinarySearch.firstTrue(predicate, 0, (long) 1e10) / 2d;
        out.println(ans);
    }
}

