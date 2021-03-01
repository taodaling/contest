package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.task.TestType;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class UTSOpen21P3LatinClassTestCase {
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
        int n = random.nextInt(1, 10);
        int[] h = new int[n];
        for (int i = 0; i < n; i++) {
            h[i] = random.nextInt(1, n);
        }
        long ans = solve(h);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        printLine(in, h);
        return new Test(in.toString(), "" + ans);
    }

    public long solve(int[] h) {
        int mod = (int) 1e9 + 7;
        int n = h.length;
        long[][] dp = new long[n + 1][n + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            int max = 0;
            for (int j = i - 1; j >= 0; j--) {
                max = Math.max(max, h[j]);
                long sum = 0;
                for (int k = 0; k <= max; k++) {
                    sum += dp[j][k];
                }
                dp[i][max] += sum % mod;
            }
            for (int j = 0; j <= n; j++) {
                dp[i][j] %= mod;
            }
        }
        long ans = 0;
        for (int i = 0; i <= n; i++) {
            ans += dp[n][i];
        }
        return ans % mod;
    }
}
