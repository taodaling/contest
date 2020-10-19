package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class P6828ChirpZTransformTestCase {
    @TestCase
    public Collection<Test> createTests() {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = (int) 1e3;
        int m = n;
        int c = random.nextInt(1, (int) 1e8);
        int[] a = new int[n];
        StringBuilder in = new StringBuilder();
        printLine(in, n, c, m);
        for (int i = 0; i < n; i++) {
            in.append(a[i] = random.nextInt(0, (int) 1e8)).append(' ');
        }
        return new Test(in.toString(), null);
    }


}
