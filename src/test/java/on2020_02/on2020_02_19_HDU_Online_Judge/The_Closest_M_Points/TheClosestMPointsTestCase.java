package on2020_02.on2020_02_19_HDU_Online_Judge.The_Closest_M_Points;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class TheClosestMPointsTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, Object... vals) {
        for (Object val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    private static class Point {
        public Point(long[] pt) {
            this.pt = pt;
        }

        long[] pt;
        long dist;

        @Override
        public int hashCode() {
            return Arrays.hashCode(pt);
        }

        @Override
        public boolean equals(Object obj) {
            Point other = (Point) obj;
            return SequenceUtils.equal(pt, other.pt);
        }
    }

    public Test create(int testNum) {
        int n = random.nextInt(1, 5);
        int d = random.nextInt(1, 5);
        Set<Point> set = new HashSet<>();
        while (set.size() < n) {
            long[] pt = new long[d];
            for (int i = 0; i < d; i++) {
                pt[i] = random.nextInt(-10, 10);
            }
            set.add(new Point(pt));
        }

        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n, d);
        for (Point pt : set) {
            for (int j = 0; j < d; j++) {
                in.append(pt.pt[j]).append(' ');
            }
            in.append('\n');
        }

        int q = random.nextInt(1, 5);
        printLine(in, q);
        for (int i = 0; i < q; i++) {
            long[] pt = new long[d];
            for (int j = 0; j < d; j++) {
                pt[j] = random.nextInt(-10, 10);
            }
            int m = random.nextInt(1, n);
            Point[] all = set.toArray(new Point[0]);
            Arrays.sort(all, (a, b) -> Long.compare(dist(a.pt, pt), dist(b.pt, pt)));
            boolean valid = true;
            for (int j = 1; j <= m && j < n; j++) {
                valid = valid && dist(all[j - 1].pt, pt) != dist(all[j].pt, pt);
            }
            if (!valid) {
                i--;
                continue;
            }
            for (int k = 0; k < d; k++) {
                in.append(pt[k]).append(' ');
            }
            in.append('\n');
            in.append(m).append('\n');

            printLine(out, "the closest ", m, " points are:");
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < d; k++) {
                    out.append(all[j].pt[k]).append(' ');
                }
                out.append('\n');
            }
        }

        return new Test(in.toString(), out.toString());
    }

    public long dist(long[] a, long[] b) {
        long sum = 0;
        for (int i = 0; i < a.length; i++) {
            long d = a[i] - b[i];
            sum += d * d;
        }
        return sum;
    }
}
