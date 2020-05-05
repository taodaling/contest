package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.Modular;
import template.rand.RandomWrapper;
import template.rand.Randomized;

public class JJourneyWithKnapsackTestCase {
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
        int[] a = new int[n + 1];
        int m = random.nextInt(1, testNum);

        boolean[] used = new boolean[n * 2 + 1];
        for (int i = 1; i <= n; i++) {
            do {
                a[i] = random.nextInt(0, n * 2);
            } while (used[a[i]]);
            used[a[i]] = true;
        }
        Randomized.shuffle(a, 1, n + 1);
        Arrays.sort(a, 1, n + 1);

        int[] b = new int[m];
        for (int i = 0; i < m; i++) {
            b[i] = random.nextInt(0, 2 * n);
        }

        int ans = solve(a, n, b);

        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for (int i = 1; i <= n; i++) {
            in.append(a[i]).append(' ');
        }
        printLine(in);
        for (int x : b) {
            in.append(x).append(' ');
        }

        return new Test(in.toString(), "Case #1: " + ans);
    }

    public int solve(int[] a, int n, int[] b) {
        Modular mod = new Modular(1e9 + 7);
        int[][] dp = new int[n + 1][n * 2 + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= n * 2; j++) {
                for (int k = 0; k <= a[i] && k * i <= j; k++) {
                    dp[i][j] = mod.plus(dp[i][j], dp[i - 1][j - k * i]);
                }
            }
        }

        int ans = 0;
        for (int x : b) {
            ans = mod.plus(ans, dp[n][2 * n - x]);
        }

        return ans;
    }
}
