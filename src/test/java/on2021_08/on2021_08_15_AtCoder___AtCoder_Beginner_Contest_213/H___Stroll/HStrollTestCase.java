package on2021_08.on2021_08_15_AtCoder___AtCoder_Beginner_Contest_213.H___Stroll;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HStrollTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
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
        int n = 10;
        int m = 10;
        int T = 40000;
        StringBuilder in = new StringBuilder();
        printLine(in, n, m, T);
        for (int i = 1; i <= m; i++) {
            int a = i;
            int b = i + 1;
            if (b > m) {
                b = 1;
            }
            printLine(in, a, b);
            int[] cost = new int[T];
            for (int j = 0; j < T; j++) {
                cost[j] = random.nextInt(1, 10000);
            }
            printLine(in, cost);
        }
        return new Test(in.toString());
    }
}
