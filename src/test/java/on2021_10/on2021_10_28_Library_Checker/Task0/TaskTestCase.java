package on2021_10.on2021_10_28_Library_Checker.Task0;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.DigitUtils;
import template.math.Power;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TaskTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
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
        int r = random.nextInt(0, 100);
        int d = random.nextInt(0, 100);
        int n = random.nextInt(1, 100);
        StringBuilder in = new StringBuilder();
        printLineObj(in, r, d, n);
        StringBuilder out = new StringBuilder();
        printLineObj(out, solve(r, d, n));
        return new Test(in.toString(), out.toString());
    }

    public int solve(int r, int d, int n) {
        int mod = 998244353;
        long ans = 0;
        Power power = new Power(mod);
        for (int i = 0; i < n; i++) {
            long contrib = (long) power.pow(r, i) * power.pow(i, d) % mod;
            ans += contrib;
        }
        return DigitUtils.mod(ans, mod);
    }
}
