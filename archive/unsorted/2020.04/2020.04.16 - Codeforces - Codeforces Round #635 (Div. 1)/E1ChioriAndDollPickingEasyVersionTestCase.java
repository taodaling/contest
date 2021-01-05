package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class E1ChioriAndDollPickingEasyVersionTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 2);
        int m = random.nextInt(1, 10);
        long[] vals = new long[n];
        for (int i = 0; i < n; i++) {
            vals[i] = random.nextLong(0, (1L << m) - 1);
        }
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n, m);
        for (long v : vals) {
            in.append(v).append(' ');
        }
        for (long v : solve(vals, m)) {
            out.append(v).append(' ');
        }
        return new Test(in.toString(), out.toString());
    }

    private void dfs(int[] cnt, long[] vals, int i, long xor) {
        if (i < 0) {
            cnt[Long.bitCount(xor)]++;
            return;
        }
        dfs(cnt, vals, i - 1, xor);
        dfs(cnt, vals, i - 1, xor ^ vals[i]);
    }

    public int[] solve(long[] vals, int m) {
        int[] ans = new int[m + 1];
        dfs(ans, vals, vals.length - 1, 0);
        return ans;
    }


}
