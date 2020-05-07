package on2020_05.on2020_05_07_Codeforces___Codeforces_Round__639__Div__1_.D__R_sum__Review0;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class DRsumReviewTestCase {
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
        int n = random.nextInt(1, testNum);
        int[] a = new int[n];
        int sum = 0;
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(0, testNum);
            sum += a[i];
        }
        int k = random.nextInt(0, sum);
        long ans = solve(a, k);
        StringBuilder in = new StringBuilder();
        printLine(in, n, k);
        for (int i = 0; i < n; i++) {
            in.append(a[i]).append(' ');
        }
        return new Test(in.toString(), "" + ans);
    }

    public long solve(int[] a, int k) {
        int n = a.length;
        long[][] dp = new long[n + 1][k + 1];
        long inf = (long) 1e18;
        SequenceUtils.deepFill(dp, -inf);
        dp[0][0] = 0;
        for (int i = 1; i <= n; i++) {
            int v = a[i - 1];
            for (int j = 0; j <= k; j++) {
                for (int t = 0; t <= v && t <= j; t++) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - t] + t * (v - (long) t * t));
                }
            }
        }

        return dp[n][k];
    }
}
