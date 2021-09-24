package on2021_08.on2021_08_01_DMOJ___An_Animal_Contest_3.P7___Monkey_Lasers;



import chelper.ExternalExecutor;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class P7MonkeyLasersTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
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

    private <T> void printLineObj(StringBuilder builder, T... vals) {
        for (T val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 100);
        int m = random.nextInt(2, 100);
        int[] c = new int[n];
        for (int i = 0; i < n; i++) {
            c[i] = random.nextInt(1, m);
        }
        char[] dirs = new char[n];
        for (int i = 0; i < n; i++) {
            dirs[i] = random.range('L', 'R');
        }
        int[] a = new int[n + 1];
        for (int i = 0; i < n + 1; i++) {
            a[i] = random.nextInt(1, 100);
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        printLine(in, c);
        printLineObj(in, new String(dirs));
        printLine(in, a);
        System.out.println(in);
        return new Test(in.toString(), new ExternalExecutor("F:\\geany\\main.exe").invoke(in.toString()));
    }
}
