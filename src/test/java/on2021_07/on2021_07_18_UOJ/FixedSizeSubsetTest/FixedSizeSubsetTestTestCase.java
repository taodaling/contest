package on2021_07.on2021_07_18_UOJ.FixedSizeSubsetTest;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.binary.Log2;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FixedSizeSubsetTestTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
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
        int n = random.nextInt(1, 2000);
        int k = random.nextInt(1, Integer.bitCount(n));
        int[] weights = new int[32];
        for (int i = 0; i < 32; i++) {
            weights[i] = random.nextInt(0, 100);
        }
        String out = solve(n, k, weights);
        StringBuilder in = new StringBuilder();
        printLine(in, n, k);
        printLine(in, weights);
        return new Test(in.toString(), out);
    }

    public void dfs(int i, int n, int k, int mask, int sum, int[] weights, List<int[]> ans) {
        if (k < 0) {
            return;
        }
        if (i < 0) {
            if (k == 0) {
                ans.add(new int[]{mask, sum});
            }
            return;
        }
        dfs(i - 1, n, k, mask, sum, weights, ans);
        if (Bits.get(n, i) == 1) {
            dfs(i - 1, n, k - 1, mask | (1 << i), sum + weights[i], weights, ans);
        }
    }

    public String solve(int n, int k, int[] weights) {
        int log = Log2.floorLog(n);
        List<int[]> ans = new ArrayList<>();
        dfs(log, n, k, 0, 0, weights, ans);
        StringBuilder in = new StringBuilder();
        for(int[] x : ans){
            printLine(in, x);
        }
        return in.toString();
    }
}
