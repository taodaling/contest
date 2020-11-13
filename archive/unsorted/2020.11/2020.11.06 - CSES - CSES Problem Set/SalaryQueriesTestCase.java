package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class SalaryQueriesTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, int... vals) {
        for (int val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, long... vals) {
        for (long val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 5);
        int m = random.nextInt(1, 5);
        int[] a = new int[n];
        int[][] b = new int[m][3];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1, 10);
        }
        for (int i = 0; i < m; i++) {
            b[i][0] = random.nextInt(0, 1) == 0 ? '!' : '?';
            if (b[i][0] == '!') {
                b[i][1] = random.nextInt(1, n);
                b[i][2] = random.nextInt(1, 10);
            } else {
                b[i][1] = random.nextInt(1, 10);
                b[i][2] = random.nextInt(1, 10);
                if (b[i][1] > b[i][2]) {
                    SequenceUtils.swap(b[i], 1, 2);
                }
            }
        }

        int[] ans = solve(a, b);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        printLine(in, a);
        for (int[] q : b) {
            in.append((char) q[0]);
            in.append(' ');
            printLine(in, q[1], q[2]);
        }
        StringBuilder out = new StringBuilder();
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int[] a, int[][] b) {
        a = a.clone();
        int m = b.length;
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            if (b[i][0] == '!') {
                int k = b[i][1] - 1;
                int x = b[i][2];
                a[k] = x;
            } else {
                int l = b[i][1];
                int r = b[i][2];
                int cnt = 0;
                for (int x : a) {
                    if (x >= l && x <= r) {
                        cnt++;
                    }
                }
                ans.add(cnt);
            }
        }
        return ans.stream().mapToInt(Integer::intValue).toArray();
    }
}
