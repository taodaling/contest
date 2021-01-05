package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class P4897GomoryHuTreeTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 2);
        int m = n - 1 + random.nextInt(1, 3);
        List<int[]> edges = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            edges.add(SequenceUtils.wrapArray(random.nextInt(0, i - 1), i, random.nextInt(0, 10)));
        }
        for (int i = n + 1; i <= m; i++) {
            int u = 0;
            int v = 0;
            while (u == v) {
                u = random.nextInt(0, n);
                v = random.nextInt(0, n);
            }
            edges.add(SequenceUtils.wrapArray(u, v, random.nextInt(0, 10)));
        }

        List<int[]> qs = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                if (i != j) {
                    qs.add(new int[]{i, j});
                }
            }
        }

        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for (int[] e : edges) {
            printLine(in, e[0], e[1], e[2]);
        }
        printLine(in, qs.size());
        for (int[] q : qs) {
            printLine(in, q[0], q[1]);
        }

        long[] ans = solve(n, edges, qs);
        StringBuilder out = new StringBuilder();
        for (long x : ans) {
            printLine(out, x);
        }
        return new Test(in.toString(), out.toString());
    }

    long solve(int n, List<int[]> edges, int u, int v) {
        LongHLPP hlpp = new LongHLPP(n + 1, u, v);
        for (int[] e : edges) {
            hlpp.addEdge(e[0], e[1], e[2], false);
        }
        return hlpp.calc();
    }

    long[] solve(int n, List<int[]> edges, List<int[]> qs) {
        long[] ans = qs.stream().mapToLong(x -> solve(n, edges, x[0], x[1])).toArray();
        return ans;
    }

}
