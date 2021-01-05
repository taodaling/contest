package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class DAnimalsAndPuzzleTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = 1000;
        StringBuilder in = new StringBuilder();
        printLine(in, n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                in.append(1).append(' ');
            }
            printLine(in);
        }
        int m = n * n;
        printLine(in, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                printLine(in, i + 1, j + 1, n, n);
            }
        }

        return new Test(in.toString(), null);
    }
}
