package contest;







import Sjava.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class EBirthdayTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        tests.add(create1(1));
        tests.add(create2(1));
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

    public Test create1(int testNum) {
        StringBuilder builder = new StringBuilder((int) 1e7 + 1000);
        printLine(builder, 1000);
        for (int i = 0; i < 1000; i++) {
            builder.append(random.nextString('a', 'a', 10000));
            printLine(builder);
        }
        return new Test(builder.toString());
    }

    public Test create2(int testNum) {
        StringBuilder builder = new StringBuilder((int) 1e7 + 1000);
        printLine(builder, 10);
        for (int i = 0; i < 10; i++) {
            builder.append(random.nextString('a', 'b', 1000000));
            printLine(builder);
        }
        return new Test(builder.toString());
    }
}
