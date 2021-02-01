package contest;





import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.rand.Randomized;

public class TaskTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
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
        int n = random.nextInt(500,  500);
        List<int[]> edges = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            int p = random.nextInt(0, i - 1);
            edges.add(new int[]{p, i});
        }
        for (int i = 1; i < n; i++) {
            int p = random.nextInt(0, i - 1);
            edges.add(new int[]{p, i});
        }
        for (int i = 1; i < n; i++) {
            int p = random.nextInt(0, i - 1);
            edges.add(new int[]{p, i});
        }
        int m = 100;//random.nextInt(64 * 64 / 2, 64 * 64 / 2);
        for (int i = 0; i < m; i++) {
            edges.add(new int[]{random.nextInt(0, n - 1), random.nextInt(0, n - 1)});
        }
        Randomized.shuffle(edges);
        StringBuilder in = new StringBuilder();
        printLine(in, n, edges.size());
        for (int[] e : edges) {
            printLine(in, e);
        }
        return new Test(in.toString(), null);
    }
}
