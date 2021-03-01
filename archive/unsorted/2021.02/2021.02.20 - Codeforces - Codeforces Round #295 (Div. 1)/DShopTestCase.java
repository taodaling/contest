package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import chelper.ExternalExecutor;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class DShopTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 0; i++) {
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
        int k = random.nextInt(1, 3);
        int n = random.nextInt(1, 5);
        int m = random.nextInt(0, n);
        int[] a = new int[k];
        int limit = (int) 1e6;
        for (int i = 0; i < k; i++) {
            a[i] = random.nextInt(1, limit);
        }
        int[][] op = new int[n][3];
        for (int i = 0; i < n; i++) {
            op[i][0] = random.nextInt(1, 3);
            op[i][1] = random.nextInt(1, k);
            op[i][2] = random.nextInt(1, limit);
        }

        StringBuilder in = new StringBuilder();
        printLine(in, k, n, m);
        printLine(in, a);
        for (int[] o : op) {
            printLine(in, o);
        }

        String out = new ExternalExecutor("F:\\geany\\main.exe")
                .invoke(in.toString());
        return new Test(in.toString(), out);
    }
}
