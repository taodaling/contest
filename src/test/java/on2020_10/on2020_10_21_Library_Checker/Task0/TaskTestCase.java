package on2020_10.on2020_10_21_Library_Checker.Task0;





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
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
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
        int n = random.nextInt(1, 20);
        int[] a = new int[n];
        int[] b = new int[n];
        int[] w = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(0, n - 1);
            b[i] = random.nextInt(0, n - 1);
            w[i] = random.nextInt(-10, 10);
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        printLine(in, a);
        printLine(in, b);
        printLine(in, w);
        return new Test(in.toString(), null);
    }

}
