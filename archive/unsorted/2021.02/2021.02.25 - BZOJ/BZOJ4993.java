package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerGenericBIT;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BZOJ4993 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] A = new int[n];
        int[] B = new int[n];
        in.populate(A);
        in.populate(B);
        List<Point> pts = new ArrayList<>(n * n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (Math.abs(A[i] - B[j]) <= 4) {
                    pts.add(new Point(i + 1, j + 1));
                }
            }
        }
        pts.sort(Comparator.<Point>comparingInt(x -> x.x).thenComparingInt(x -> -x.y));
        IntegerGenericBIT bit = new IntegerGenericBIT(n, Math::max, 0);
        for (Point pt : pts) {
            int best = bit.query(pt.y - 1) + 1;
            bit.update(pt.y, best);
        }
        int ans = bit.query(n);
        out.println(ans);
    }
}

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}