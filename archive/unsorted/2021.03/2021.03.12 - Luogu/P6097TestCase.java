package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class P6097TestCase {
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

    int mod = (int) 1e9 + 9;

    public Test create(int testNum) {
        int log = random.nextInt(1, 5);
        int[] a = new int[1 << log];
        int[] b = new int[1 << log];
        for (int i = 0; i < a.length; i++) {
            a[i] = random.nextInt(0, mod - 1);
            b[i] = random.nextInt(0, mod - 1);
        }
        long[] ans = dfs(a, b);
        StringBuilder in = new StringBuilder();
        printLine(in, log);
        printLine(in, a);
        printLine(in, b);
        StringBuilder out = new StringBuilder();
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public long[] dfs(int[] a, int[] b) {
        long[] ans = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                if ((i & j) == 0) {
                    ans[i | j] += (long) a[i] * b[j] % mod;
                }
            }
        }
        for (int i = 0; i < ans.length; i++) {
            ans[i] %= mod;
        }
        return ans;
    }
}
