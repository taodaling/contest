package on2021_06.on2021_06_21_DMOJ.DMOPC__20_Contest_7_P3___Senpai_and_Art;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.problem.MinimumSegmentSubtract;
import template.rand.RandomWrapper;

public class DMOPC20Contest7P3SenpaiAndArtTestCase {
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
        int n = random.nextInt(2, 5);
        int l = 0;
        int r = -1;
        while (r - l + 1 < (n + 1) / 2) {
            l = random.nextInt(1, n);
            r = random.nextInt(1, n);
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
        }
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(0, 3);
        }
        long ans = MinimumSegmentSubtract.solve1(n, l, r, i -> a[i]);
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        printLine(in, n, l, r);
        printLine(in, a);
        return new Test(in.toString(), "" + ans);
    }
}
