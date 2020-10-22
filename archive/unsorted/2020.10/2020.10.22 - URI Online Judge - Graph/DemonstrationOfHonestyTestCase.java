package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class DemonstrationOfHonestyTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = 100;
        int m = 10000;
        int k = 200;
        int[][] edges = new int[m][3];
        for(int i = 2; i <= n; i++){
            edges[i - 2][0] = random.nextInt(1, i - 1);
            edges[i - 2][1] = i;
            edges[i - 2][2] = i;
        }
        for (int i = n - 2 + 1; i < m; i++) {
            edges[i][0] = random.nextInt(1, n);
            edges[i][1] = random.nextInt(1, n);
            edges[i][2] = random.nextInt(1, k);
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n, m, k);
        for(int i = 0; i < m; i++){
            printLine(in, edges[i]);
        }
        return new Test(in.toString(), null);
    }
}
