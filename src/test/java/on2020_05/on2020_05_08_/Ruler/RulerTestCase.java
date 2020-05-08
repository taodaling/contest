package on2020_05.on2020_05_08_.Ruler;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerVersionArray;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class RulerTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 1; i <= 998; i++) {
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
        int n = random.nextInt(1, 10);
        int x = random.nextInt(0, testNum);
        return new Test(n + " " + x, "" + dp(n, x));
    }

    int[][] dp = new int[1000][1000];
    int[][] prefix = new int[1000][1000];
    IntegerVersionArray iva = new IntegerVersionArray(10000);

    {
        SequenceUtils.deepFill(dp, -1);
        SequenceUtils.deepFill(prefix, -1);
    }

    public int prefix(int i, int j) {
        if (j < 0) {
            return 0;
        }
        if (prefix[i][j] == -1) {
            prefix[i][j] = prefix(i, j - 1) ^ dp(i, j);
        }
        return prefix[i][j];
    }

    public int dp(int i, int j) {
        if (dp[i][j] == -1) {
            for (int k = Math.max(0, j - i + 1); k <= j; k++) {
                int v = prefix(i, j - 1) ^ prefix(i, k - 1);
            }
            iva.clear();
            for (int k = Math.max(0, j - i + 1); k <= j; k++) {
                int v = prefix(i, j - 1) ^ prefix(i, k - 1);
                iva.set(v, 1);
            }
            dp[i][j] = 0;
            while (iva.get(dp[i][j]) == 1) {
                dp[i][j]++;
            }
        }
        return dp[i][j];
    }

}
