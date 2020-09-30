package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class LTheKnapsackProblemTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            System.out.println("build  testcase " + i);
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
        int n = random.nextInt(1, 3);
        int s = random.nextInt(1, 100);
        int[][] items = new int[n][2];
        for (int i = 0; i < n; i++) {
            items[i][0] = random.nextInt(1, s);
            items[i][1] = random.nextInt(0, 100);
        }

        long ans = dp(items, s);
        StringBuilder in = new StringBuilder();
        printLine(in, n, s);
        for (int[] item : items) {
            printLine(in, item[0], item[1]);
        }
        return new Test(in.toString(), "" + ans);
    }

    public long dp(int[][] items, int s) {
        long[] last = new long[s + 1];
        long[] next = new long[s + 1];
        long inf = (long) 1e18;
        Arrays.fill(last, -inf);
        last[0] = 0;
        for (int[] item : items) {
            Arrays.fill(next, -inf);
            for (int j = 0; j <= s; j++) {
                for (int k = 0; k * item[0] + j <= s; k++) {
                    next[k * item[0] + j] = Math.max(next[k * item[0] + j], last[j] + k * item[1]);
                }
            }
            long[] tmp = last;
            last = next;
            next = tmp;
        }
        long ans = Arrays.stream(last).max().orElse(0);
        return ans;
    }
}
