package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class DChatteringTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 0; i++) {
            System.out.println("build  testcase " + i);
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

    public Test create(int testNum) {
        int n = random.nextInt(1, 10);
        int[] r = new int[n];
        for (int i = 0; i < n; i++) {
            r[i] = random.nextInt(1, n);
        }

        int[] ans = solve(r);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for (int i = 0; i < r.length; i++) {
            in.append(r[i]).append(' ');
        }

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < n; i++) {
            out.append(ans[i]).append(' ');
        }

        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int[] radius) {
        int n = radius.length;
        int[] ans = new int[n];
        int[] rs = new int[3 * n];
        for (int i = 0; i < 3 * n; i++) {
            rs[i] = radius[i % n];
        }
        for (int i = n; i < 2 * n; i++) {
            int time = 0;
            int l = i;
            int r = i;
            while (r - l + 1 < n) {
                time++;
                int oldL = l;
                int oldR = r;
                for (int j = oldL; j <= oldR; j++) {
                    l = Math.min(l, j - rs[j]);
                    r = Math.max(r, j + rs[j]);
                }
            }
            ans[i - n] = time;
        }
        return ans;
    }
}
