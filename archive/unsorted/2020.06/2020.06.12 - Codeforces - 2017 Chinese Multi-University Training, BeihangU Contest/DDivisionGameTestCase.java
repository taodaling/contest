package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class DDivisionGameTestCase {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        StringBuilder in = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            printLine(in, 10, 10);
            for (int j = 0; j < 10; j++) {
                printLine(in, j + 1, 10000);
            }
        }

        for (int i = 5; i < 200; i++) {
            printLine(in, 10, 10);
            for (int j = 0; j < 10; j++) {
                printLine(in, j + 1, 1000);
            }
        }

        return new Test(in.toString(), null);
    }
}
