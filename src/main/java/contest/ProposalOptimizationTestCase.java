package contest;

import net.egork.chelper.task.NewTopCoderTest;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.*;

public class ProposalOptimizationTestCase {
    @TestCase
    public Collection<NewTopCoderTest> createTests() {
        List<NewTopCoderTest> tests = new ArrayList<>();
        for (int i = 1; i <= 1; i++) {
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

    public NewTopCoderTest create(int testNum) {
        int R = 50;
        int C = 50;
        int k = (int) 1e9;
        int[] roses = new int[R * C];
        int[] tulips = new int[R * C];
        int[] cost = new int[R * C];
        for (int i = 1; i < R * C - 1; i++) {
            roses[i] = random.nextInt(1, (int) 1e6);
            tulips[i] = random.nextInt(1, (int) 1e6);
            cost[i] = random.nextInt(1, (int) 1e6);
        }

        return new NewTopCoderTest(new Object[]{
                R, C, k, roses, tulips, cost
        }, null);
    }
}
