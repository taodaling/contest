package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class RandomWalkQueriesTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
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

    private <T> void printLineObj(StringBuilder builder, T... vals) {
        for (T val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = (int) 1;
        int q = (int) 1e5;
        int[][] edges = new int[n - 1][];
        for (int i = 0; i < n - 1; i++) {
            edges[i] = new int[]{random.nextInt(1, i + 1), i + 2};
        }
        int[][] qs = new int[q][];
        for (int i = 0; i < q; i++) {
            int t = random.nextInt(1, 2);
            if (t == 1) {
                qs[i] = new int[]{t, random.nextInt(1, n), random.nextInt(1, n)};
            } else {
                qs[i] = new int[]{t, random.nextInt(1, n)};
            }
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n, q);
        for (int[] e : edges) {
            printLine(in, e);
        }
        for (int[] query : qs) {
            printLine(in, query);
        }
        return new Test(in.toString(), null);
    }
}
