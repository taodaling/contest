package on2021_04.on2021_04_28_Codeforces___Codeforces_Round__194__Div__1_.E__Summer_Earnings0;



import template.datastructure.MinQueue;
import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.LongComparator;
import template.primitve.generated.datastructure.LongMinQueue;
import template.utils.GeoConstant;

import java.util.Arrays;
import java.util.Comparator;

public class ESummerEarnings {
    public double between(Pt a, Pt b, boolean next) {
        double res = a.angle - b.angle;
        if (next) {
            res += Math.PI * 2;
        }
        return res;
    }


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Pt[] pts = new Pt[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Pt(in.ri(), in.ri());
        }
        Pt[] sorted = new Pt[n - 1];
        MinQueue<Pt> mq = new MinQueue<>(n, Comparator.comparingLong(x -> -x.d2));
        double atleast = Math.PI / 3;
        long best = 0;
        for (int i = 0; i < n; i++) {
            int wpos = 0;
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                sorted[wpos++] = pts[j];
            }
            assert wpos == n - 1;
            for (int j = 0; j < n - 1; j++) {
                Pt pt = sorted[j];
                pt.angle = GeoConstant.theta(pt.x - pts[i].x, pt.y - pts[i].y);
                pt.d2 = IntegerPoint2.dist2(pt, pts[i]);
            }
            Arrays.sort(sorted, 0, n - 1, Comparator.comparingDouble(x -> x.angle));
            mq.clear();
            for (int j = 0, iter = 0; j < n - 1; j++) {
                while (iter < j + n - 1 && between(sorted[iter % (n - 1)], sorted[j], iter >= n - 1) <= Math.PI) {
                    sorted[iter % (n - 1)].next = iter >= n - 1;
                    mq.add(sorted[iter % (n - 1)]);
                    iter++;
                }
                while (!mq.isEmpty() && between(mq.first(), sorted[j], mq.first().next) < atleast) {
                    mq.removeFirst();
                }
                if (!mq.isEmpty()) {
                    best = Math.max(best, Math.min(sorted[j].d2, mq.query().d2));
                }
            }
        }

        double ans = Math.sqrt(best);
        out.println(ans / 2);
    }
}

class Pt extends IntegerPoint2 {
    double angle;
    boolean next;
    long d2;

    public Pt(long x, long y) {
        super(x, y);
    }
}