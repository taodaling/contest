package contest;

import net.egork.chelper.task.NewTopCoderTest;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.*;

public class IncreasingJumpsDiv1TestCase {
    @TestCase
    public Collection<NewTopCoderTest> createTests() {
        List<NewTopCoderTest> tests = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
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

    public NewTopCoderTest create(int testNum) {
        int[] x = new int[50];
        for (int i = 0; i < 50; i++) {
            x[i] = random.nextInt(0, 1) * 2500;
        }
        return new NewTopCoderTest(new Object[]{x}, null);
    }
}
