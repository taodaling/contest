package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class TaskFTestCase {
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
        StringBuilder a = new StringBuilder();
        StringBuilder b = new StringBuilder();
        int n = random.nextInt(1, 5);
        int m = random.nextInt(1, 10);
        for (int i = 0; i < n; i++) {
            a.append((char) ('a' + random.nextInt(0, 2)));
        }
        for (int i = 0; i < m; i++) {
            b.append((char) ('a' + random.nextInt(0, 2)));
        }
        String ans = lcs(a.toString().toCharArray(), b.toString().toCharArray());
        StringBuilder in = new StringBuilder();
        in.append(a).append('\n').append(b);
        return new Test(in.toString(), ans);
    }

    public String lcs(char[] a, char[] b) {
        int n = a.length;
        int m = b.length;
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                if (a[i - 1] == b[j - 1]) {
                    dp[i][j] = Math.max(dp[i][j], 1 + dp[i - 1][j - 1]);
                }
            }
        }

        StringBuilder ans = new StringBuilder();
        trace(a, b, dp, n, m, ans);
        return ans.toString();
    }

    private void trace(char[] a, char[] b, int[][] dp, int i, int j,
                       StringBuilder ans) {
        if (i == 0 || j == 0) {
            return;
        }
        if (a[i - 1] == b[j - 1]) {
            trace(a, b, dp, i - 1, j - 1, ans);
            ans.append(a[i - 1]);
            return;
        }
        if (dp[i][j] == dp[i - 1][j]) {
            trace(a, b, dp, i - 1, j, ans);
        } else {
            trace(a, b, dp, i, j - 1, ans);
        }
    }
}
