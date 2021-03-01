package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerPreSum2D;
import template.rand.RandomWrapper;
import template.rand.Randomized;

public class FOrchestraTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
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

    private <T> void printLineObj(StringBuilder builder, T... vals) {
        for (T val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 3);
        int m = random.nextInt(1, 2);
        int t = random.nextInt(1, n * m);
        int[][] pts = new int[n * m][2];
        for (int i = 0; i < n * m; i++) {
            pts[i][0] = i / m + 1;
            pts[i][1] = i % m + 1;
        }
        Randomized.shuffle(pts);
        pts = Arrays.copyOf(pts, t);
        int k = random.nextInt(1, 3);
        int ans = solve(pts, n, m, k);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n, m, t, k);
        for (int[] pt : pts) {
            printLine(in, pt);
        }
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public int solve(int[][] pts, int n, int m, int k) {
        IntegerPreSum2D ps = new IntegerPreSum2D(n, m);
        int[][] mat = new int[n][m];
        for (int[] pt : pts) {
            mat[pt[0] - 1][pt[1] - 1]++;
        }
        ps.init((i, j) -> mat[i][j], n, m);
        int ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                for (int a = 0; a < m; a++) {
                    for (int b = a; b < m; b++) {
                        ans += ps.rect(i, j, a, b) >= k ? 1 : 0;
                    }
                }
            }
        }
        return ans;
    }
}
