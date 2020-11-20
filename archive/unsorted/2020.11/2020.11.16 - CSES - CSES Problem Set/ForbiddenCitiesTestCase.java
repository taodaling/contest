package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.datastructure.DSU;
import template.rand.RandomWrapper;

public class ForbiddenCitiesTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private <T> void printLineObj(StringBuilder builder, T... vals) {
        for (T val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, int... vals) {
        for (int val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }


    private void printLine(StringBuilder builder, long... vals) {
        for (long val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 5);
        int m = random.nextInt(1, 5);
        int q = random.nextInt(1, 5);
        int[][] edges = new int[m][2];
        for (int i = 0; i < m; i++) {
            edges[i][0] = random.nextInt(1, n);
            edges[i][1] = random.nextInt(1, n);
        }
        int[][] qs = new int[q][3];
        for (int i = 0; i < q; i++) {
            qs[i][0] = random.nextInt(1, n);
            qs[i][1] = random.nextInt(1, n);
            qs[i][2] = random.nextInt(1, n);
        }

        String ans = solve(n, edges, qs);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m, q);
        for (int[] e : edges) {
            printLine(in, e);
        }
        for (int[] query : qs) {
            printLine(in, query);
        }
        return new Test(in.toString(), ans);
    }

    public String solve(int n, int[][] edges, int[][] qs) {
        DSU dsu = new DSU(n);
        StringBuilder ans = new StringBuilder();
        for (int[] q : qs) {
            int a = q[0];
            int b = q[1];
            int c = q[2];
            dsu.init();
            for (int[] e : edges) {
                if (e[0] == c || e[1] == c) {
                    continue;
                }
                dsu.merge(e[0] - 1, e[1] - 1);
            }
            if (dsu.find(a - 1) == dsu.find(b - 1) && a != c && b != c) {
                ans.append("YES");
            } else {
                ans.append("NO");
            }
            ans.append('\n');
        }
        return ans.toString();
    }
}
