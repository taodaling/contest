package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class EDistanceMatchingTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            System.out.println("build  testcase " + i);
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
        int n = random.nextInt(1, 5) * 2;
        int k = random.nextInt(1, n * n);
        int[] p = new int[n];
        for (int i = 1; i < n; i++) {
            p[i] = random.nextInt(1, i);
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n, k);
        for (int i = 1; i < n; i++) {
            printLine(in, p[i], i + 1);
        }
        return new Test(in.toString(), null);
    }
}
