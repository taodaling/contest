package on2021_07.on2021_07_22_Codeforces___Codeforces_Round__552__Div__3_.G__Minimum_Possible_LCM;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.LCMs;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GMinimumPossibleLCMTestCase {
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
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1, 10);
        }
        int[] ans = solve(a);
        ans[0]++;
        ans[1]++;
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n);
        printLine(in, a);
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int[] a) {
        long lcm = (long) 1e18;
        int[] best = new int[2];
        for (int i = 0; i < a.length; i++) {
            for (int j = i + 1; j < a.length; j++) {
                long cand = LCMs.lcm(a[i], a[j]);
                if (cand < lcm) {
                    lcm = cand;
                    best[0] = i;
                    best[1] = j;
                }
            }
        }
        return best;
    }
}
