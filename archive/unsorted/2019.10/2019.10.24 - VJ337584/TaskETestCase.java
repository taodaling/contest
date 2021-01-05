package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.google.common.base.Preconditions;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.RandomWrapper;

public class TaskETestCase {
    @TestCase
    public Collection<Test> createTests() {
//        Preconditions.checkState(solve(3, 3, new int[]{0, 9, 2}, new int[][]{
//                {},
//                {0, 5, 7, 3},
//                {0, 3, 10, 8},
//                {0, 7, 7, 8}
//        }) == 21);

        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create());
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create() {
        int n = random.nextInt(1, 3);
        int m = random.nextInt(1, 3);
        int[] a = new int[n];
        for (int i = 1; i < n; i++) {
            a[i] = random.nextInt(1, 10);
        }
        int[][] bs = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                bs[i][j] = random.nextInt(1, 10);
            }
        }

        long ans = solve(n, m, a, bs);

        StringBuilder in = new StringBuilder();
        in.append(n).append(' ').append(m).append('\n');
        for(int i = 1; i < n; i++){
            in.append(a[i]).append(' ');
        }
        in.append('\n');
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= m; j++){
                in.append(bs[i][j]).append(' ');
            }
            in.append('\n');
        }

        return new Test(in.toString(), Long.toString(ans));
    }

    public long solve(int n, int m, int[] a, int[][] b) {
        long ans = 0;
        for (int i = 1; i <= n; i++) {
            int[] hold = new int[m + 1];
            merge(hold, b[i]);
            ans = Math.max(ans, sum(hold));
            long fee = 0;
            for (int j = i + 1; j <= n; j++) {
                merge(hold, b[j]);
                fee += a[j - 1];
                ans = Math.max(ans, sum(hold) - fee);
            }
        }
        return ans;
    }

    public void merge(int[] a, int[] b) {
        for (int i = 1; i < a.length; i++) {
            a[i] = Math.max(a[i], b[i]);
        }
    }

    public long sum(int[] a) {
        long s = 0;
        for (int i = 1; i < a.length; i++) {
            s += a[i];
        }
        return s;
    }
}
