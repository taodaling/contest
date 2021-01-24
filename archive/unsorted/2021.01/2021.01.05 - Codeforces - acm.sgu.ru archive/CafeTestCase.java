package contest;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class CafeTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
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

    private <T> void printLineObj(StringBuilder builder, T... vals) {
        for (T val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(5);

    public Test create(int testNum) {
        int n = random.nextInt(1, 20);
        int r = random.nextInt(3, 20);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(2, 100);
        }
        int ans = solve(a, r);
        StringBuilder in = new StringBuilder();
        printLine(in, n, r);
        printLine(in, a);
        return new Test(in.toString(), "" + ans);
    }

    public int solve(int[] a, int r) {
        int two = 0;
        int three = 0;
        for (int x : a) {
            if (x % 2 == 1) {
                three++;
                x -= 3;
            }
            two += x / 2;
        }
        boolean[][] possible = new boolean[two + 1][three + 1];
        possible[0][0] = true;
        for (int i = 1; ; i++) {
            for (int j = two; j >= 0; j--) {
                for (int k = three; k >= 0; k--) {
                    if (!possible[j][k]) {
                        continue;
                    }
                    for (int t = 0; t * 3 <= r; t++) {
                        int nj = Math.min(two, (r - t * 3) / 2 + j);
                        int nk = Math.min(three, t + k);
                        possible[nj][nk] = true;
                    }
                }
            }
            if (possible[two][three]) {
                return i;
            }
        }
    }
}
