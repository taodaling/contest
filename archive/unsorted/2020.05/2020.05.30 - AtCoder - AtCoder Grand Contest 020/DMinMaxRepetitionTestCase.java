package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.DigitUtils;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class DMinMaxRepetitionTestCase {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int a = random.nextInt(1, 10);
        int b = random.nextInt(1, 10);
        int c = 1;
        int d = a + b;
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        printLine(in, a, b, c, d);
        String ans = solve(a, b);

        return new Test(in.toString(), ans);
    }

    int[][][][] dp;

    private int possible(int a, int b, int preA, int k) {
        if (a < 0 || b < 0 || preA > k) {
            return 0;
        }
        if (dp[a][b][preA][k] == -1) {
            if (a == 0 && b == 0) {
                return dp[a][b][preA][k] = 1;
            }
            dp[a][b][preA][k] = possible(a - 1, b, preA + 1, k) == 1 ||
                    possible(b - 1, a, 1, k) == 1 ? 1 : 0;
        }
        return dp[a][b][preA][k];
    }

    public String solve(int a, int b) {
        int k;
        if (a > b) {
            k = DigitUtils.ceilDiv(a, b + 1);
        } else {
            k = DigitUtils.ceilDiv(b, a + 1);
        }
        dp = new int[Math.max(a, b) + 1][Math.max(a, b) + 1][k + 1][k + 1];
        SequenceUtils.deepFill(dp, -1);
        int preA = 0;
        int preB = 0;
        StringBuilder ans = new StringBuilder();
        while (a > 0 || b > 0) {
            if (possible(a - 1, b, preA + 1, k) == 1) {
                ans.append('A');
                preA++;
                preB = 0;
                a--;
            } else {
                ans.append('B');
                preA = 0;
                preB++;
                b--;
            }
        }
        return ans.toString();
    }
}
