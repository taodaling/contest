package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerVersionArray;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class TaskTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
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
        int limit = testNum;
        int a = random.nextInt(0, limit);
        int b = random.nextInt(0, limit);
        int ans = sg(a, b);

        return new Test("" + a + " " + b, "" + ans);
    }

    int[][] dp = new int[1024][1024];

    {
        SequenceUtils.deepFill(dp, -1);
    }

    IntegerVersionArray iva = new IntegerVersionArray((int)1e6);

    public int sg(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        if (dp[a][b] == -1) {
            for (int i = 0; i < a; i++) {
                for (int j = 0; j < b; j++) {
                    int val = sg(i, j) ^ sg(i, b) ^ sg(a, j);
                }
            }
            iva.clear();
            for (int i = 0; i < a; i++) {
                for (int j = 0; j < b; j++) {
                    int val = sg(i, j) ^ sg(i, b) ^ sg(a, j);
                    iva.set(val, 1);
                }
            }
            dp[a][b] = 0;
            while (iva.get(dp[a][b]) == 1) {
                dp[a][b]++;
            }
        }
        return dp[a][b];
    }
}
