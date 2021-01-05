package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.EulerSieve;
import template.rand.RandomWrapper;

public class AMaximumSplittingTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
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
        int n = testNum;
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        printLine(in, n);
        return new Test(in.toString(), solve(n) + "");
    }

    EulerSieve sieve = new EulerSieve(100000);
    int[] dp = new int[10000 + 1];

    {

        int inf = (int) -1e9;
        Arrays.fill(dp, inf);
        dp[0] = 0;
        for (int i = 1; i <= 10000; i++) {
            for (int j = 1; j <= i; j++) {
                if (sieve.isComp(j)) {
                    dp[i] = Math.max(dp[i], dp[i - j] + 1);
                }
            }
        }
    }

    public int solve(int n) {
        return dp[n] < 0 ? -1 : dp[n];
    }


}
