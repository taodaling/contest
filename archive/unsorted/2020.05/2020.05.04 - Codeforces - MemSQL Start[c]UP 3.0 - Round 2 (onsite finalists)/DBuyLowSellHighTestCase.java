package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class DBuyLowSellHighTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
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
        int n = random.nextInt(1, 5);
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = random.nextInt(1, 10);
        }

        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for (int i = 0; i < n; i++) {
            in.append(p[i]).append(' ');
        }

        return new Test(in.toString(), "" + solve(p));
    }

    public long solve(int[] p) {
        int n = p.length;
        long[][] dp = new long[n + 1][n + 1];
        long inf = (long) 1e18;
        SequenceUtils.deepFill(dp, -inf);
        dp[0][0] = 0;
        for (int i = 1; i <= n; i++) {
            int price = p[i - 1];
            for (int j = 0; j <= n; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j > 0) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - 1] - price);
                }
                if (j + 1 <= n) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j + 1] + price);
                }
            }
        }
        return dp[n][0];
    }


}
