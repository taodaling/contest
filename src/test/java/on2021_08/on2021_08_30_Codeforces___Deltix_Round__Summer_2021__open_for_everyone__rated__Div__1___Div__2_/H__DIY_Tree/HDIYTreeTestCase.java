package on2021_08.on2021_08_30_Codeforces___Deltix_Round__Summer_2021__open_for_everyone__rated__Div__1___Div__2_.H__DIY_Tree;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HDIYTreeTestCase {
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
        int n = 50;
        int k = 5;
        int[] cap = new int[k];
        for (int i = 0; i < k; i++) {
            cap[i] = random.nextInt(1, n);
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n, k);
        printLine(in, cap);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                in.append(random.nextInt(1, 100)).append(' ');
            }
            printLine(in);
        }
        return new Test(in.toString());
    }
}
