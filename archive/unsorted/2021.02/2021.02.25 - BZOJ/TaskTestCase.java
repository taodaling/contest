package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class TaskTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
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
        int n = random.nextInt(1, 100);
        int a = random.nextInt(0, 100);
        int b = random.nextInt(0, 100);
        int c = random.nextInt(1, 100);
        long[] ans = solve(n, a, b, c);
        StringBuilder in = new StringBuilder();
        printLine(in, n, a, b, c);
        StringBuilder out = new StringBuilder();
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public long[] solve(int n, long a, long b, long c) {
        long f = 0;
        long g = 0;
        long h = 0;
        for (int i = 0; i <= n; i++) {
            long v = (i * a + b) / c;
            f += v;
            g += i * v;
            h += v * v;
        }
        return new long[]{f, g, h};
    }
}
