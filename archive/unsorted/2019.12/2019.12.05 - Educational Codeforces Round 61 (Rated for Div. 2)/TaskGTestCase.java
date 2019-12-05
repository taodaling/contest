package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class TaskGTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 5);
        int k = random.nextInt(1, n);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1, 10);
        }
        StringBuilder builder = new StringBuilder();
        builder.append(n).append(' ').append(k).append('\n');
        for (int i = 0; i < n; i++) {
            builder.append(a[i]).append(' ');
        }
        return new Test(builder.toString(), solve(n, k, a));
    }

    public String solve(int n, int k, int[] a) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i + k <= n; i++) {
            builder.append(lis(Arrays.copyOfRange(a, i, i + k))).append('\n');
        }
        return builder.toString();
    }

    private int lis(int[] data) {
        int n = data.length;
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (data[j] < data[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }
        int max = 0;
        for (int i = 0; i < n; i++) {
            max = Math.max(max, dp[i]);
        }
        return max;
    }
}
