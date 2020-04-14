package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.datastructure.DSU;
import template.rand.RandomWrapper;

public class MSTTestTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
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

    public Test create(int testNum) {
        int n = random.nextInt(1, 1000);
        long[] x = new long[n];
        long[] y = new long[n];
        TreeSet<long[]> pairs = new TreeSet<>((a, b) -> a[0] == b[0] ? Long.compare(a[1], b[1]) :
                Long.compare(a[0], b[0]));
        for (int i = 0; i < n; i++) {
            long cx = random.nextInt(1, n);
            long cy = random.nextInt(1, n);
            if (pairs.contains(new long[]{cx, cy})) {
                i--;
                continue;
            }
            pairs.add(new long[]{cx, cy});
            x[i] = cx;
            y[i] = cy;
        }

        long ans = solve(x, y);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for (int i = 0; i < n; i++) {
            printLine(in, x[i], y[i]);
        }
        return new Test(in.toString(), "" + ans);
    }

    public long dist(long x1, long y1, long x2, long y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public long solve(long[] x, long[] y) {
        int n = x.length;
        List<Edge> list = new ArrayList<>(n * n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                Edge edge = new Edge();
                edge.a = i;
                edge.b = j;
                edge.w = dist(x[i], y[i], x[j], y[j]);
                list.add(edge);
            }
        }

        long weight = 0;
        List<Edge> ans = new ArrayList<>();
        DSU dsu = new DSU(n);
        list.sort((a, b) -> Long.compare(a.w, b.w));
        for (Edge e : list) {
            if (dsu.find(e.a) == dsu.find(e.b)) {
                continue;
            }
            dsu.merge(e.a, e.b);
            ans.add(e);
            weight += e.w;
        }
        return weight;
    }

    static class Edge {
        int a;
        int b;
        long w;
    }
}
