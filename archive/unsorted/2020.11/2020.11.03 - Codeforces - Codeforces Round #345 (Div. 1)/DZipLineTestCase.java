package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.algo.LIS;
import template.rand.RandomWrapper;

public class DZipLineTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 10);
        int m = random.nextInt(1, 10);
        int[] a = new int[n];
        int[][] b = new int[m][2];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1, 10);
        }
        for (int i = 0; i < m; i++) {
            b[i][0] = random.nextInt(1, n);
            b[i][1] = random.nextInt(1, 10);
        }

        int[] ans = solve(a, b);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n, m);
        printLine(in, a);
        for(int[] e : b){
            printLine(in, e);
        }
        printLine(out, ans);

        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int[] a, int[][] b) {
        int n = a.length;
        int m = b.length;
        int[] ans = new int[m];
        for (int i = 0; i < m; i++) {
            int j = b[i][0] - 1;
            int v = b[i][1];
            ans[i] = LIS.lisLength(x -> x == j ? v : a[x], n, Comparator.<Integer>naturalOrder());
        }
        return ans;
    }
}
