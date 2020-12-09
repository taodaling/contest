package contest;

import chelper.ExternalExecutor;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class DEqualityTestCase {
    @TestCase
    public Collection<Test> createTests() {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 3);
        int x = 1;
        int y = 1;
        int[] a = getInterval(n);
        int[] b = getInterval(n);

        StringBuilder in = new StringBuilder();
        printLine(in, n);
        printLine(in, x);
        printLine(in, a);
        printLine(in, y);
        printLine(in, b);

        String ans = "" + solve(n, Arrays.asList(a), Arrays.asList(b));
        return new Test(in.toString(), ans);
    }

    public boolean contain(int[] interval, int x) {
        return interval[0] <= x && x <= interval[1];
    }

    public int solve(int n, List<int[]> a, List<int[]> b) {
        int ans = 0;
        for (int i = 1; i <= n; i++) {
            boolean valid = true;
            for (int j = i; j <= n; j += 2 * i) {
                boolean find = false;
                for (int[] x : a) {
                    if (contain(x, j)) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    valid = false;
                }
            }

            for (int j = i * 2; j <= n; j += 2 * i) {
                boolean find = false;
                for (int[] x : b) {
                    if (contain(x, j)) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    valid = false;
                }
            }
            if (valid) {
                ans++;
            }
        }
        return ans;
    }

    public int[] getInterval(int n) {
        int[] ans = new int[]{random.nextInt(1, n), random.nextInt(1, n)};
        if (ans[0] > ans[1]) {
            SequenceUtils.swap(ans, 0, 1);
        }
        return ans;
    }
}
