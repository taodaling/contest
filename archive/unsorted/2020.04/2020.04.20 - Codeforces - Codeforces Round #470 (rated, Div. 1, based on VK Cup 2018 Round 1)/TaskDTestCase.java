package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.ILongModular;
import template.math.LongModular;
import template.math.Modular;
import template.rand.RandomWrapper;

public class TaskDTestCase {
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
        int n = random.nextInt(1, 100);
        int p = (int) 2e9;// / 1024;//random.nextInt((int) 2e9, (int) 2e9);
        int l = 0;
        int r = n;
        int ans = solve(n, p, l, r);

        StringBuilder in = new StringBuilder();
        printLine(in, n, p, l, r);
        return new Test(in.toString(), "" + ans);
    }

    public int solve(int n, int p, int l, int r) {
        long[][] dp = new long[n + 1][n + 1];
        dp[0][0] = 1;

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j > 0) {
                    dp[i][j] += dp[i - 1][j - 1];
                }
                if (j + 1 <= n) {
                    dp[i][j] += dp[i - 1][j + 1];
                }
                dp[i][j] %= p;
            }
        }

        long ans = 0;
        for (int i = l; i <= r; i++) {
            ans += dp[n][i];
        }
        return (int) (ans % p);
    }
}
