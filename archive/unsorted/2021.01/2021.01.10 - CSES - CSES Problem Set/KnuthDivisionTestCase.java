package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class KnuthDivisionTestCase {
    @TestCase
    public Collection<Test> createTests() {
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
        int n = random.nextInt(1, 4);
        int[] x = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = random.nextInt(1, 10);
        }
        long ans = solve(n, x);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        printLine(in, x);
        return new Test(in.toString(), "" + ans);
    }

    int[] ps;

    public int interval(int l, int r) {
        int ans = ps[r];
        if (l > 0) {
            ans -= ps[l - 1];
        }
        return ans;
    }

    int[][] dp;

    public int dp(int l, int r) {
        if (dp[l][r] == -1) {
            if (l == r) {
                return dp[l][r] = 0;
            }
            int ans = (int) 1e9;
            for (int i = l; i < r; i++) {
                ans = Math.min(ans, dp(l, i) + dp(i + 1, r));
            }
            ans += interval(l, r);
            dp[l][r] = ans;
        }
        return dp[l][r];
    }

    public long solve(int n, int[] x) {
        ps = new int[n];
        for(int i = 0; i < n; i++){
            ps[i] = x[i];
            if(i > 0){
                ps[i] += ps[i - 1];
            }
        }
        dp = new int[n][n];
        SequenceUtils.deepFill(dp, -1);
        return dp(0, n - 1);
    }
}
